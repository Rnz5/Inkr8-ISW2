package com.inkr8.lab

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.ViewModelStore
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.tasks.Tasks
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.inkr8.data.*
import com.inkr8.viewmodel.AppViewModel
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

// Explicit queued-callback and timeout-state doubles around the production handler.
// Not automatic Firestore delivery or proof of elapsed 93 seconds. The unchanged
// integrated later-match method proves actual delivery; old timeout evidence is retained.
@RunWith(AndroidJUnit4::class)
class LabResultRefreshTest {
    @get:Rule val ui = createAndroidComposeRule<LabHostActivity>()
    private val store = ViewModelStore()
    private lateinit var vm: AppViewModel
    private val original = Submissions(id = "refresh-a", playmode = "RANKED",
        status = SubmissionStatus.EVALUATED, matchStatus = "PENDING",
        evaluation = Evaluation(finalScore = 80.0, resultStatus = SubmissionStatus.EVALUATED))
    private val matched = original.copy(matchStatus = "MATCHED",
        evaluation = original.evaluation!!.copy(ratingChange = 1))

    @Before fun setup() {
        assertEquals("demo-inkr8-local", FirebaseApp.getInstance().options.projectId)
        FirebaseAuth.getInstance().signOut()
        val uid = Tasks.await(FirebaseAuth.getInstance().signInAnonymously(), 15, TimeUnit.SECONDS).user!!.uid
        val user = Users(id = uid, isPlaced = true, hasSeenPlacementReveal = true)
        Tasks.await(FirebaseFirestore.getInstance().collection("users").document(uid).set(user), 15, TimeUnit.SECONDS)
        ui.runOnIdle {
            vm = AppViewModel(user)
            store.put("refresh-vm", vm)
            deliver(original)
        }
        assertEquals(Screen.results, vm.currentScreen)
        assertEquals(original, vm.latestSubmission)
    }
    @After fun release() { ui.runOnIdle { store.clear() } }
    private fun deliver(value: Submissions, generation: Long = 0) {
        val method = AppViewModel::class.java.declaredMethods.single { it.name == "handleSubmissionUpdate" }
        method.isAccessible = true
        method.invoke(vm, value, original.id, generation)
    }

    @Test fun wrongDocumentAndOldGenerationCannotRefreshResult() {
        ui.runOnIdle {
            deliver(matched.copy(id = "refresh-b"))
            assertEquals(original, vm.latestSubmission)
            deliver(matched, -1)
            assertEquals(original, vm.latestSubmission)
            deliver(matched)
        }
        assertEquals(matched, vm.latestSubmission)
        assertEquals(Screen.results, vm.currentScreen)
    }

    @Test fun leavingResultsDoesNotRefreshOrNavigateBack() {
        ui.runOnIdle { vm.navigateTo(Screen.home); deliver(matched) }
        assertEquals(original, vm.latestSubmission)
        assertEquals(Screen.home, vm.currentScreen)
    }

    @Test fun timeoutStillIgnoresLateMatchedEvaluation() {
        ui.runOnIdle {
            vm.latestSubmission = null
            vm.loadingResolved = false
            vm.loadingTimeout = true // Explicit state fixture; timer itself unchanged.
            vm.currentScreen = Screen.loading
            deliver(matched)
        }
        assertNull(vm.latestSubmission)
        assertFalse(vm.loadingResolved)
        assertTrue(vm.loadingTimeout)
        assertEquals(Screen.loading, vm.currentScreen)
    }

    @Test fun clearedViewModelIgnoresQueuedMatchedUpdate() {
        ui.runOnIdle { store.clear(); deliver(matched) }
        assertEquals(original, vm.latestSubmission)
        assertEquals(Screen.results, vm.currentScreen)
    }

    @Test fun ghostResolutionRefreshesSameDisplayedResult() {
        val ghost=original.copy(matchStatus="GHOST",
            matchResult=MatchResult(opponentId="GHOST",opponentName="R8 Average",
                opponentScore=65.0,outcome="WIN",ratingChange=2),
            evaluation=original.evaluation!!.copy(ratingChange=2))
        ui.runOnIdle { deliver(ghost) }
        assertEquals(ghost,vm.latestSubmission)
        assertEquals(Screen.results,vm.currentScreen)
    }
}
