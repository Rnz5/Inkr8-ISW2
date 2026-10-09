package com.inkr8.lab

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.inkr8.data.Users
import com.inkr8.repository.FirestoreSubmissionRepository
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

// Real Android Firebase SDK + genuine applyMeritAction in the local emulator.
@RunWith(AndroidJUnit4::class)
class LabCallableTest {
    @get:Rule val ui = createAndroidComposeRule<LabHostActivity>()
    @Test fun saveCallsLocalFunctionAndPreservesExistingMeritCost() {
        FirebaseAuth.getInstance().signOut()
        val uid=Tasks.await(FirebaseAuth.getInstance().signInAnonymously(),15,TimeUnit.SECONDS).user!!.uid
        val db=FirebaseFirestore.getInstance()
        val user=db.collection("users").document(uid)
        Tasks.await(user.set(Users(id=uid,name="Local callable user",merit=10000)),15,TimeUnit.SECONDS)
        val id="lab-callable-"+UUID.randomUUID()
        val doc=db.collection("submissions").document(id)
        Tasks.await(doc.set(mapOf("authorId" to uid,"status" to "EVALUATED","isSaved" to false)),15,TimeUnit.SECONDS)
        val completed=CountDownLatch(1);val error=AtomicReference<Exception?>()
        ui.runOnIdle { FirestoreSubmissionRepository().saveSubmission(id,
            onSuccess={completed.countDown()},onError={error.set(it);completed.countDown()}) }
        assertTrue(completed.await(30,TimeUnit.SECONDS));assertNull(error.get())
        assertEquals(true,Tasks.await(doc.get(),15,TimeUnit.SECONDS).getBoolean("isSaved"))
        val saved=Tasks.await(user.get(),15,TimeUnit.SECONDS)
        assertEquals(8000L,saved.getLong("merit"))
        assertEquals(1L,saved.getLong("savedSubmissionsCount"))
    }
}
