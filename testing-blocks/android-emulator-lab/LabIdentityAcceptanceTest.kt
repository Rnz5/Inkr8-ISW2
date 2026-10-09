package com.inkr8.lab

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.ViewModelStore
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.inkr8.data.*
import com.inkr8.mappers.toFirestore
import com.inkr8.viewmodel.AppViewModel
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID
import java.util.concurrent.TimeUnit

// Genuine confirmation/listener/poll SDK route. Queued-delivery cases explicitly
// invoke the private callback boundary, a scheduling double, not Firebase delivery.
@RunWith(AndroidJUnit4::class)
class LabIdentityAcceptanceTest {
    @get:Rule val ui = createAndroidComposeRule<LabHostActivity>()
    private val db get() = FirebaseFirestore.getInstance()
    private val store = ViewModelStore()
    private lateinit var uid: String
    private lateinit var vm: AppViewModel

    @Before fun setup() {
        FirebaseAuth.getInstance().signOut()
        uid = Tasks.await(FirebaseAuth.getInstance().signInAnonymously(), 15, TimeUnit.SECONDS).user!!.uid
        val user = Users(id = uid, isPlaced = true, hasSeenPlacementReveal = true)
        Tasks.await(db.collection("users").document(uid).set(user), 15, TimeUnit.SECONDS)
        ui.runOnIdle { vm = AppViewModel(user); store.put("identity-vm", vm) }
    }
    @After fun release() { ui.runOnIdle { store.clear() } }

    private fun start(name: String): Submissions {
        val submission = Submissions(id = "lab-identity-$name-" + UUID.randomUUID(), authorId = uid,
            content = "Explicit pending fixture", timestamp = System.currentTimeMillis())
        var persisted = false
        ui.runOnIdle { vm.submitWriting(submission, onPersisted = { persisted = true }) { fail(it) } }
        ui.waitUntil(15000) { persisted && vm.currentScreen == Screen.loading }
        return submission
    }
    private fun evaluated(submission: Submissions) = submission.copy(status = SubmissionStatus.EVALUATED,
        evaluation = Evaluation(finalScore = 75.0, resultStatus = SubmissionStatus.EVALUATED, feedback = "Explicit identity fixture"))
    private fun evaluate(submission: Submissions) {
        Tasks.await(db.collection("submissions").document(submission.id).update(mapOf(
            "status" to "EVALUATED", "evaluation" to evaluated(submission).evaluation!!.toFirestore())), 15, TimeUnit.SECONDS)
    }
    private fun generation(): Long = AppViewModel::class.java.declaredFields
        .firstOrNull { it.name == "loadingWaitGeneration" }?.let { it.isAccessible = true; it.getLong(vm) } ?: 0L
    private fun deliverQueued(submission: Submissions, waitId: String, oldGeneration: Long) {
        val callback = AppViewModel::class.java.declaredMethods.single { it.name == "handleSubmissionUpdate" }
        callback.isAccessible = true
        // Same acceptance across preimage and new signature, without weakening its oracle.
        if (callback.parameterCount == 1) callback.invoke(vm, submission)
        else callback.invoke(vm, submission, waitId, oldGeneration)
    }

    @Test fun pollUsesConfirmedDocumentEvenWhenNewerBExists() {
        val a = start("poll-a")
        ui.runOnIdle {
            val field = AppViewModel::class.java.getDeclaredField("loadingResultListener")
            field.isAccessible = true
            (field.get(vm) as ListenerRegistration).remove()
        }
        val b = evaluated(a).copy(id = "lab-identity-poll-b-" + UUID.randomUUID(), timestamp = a.timestamp + 1000)
        Tasks.await(db.collection("submissions").document(b.id).set(b.toFirestore()), 15, TimeUnit.SECONDS)
        evaluate(a)
        ui.waitUntil(15000) { vm.loadingResolved }
        assertEquals("With the listener removed, the genuine poll must resolve A", a.id, vm.latestSubmission?.id)
        assertEquals(Screen.results, vm.currentScreen)
    }

    @Test fun obsoleteWaitCallbackCannotResolveCurrentWait() {
        val a = start("old-a")
        val oldGeneration = generation()
        val c = start("current-c")
        ui.runOnIdle { deliverQueued(evaluated(a), a.id, oldGeneration) }
        assertFalse("A queued callback from the previous wait must be ignored", vm.loadingResolved)
        assertNull(vm.latestSubmission)
        assertEquals(Screen.loading, vm.currentScreen)
        evaluate(c)
        ui.waitUntil(15000) { vm.loadingResolved }
        assertEquals(c.id, vm.latestSubmission?.id)
    }

    @Test fun disposedViewModelRejectsQueuedWaitResponse() {
        val a = start("disposed")
        val oldGeneration = generation()
        ui.runOnIdle { store.clear(); deliverQueued(evaluated(a), a.id, oldGeneration) }
        assertFalse("Cleared ViewModel must not accept a queued callback", vm.loadingResolved)
        assertNull(vm.latestSubmission)
        assertEquals(Screen.loading, vm.currentScreen)
    }
}
