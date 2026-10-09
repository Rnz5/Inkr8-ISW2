package com.inkr8.lab

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.ViewModelStore
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.inkr8.data.*
import com.inkr8.mappers.toFirestore
import com.inkr8.repository.FirestoreSubmissionRepository
import com.inkr8.screens.Writing
import com.inkr8.utils.DraftManager
import com.inkr8.viewmodel.AppViewModel
import org.junit.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.json.JSONObject
import java.io.File
import java.util.UUID
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

// Red acceptance probes, distinct from AND-002 defect characterization.
// Evaluator delivery is disabled; evaluation updates below are explicit fixtures.
@RunWith(AndroidJUnit4::class)
class LabCoreAcceptanceTest {
    @get:Rule val ui=createAndroidComposeRule<LabHostActivity>()
    private val db get()=FirebaseFirestore.getInstance()
    private lateinit var uid:String
    private val store=ViewModelStore()
    @Before fun setup() {
        FirebaseAuth.getInstance().signOut()
        uid=Tasks.await(FirebaseAuth.getInstance().signInAnonymously(),15,TimeUnit.SECONDS).user!!.uid
    }
    @After fun release() {ui.runOnIdle {store.clear()}}

    @Test fun rejectedPersistencePreservesEditorAndDraft_acceptanceProbe() {
        val text="Bright rivers carry ancient stories while curious writers explore the quiet valley. Every morning a traveler observes changing shadows beside patient trees and remembers distant friends. Gentle rain reveals new colors across the landscape, inspiring thoughtful descriptions of journeys, discoveries, and ordinary moments that deserve careful attention from everyone around us."
        val key=LabDraftFixtures.seed(ui.activity,text)
        val done=CountDownLatch(1);val error=AtomicReference<Exception?>()
        ui.activityRule.scenario.onActivity {activity->activity.show {
            Writing(StandardWriting,PlayMode.Practice,onAddSubmission={submission,onPersisted,onFailure->
                FirestoreSubmissionRepository().addSubmission(submission.copy(
                    id="lab-denied-"+UUID.randomUUID(),authorId=uid),
                    onSuccess={onPersisted();done.countDown()},onError={onFailure();error.set(it);done.countDown()})
            },onNavigateBack={},onNavigateToResults={})
        }}
        ui.onNode(hasClickAction() and hasText("Submit")).performClick()
        assertTrue(done.await(15,TimeUnit.SECONDS));assertNotNull(error.get())
        val storedDraft=DraftManager.getDraft(ui.activity,key)
        File(ui.activity.filesDir,"core-draft-observation.json").writeText(JSONObject(mapOf(
            "expectedTextLength" to text.length,"storedDraftLength" to storedDraft.length,
            "persistenceFailed" to true)).toString(2))
        // Human decision: failed persistence must preserve BOTH the editor and draft.
        ui.onNode(hasSetTextAction()).assertTextContains(text)
        assertEquals(text,storedDraft)
    }

    @Test fun resultBMustNotResolveSubmissionA_acceptanceProbe() {
        val user=Users(id=uid,isPlaced=true,hasSeenPlacementReveal=true)
        Tasks.await(db.collection("users").document(uid).set(user),15,TimeUnit.SECONDS)
        lateinit var vm:AppViewModel
        ui.runOnIdle {vm=AppViewModel(user);store.put("core-vm",vm)}
        val a=Submissions(id="lab-core-wait-a-"+UUID.randomUUID(),authorId=uid,
            content="Pending fixture",timestamp=System.currentTimeMillis())
        ui.runOnIdle {vm.submitWriting(a) {fail("Persistence failed: $it")}}
        ui.waitUntil(15000) {vm.currentScreen==Screen.loading}
        val b=a.copy(id="lab-core-wait-b-"+UUID.randomUUID(),timestamp=a.timestamp+1000,
            status=SubmissionStatus.EVALUATED,evaluation=Evaluation(finalScore=80.0,
                resultStatus=SubmissionStatus.EVALUATED,feedback="B is an explicit fixture"))
        Tasks.await(db.collection("submissions").document(b.id).set(b.toFirestore()),15,TimeUnit.SECONDS)
        // Wait through a genuine listener resolution or the first production poll.
        ui.waitUntil(7000) {vm.loadingResolved || vm.loadingElapsedSeconds>=3}
        File(ui.activity.filesDir,"core-identity-observation.json").writeText(JSONObject(mapOf(
            "expectedSubmissionId" to a.id,"actualSubmissionId" to (vm.latestSubmission?.id ?: ""),
            "resolvedBeforeA" to vm.loadingResolved)).toString(2))
        assertFalse("B must not resolve the wait initiated for A",vm.loadingResolved)
        assertNull(vm.latestSubmission)
        Tasks.await(db.collection("submissions").document(a.id).update(mapOf(
            "status" to "EVALUATED","evaluation" to Evaluation(finalScore=75.0,
                resultStatus=SubmissionStatus.EVALUATED,feedback="A fixture").toFirestore())),15,TimeUnit.SECONDS)
        ui.waitUntil(15000) {vm.loadingResolved}
        assertEquals(a.id,vm.latestSubmission?.id)
        assertEquals(Screen.results,vm.currentScreen)
    }
}
