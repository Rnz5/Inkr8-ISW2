package com.inkr8.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.inkr8.data.Users
import com.google.firebase.functions.FirebaseFunctions
import com.inkr8.utils.SystemConfig
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class UserRepository(
    private val functions: FirebaseFunctions = FirebaseFunctions.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private val usersCollection = firestore.collection(SystemConfig.USERS_COLLECTION)

    fun listenToUser(userId: String): Flow<Users?> = callbackFlow {
        val listener = usersCollection.document(userId).addSnapshotListener { snapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                trySend(snapshot.toObject(Users::class.java))
            } else {
                trySend(null)
            }
        }
        awaitClose { listener.remove() }
    }

    fun getAllUsers(onResult: (List<Users>) -> Unit) {
        usersCollection
            .get()
            .addOnSuccessListener { snapshot ->
                val users = snapshot.toObjects(Users::class.java)
                onResult(users)
            }
    }

    fun ensureUserExists(
        uid: String,
        email: String?,
        onReady: (Users) -> Unit
    ) {
        val docRef = usersCollection.document(uid)

        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            if (snapshot.exists()) {
                snapshot.toObject(Users::class.java) ?: throw Exception("Data corruption: User exists but could not be parsed.")
            } else {
                val newUser = Users(
                    id = uid,
                    email = email,
                    name = "" // Use name="" as a fallback, actual default in data class is also empty string.
                )
                transaction.set(docRef, newUser)
                newUser
            }
        }.addOnSuccessListener { user ->
            onReady(user)
        }.addOnFailureListener { e ->
            e.printStackTrace()
        }
    }

    fun validateUsername(username: String): String? {
        if (username.length < 2) return "Username must be at least 2 characters."
        if (username.length > 20) return "Username must be at most 20 characters."

        val allowedRegex = Regex("^[A-Za-z0-9_.,*{}\\[\\]()√]+$")
        if (!allowedRegex.matches(username)) {
            return "Username contains invalid characters."
        }

        val hasLetterOrDigit = username.any { it.isLetterOrDigit() }
        if (!hasLetterOrDigit) {
            return "Username must contain at least one letter or number."
        }

        return null
    }

    private fun normalizeUsername(username: String): String {
        return username.trim().lowercase()
    }

    fun claimUsername(
        userId: String,
        username: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val validationError = validateUsername(username)
        if (validationError != null) {
            onError(Exception(validationError))
            return
        }

        val normalized = normalizeUsername(username)
        val usernameRef = firestore.collection(SystemConfig.USERNAMES_COLLECTION).document(normalized)
        val userRef = usersCollection.document(userId)

        firestore.runTransaction { transaction ->
            val usernameSnapshot = transaction.get(usernameRef)
            val userSnapshot = transaction.get(userRef)

            if (!userSnapshot.exists()) {
                throw Exception("User document not found.")
            }

            if (usernameSnapshot.exists()) {
                throw Exception("That username is already taken :(")
            }

            transaction.set(
                usernameRef,
                mapOf(
                    "userId" to userId,
                    "username" to username,
                    "normalized" to normalized,
                    "createdAt" to System.currentTimeMillis()
                )
            )

            transaction.update(
                userRef,
                mapOf(
                    "name" to username,
                    "hasChosenUsername" to true
                )
            )
        }.addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener {
            onError(Exception(it.message ?: "Failed to claim username."))
        }
    }

    fun isUsernameAvailable(
        username: String,
        onResult: (Boolean) -> Unit
    ) {
        val normalized = username.trim().lowercase()

        firestore.collection(SystemConfig.USERNAMES_COLLECTION)
            .document(normalized)
            .get()
            .addOnSuccessListener { snapshot ->
                onResult(!snapshot.exists())
            }
            .addOnFailureListener {
                it.printStackTrace()
                onResult(false)
            }
    }

    fun changeUsernameWithMerit(
        newUsername: String,
        onSuccess: (Map<String, Any>?) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val data = hashMapOf(
            "action" to "CHANGE_USERNAME",
            "newUsername" to newUsername
        )

        functions
            .getHttpsCallable(SystemConfig.APPLY_MERIT_ACTION)
            .call(data)
            .addOnSuccessListener { result ->
                val resData = result.data as? Map<String, Any>
                val updates = resData?.get("updatedFields") as? Map<String, Any>
                onSuccess(updates)
            }
            .addOnFailureListener { error ->
                onError(Exception(error.message ?: "Failed to change username"))
            }
    }

    fun deleteAccount(
        userId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val userRef = usersCollection.document(userId)

        firestore.runTransaction { transaction ->

            val snapshot = transaction.get(userRef)
            if (!snapshot.exists()) throw Exception("User not found")

            val username = snapshot.getString("name") ?: ""
            val normalized = username.lowercase()

            val usernameRef = firestore.collection(SystemConfig.USERNAMES_COLLECTION).document(normalized)

            transaction.delete(usernameRef)

            transaction.delete(userRef)

        }.addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener {
            onError(Exception(it.message ?: "Failed to delete account"))
        }
    }

    fun applyMeritAction(
        action: String,
        onSuccess: (Map<String, Any>?) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val data = hashMapOf(
            "action" to action
        )

        functions
            .getHttpsCallable(SystemConfig.APPLY_MERIT_ACTION)
            .call(data)
            .addOnSuccessListener { result ->
                val resData = result.data as? Map<String, Any>
                val updates = resData?.get("updatedFields") as? Map<String, Any>
                onSuccess(updates)
            }
            .addOnFailureListener { error: Exception ->
                onError(Exception(error.message ?: "Failed to apply merit action"))
            }
    }

    fun getUserById(
        userId: String,
        onResult: (Users?) -> Unit
    ) {
        usersCollection.document(userId)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    onResult(snapshot.toObject(Users::class.java))
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
                onResult(null)
            }
    }

    fun updateEmail(userId: String, email: String?) {
        usersCollection.document(userId)
            .update("email", email)
    }

    fun updateRatingAndStreak(
        userId: String,
        newRating: Long,
        winStreak: Long,
        lossStreak: Long
    ) {
        val updates = mapOf(
            "rating" to newRating,
            "rankedWinStreak" to winStreak,
            "rankedLossStreak" to lossStreak
        )

        usersCollection.document(userId).update(updates)
    }

    fun startRankedSession(userId: String) {
        val updates = mapOf("currentlyInRanked" to true, "rankedSessionStartedAt" to System.currentTimeMillis())

        usersCollection.document(userId).update(updates)
    }

    fun finishRankedSession(userId: String) {
        val updates = mapOf("currentlyInRanked" to false, "rankedSessionStartedAt" to null)

        usersCollection.document(userId).update(updates)
    }

    fun enablePhilosopher(
        purchaseToken: String,
        productId: String,
        onSuccess: (Map<String, Any>?) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val data = hashMapOf(
            "purchaseToken" to purchaseToken,
            "productId" to productId
        )

        functions
            .getHttpsCallable(SystemConfig.ACTIVATE_PHILOSOPHER_STATUS)
            .call(data)
            .addOnSuccessListener { result ->
                val resData = result.data as? Map<String, Any>
                val updates = resData?.get("updatedFields") as? Map<String, Any>
                onSuccess(updates)
            }
            .addOnFailureListener { onError(it) }
    }

    fun markPlacementRevealSeen(
        userId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        usersCollection.document(userId)
            .update("hasSeenPlacementReveal", true)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }


}
