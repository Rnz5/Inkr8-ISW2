package com.inkr8.lab

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.*
import androidx.compose.ui.graphics.asAndroidBitmap
import android.graphics.Bitmap
import java.io.File
import androidx.lifecycle.ViewModelStore
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.inkr8.data.*
import com.inkr8.mappers.toFirestore
import com.inkr8.repository.FirestoreSubmissionRepository
import com.inkr8.viewmodel.AppViewModel
import com.inkr8.screens.LoadingScreen
import org.junit.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

// Actual ViewModel/repository/listeners; evaluator writes are explicit test fixtures.
@RunWith(AndroidJUnit4::class)
class LabWaitTest {
    @get:Rule val ui = createAndroidComposeRule<LabHostActivity>()
    private val db get() = FirebaseFirestore.getInstance()
    private lateinit var uid: String
    private lateinit var vm: AppViewModel
    private val store = ViewModelStore()
    @Before fun setup() {
        FirebaseAuth.getInstance().signOut()
        uid=Tasks.await(FirebaseAuth.getInstance().signInAnonymously(),15,TimeUnit.SECONDS).user!!.uid
        val user=Users(id=uid,name="Laboratory user",isPlaced=true,hasSeenPlacementReveal=true,isPhilosopher=true)
        Tasks.await(db.collection("users").document(uid).set(user),15,TimeUnit.SECONDS)
        ui.runOnIdle { vm=AppViewModel(user); store.put("lab-vm",vm) }
    }
    @After fun release() { ui.runOnIdle { store.clear() } }
    private fun submit(): Submissions {
        val a=Submissions(id="lab-wait-"+UUID.randomUUID(),authorId=uid,content="Local pending fixture",
            timestamp=System.currentTimeMillis())
        ui.runOnIdle { vm.submitWriting(a) { fail("Unexpected persistence failure: $it") } }
        ui.waitUntil(15000) { vm.currentScreen==Screen.loading }
        return a
    }
    private fun evaluate(a: Submissions) {
        Tasks.await(db.collection("submissions").document(a.id).update(mapOf(
            "status" to "EVALUATED", "evaluation" to Evaluation(finalScore=75.0,
                resultStatus=SubmissionStatus.EVALUATED,feedback="Explicit evaluator fixture").toFirestore()
        )),15,TimeUnit.SECONDS)
    }
    @Test fun serverProfileFlagsAreReadByTheActualUserModel() {
        val ref=db.collection("users").document(uid)
        Tasks.await(ref.update(mapOf("isPlaced" to true,"isPhilosopher" to true)),15,TimeUnit.SECONDS)
        val snap=Tasks.await(ref.get(),15,TimeUnit.SECONDS)
        assertEquals(true,snap.getBoolean("isPlaced"))
        val actual=snap.toObject(Users::class.java)!!
        // Acceptance probe: keep a real mapping failure visible; do not weaken it.
        assertTrue("Stored isPlaced=true must reach the user model",actual.isPlaced)
        assertTrue("Stored isPhilosopher=true must reach the user model",actual.isPhilosopher)
    }
    @Test fun newerBResolvesTheWaitForA_characterizedGap() {
        val a=submit()
        val b=a.copy(id="lab-newer-"+UUID.randomUUID(), timestamp=a.timestamp+1000,
            status=SubmissionStatus.EVALUATED,evaluation=Evaluation(finalScore=82.0,
                resultStatus=SubmissionStatus.EVALUATED,feedback="B evaluator fixture"))
        Tasks.await(db.collection("submissions").document(b.id).set(b.toFirestore()),15,TimeUnit.SECONDS)
        ui.waitUntil(15000) { vm.loadingResolved }
        assertEquals(b.id,vm.latestSubmission?.id) // Gap, not acceptance of identity A.
        assertEquals(Screen.results,vm.currentScreen)
        evaluate(a)
        assertEquals(b.id,vm.latestSubmission?.id)
    }
    @Test fun failedSubmissionReturnsHome() {
        val a=submit()
        Tasks.await(db.collection("submissions").document(a.id).update("status","FAILED"),15,TimeUnit.SECONDS)
        ui.waitUntil(15000) { vm.loadingResolved }
        assertEquals(Screen.home,vm.currentScreen)
    }
    @Test fun queryErrorDoesNotResolveOrExposeFailure() {
        Tasks.await(db.collection("labDeniedReads").document(uid).set(mapOf("fixture" to true)),15,TimeUnit.SECONDS)
        submit()
        val failed=CountDownLatch(1); val error=AtomicReference<Exception?>()
        ui.runOnIdle { FirestoreSubmissionRepository().getLastSubmission(
            onSuccess={failed.countDown()},onError={error.set(it);failed.countDown()}) }
        assertTrue(failed.await(15,TimeUnit.SECONDS)); assertNotNull(error.get())
        assertFalse(vm.loadingResolved); assertFalse(vm.loadingTimeout)
        assertEquals(Screen.loading,vm.currentScreen)
    }
    @Test fun actualTimeoutAt93SecondsIgnoresLateResult() {
        val a=submit()
        ui.activityRule.scenario.onActivity { it.show { LoadingScreen(vm.loadingElapsedSeconds,vm.loadingTimeout) {} } }
        ui.waitUntil(110000) { vm.loadingTimeout }
        ui.onNodeWithText("JUDGMENT DELAYED").assertExists()
        File(ui.activity.filesDir,"wait-timeout.png").outputStream().use {
            ui.onRoot().captureToImage().asAndroidBitmap().compress(Bitmap.CompressFormat.PNG,100,it)
        }
        assertEquals(93,vm.loadingElapsedSeconds)
        assertFalse(vm.loadingResolved)
        evaluate(a)
        // Observe the actual Firestore update, then allow its listener to deliver.
        assertEquals("EVALUATED",Tasks.await(db.collection("submissions").document(a.id).get(),15,TimeUnit.SECONDS).getString("status"))
        Thread.sleep(1000)
        assertFalse(vm.loadingResolved); assertNull(vm.latestSubmission)
        assertEquals(Screen.loading,vm.currentScreen)
    }
}
