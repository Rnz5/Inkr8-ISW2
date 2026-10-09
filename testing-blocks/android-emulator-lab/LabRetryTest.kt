package com.inkr8.lab

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.ViewModelStore
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.inkr8.data.*
import com.inkr8.mappers.toFirestore
import com.inkr8.screens.LoadingScreen
import com.inkr8.viewmodel.AppViewModel
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.json.JSONObject
import java.io.File
import java.util.UUID
import java.util.concurrent.TimeUnit

// Real SDK/query denial/timer/UI retry. Evaluations and obsolete callback deliveries
// are explicit fixtures, no external R8/CloudEvent exactly-once claim.
@RunWith(AndroidJUnit4::class)
class LabRetryTest {
    @get:Rule val ui=createAndroidComposeRule<LabHostActivity>()
    private val db get()=FirebaseFirestore.getInstance()
    private val store=ViewModelStore()
    private lateinit var uid:String
    private lateinit var vm:AppViewModel
    @Before fun setup() {
        FirebaseAuth.getInstance().signOut()
        uid=Tasks.await(FirebaseAuth.getInstance().signInAnonymously(),15,TimeUnit.SECONDS).user!!.uid
        val user=Users(id=uid,isPlaced=true,hasSeenPlacementReveal=true,merit=100)
        Tasks.await(db.collection("users").document(uid).set(user),15,TimeUnit.SECONDS)
        ui.runOnIdle {vm=AppViewModel(user);store.put("retry",vm)}
    }
    @After fun cleanup() {ui.runOnIdle {store.clear()}}
    private fun show()=ui.activityRule.scenario.onActivity {it.show {
        LoadingScreen(elapsedSeconds=vm.loadingElapsedSeconds,isTimeout=vm.loadingTimeout,
            queryError=vm.loadingQueryError,onRetry={vm.retryLoadingResult()},onReturnHome={vm.navigateTo(Screen.home)})
    }}
    private fun submit():Submissions {
        val a=Submissions(id="lab-retry-"+UUID.randomUUID(),authorId=uid,content="Private test content must not be in retry logs",timestamp=System.currentTimeMillis())
        ui.runOnIdle {vm.submitWriting(a){fail(it)}}
        ui.waitUntil(15000){vm.currentScreen==Screen.loading}
        show();return a
    }
    private fun evaluate(a:Submissions) {
        Tasks.await(db.collection("submissions").document(a.id).update(mapOf("status" to "EVALUATED",
            "evaluation" to Evaluation(finalScore=75.0,resultStatus=SubmissionStatus.EVALUATED,feedback="Explicit retry evaluation fixture").toFirestore())),15,TimeUnit.SECONDS)
    }
    private fun generation():Long=AppViewModel::class.java.getDeclaredField("loadingWaitGeneration").apply{isAccessible=true}.getLong(vm)
    private fun deliver(s:Submissions,id:String,g:Long) {
        AppViewModel::class.java.getDeclaredMethod("handleSubmissionUpdate",Submissions::class.java,String::class.java,java.lang.Long.TYPE)
            .apply{isAccessible=true}.invoke(vm,s,id,g)
    }
    private fun count()=Tasks.await(db.collection("submissions").whereEqualTo("authorId",uid).get(Source.SERVER),15,TimeUnit.SECONDS).size()
    @Test fun realQueryErrorNotifiesRetryReadsSameIdAndRecoversAvailableResult() {
        val marker=db.collection("labDeniedReads").document(uid)
        Tasks.await(marker.set(mapOf("fixture" to true,"denied" to true)),15,TimeUnit.SECONDS)
        val a=submit()
        ui.waitUntil(15000){vm.loadingQueryError!=null}
        ui.onNodeWithText("RESULT CHECK UNAVAILABLE").assertExists()
        ui.onNodeWithText("Your entry is saved",substring=true).assertExists()
        val before=Tasks.await(db.collection("users").document(uid).get(Source.SERVER),15,TimeUnit.SECONDS).data
        evaluate(a)
        Tasks.await(marker.update("denied",false),15,TimeUnit.SECONDS) // Keep marker/history, do not delete.
        val stored=Tasks.await(db.collection("submissions").document(a.id).get(Source.SERVER),15,TimeUnit.SECONDS).data
        val old=generation()
        ui.onNodeWithText("Retry result check").performClick()
        ui.waitUntil(15000){vm.loadingResolved}
        assertTrue(generation()>old);assertEquals(a.id,vm.latestSubmission?.id);assertEquals(Screen.results,vm.currentScreen)
        assertEquals(1,count());assertEquals(stored,Tasks.await(db.collection("submissions").document(a.id).get(Source.SERVER),15,TimeUnit.SECONDS).data)
        assertEquals(before,Tasks.await(db.collection("users").document(uid).get(Source.SERVER),15,TimeUnit.SECONDS).data)
        File(ui.activity.filesDir,"retry-query.json").writeText(JSONObject(mapOf("submissionId" to a.id,"generationBefore" to old,"generationAfter" to generation(),"documentCount" to count(),"writesAndUserEffectsUnchanged" to true,"evaluation" to "explicit fixture")).toString(2))
    }
    @Test fun retryGenerationRejectsOldCallbacksAndOtherDocument() {
        val marker=db.collection("labDeniedReads").document(uid)
        Tasks.await(marker.set(mapOf("fixture" to true,"denied" to true)),15,TimeUnit.SECONDS)
        val a=submit();ui.waitUntil(15000){vm.loadingQueryError!=null}
        val old=generation();ui.onNodeWithText("Retry result check").performClick()
        ui.waitUntil(15000){generation()>old && vm.loadingQueryError!=null}
        val current=generation()
        ui.runOnIdle {
            deliver(a.copy(status=SubmissionStatus.EVALUATED),a.id,old)
            deliver(a.copy(status=SubmissionStatus.FAILED),a.id,old)
            deliver(a.copy(id="other-B",status=SubmissionStatus.EVALUATED),a.id,current)
        }
        assertFalse(vm.loadingResolved);assertNull(vm.latestSubmission);assertEquals(Screen.loading,vm.currentScreen)
        Tasks.await(marker.update("denied",false),15,TimeUnit.SECONDS)
        evaluate(a)
        ui.onNodeWithText("Retry result check").performClick()
        ui.waitUntil(15000){vm.loadingResolved};assertEquals(a.id,vm.latestSubmission?.id);assertEquals(1,count())
    }
    @Test fun timeoutAtOriginal93SecondsThenRetryRecoversLateResultWithoutWrites() {
        val a=submit();val old=generation()
        ui.waitUntil(110000){vm.loadingTimeout}
        assertEquals(93,vm.loadingElapsedSeconds);assertFalse(vm.loadingResolved)
        ui.onNodeWithText("JUDGMENT DELAYED").assertExists()
        ui.onNodeWithText("Retry result check").assertExists()
        evaluate(a)
        Thread.sleep(1000)
        assertFalse(vm.loadingResolved);assertNull(vm.latestSubmission)
        val stored=Tasks.await(db.collection("submissions").document(a.id).get(Source.SERVER),15,TimeUnit.SECONDS).data
        val before=Tasks.await(db.collection("users").document(uid).get(Source.SERVER),15,TimeUnit.SECONDS).data
        ui.onNodeWithText("Retry result check").performClick()
        ui.waitUntil(15000){vm.loadingResolved}
        assertTrue(generation()>old);assertFalse(vm.loadingTimeout);assertEquals(a.id,vm.latestSubmission?.id)
        assertEquals(1,count());assertEquals(stored,Tasks.await(db.collection("submissions").document(a.id).get(Source.SERVER),15,TimeUnit.SECONDS).data)
        assertEquals(before,Tasks.await(db.collection("users").document(uid).get(Source.SERVER),15,TimeUnit.SECONDS).data)
        File(ui.activity.filesDir,"retry-timeout.json").writeText(JSONObject(mapOf("submissionId" to a.id,"originalTimeoutSeconds" to 93,"retriedGeneration" to generation(),"queryOnly" to true)).toString(2))
    }
    @Test fun retriedFailedTerminalStillReturnsHomeAndCannotRetryFromHome() {
        val marker=db.collection("labDeniedReads").document(uid)
        Tasks.await(marker.set(mapOf("denied" to true)),15,TimeUnit.SECONDS)
        val a=submit();ui.waitUntil(15000){vm.loadingQueryError!=null}
        Tasks.await(db.collection("submissions").document(a.id).update("status","FAILED"),15,TimeUnit.SECONDS)
        Tasks.await(marker.update("denied",false),15,TimeUnit.SECONDS)
        ui.onNodeWithText("Retry result check").performClick()
        ui.waitUntil(15000){vm.loadingResolved}
        assertEquals(Screen.home,vm.currentScreen);assertNull(vm.latestSubmission)
        val g=generation();ui.runOnIdle{vm.retryLoadingResult()};assertEquals(g,generation());assertEquals(1,count())
    }

    @Test fun returningToWritingInvalidatesPreviousWaitBeforeNewPersistence() {
        val a=submit();val old=generation()
        ui.runOnIdle {
            vm.startWriting(StandardWriting,PlayMode.Practice)
            deliver(a.copy(status=SubmissionStatus.EVALUATED),a.id,old)
        }
        assertEquals("A previous wait must not navigate away from a new editor",Screen.writing,vm.currentScreen)
        assertNull(vm.latestSubmission)
        assertFalse(vm.loadingResolved)
    }
}
