package com.inkr8.lab

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.ViewModelProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.tasks.Tasks
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.inkr8.AppRoot
import com.inkr8.AppViewModelFactory
import com.inkr8.data.*
import com.inkr8.utils.DraftManager
import com.inkr8.viewmodel.AppViewModel
import org.json.JSONObject
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.util.UUID
import java.util.concurrent.TimeUnit

// Genuine Android/root/SDK -> local Firestore event -> genuine Functions engine.
// Only R8 HTTP, anonymous authentication and exercise entry are explicit fixtures.
// No Google OAuth, real provider, Home Ranked entry charge or real cloud is tested.
@RunWith(AndroidJUnit4::class)
class LabIntegratedFlowTest {
    @get:Rule val ui = createAndroidComposeRule<LabHostActivity>()
    private val db get() = FirebaseFirestore.getInstance()
    private val text = "Bright rivers carry ancient stories while curious writers explore the quiet valley. Every morning a traveler observes changing shadows beside patient trees and remembers distant friends. Gentle rain reveals new colors across the landscape, inspiring thoughtful descriptions of journeys, discoveries, and ordinary moments that deserve careful attention from everyone around us."
    private lateinit var user: Users
    private lateinit var vm: AppViewModel

    @Before fun localOnly() {
        assertEquals("demo-inkr8-local", FirebaseApp.getInstance().options.projectId)
        assertEquals("com.inkr8.lab", ui.activity.packageName)
        Tasks.await(db.enableNetwork(), 15, TimeUnit.SECONDS)
        FirebaseAuth.getInstance().signOut()
        val uid = Tasks.await(FirebaseAuth.getInstance().signInAnonymously(), 15, TimeUnit.SECONDS).user!!.uid
        // High rating isolates this pair from preserved fixtures in the shared lab.
        // This is a fixture value, not a new product rating or compatibility rule.
        user = Users(id = uid, name = "Integrated local fixture", rating = 5000,
            isPlaced = true, hasSeenPlacementReveal = true)
        Tasks.await(db.collection("users").document(uid).set(user), 15, TimeUnit.SECONDS)
        ui.runOnIdle {
            vm = ViewModelProvider(ui.activity, AppViewModelFactory(user))[user.id, AppViewModel::class.java]
        }
    }

    @After fun release() {
        ui.runOnIdle { ui.activity.viewModelStore.clear() }
    }

    private fun writeAndWait(mode: PlayMode): String {
        val modeName = when (mode) {
            PlayMode.Practice -> "PRACTICE"
            PlayMode.Ranked -> "RANKED"
        }
        val key = LabDraftFixtures.seed(ui.activity, text, play=mode)
        ui.runOnIdle { vm.startWriting(StandardWriting, mode) }
        ui.activityRule.scenario.onActivity { activity ->
            activity.show {
                val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { }
                AppRoot(initialUser = user, googleLauncher = launcher, onSessionEnded = {})
            }
        }
        ui.onNode(hasSetTextAction()).assertTextContains(text)
        ui.onNode(hasClickAction() and hasText("Submit")).performClick()
        ui.waitUntil(40000) { vm.currentScreen == Screen.results || vm.currentScreen == Screen.home }
        assertEquals("Genuine evaluation must navigate to Results", Screen.results, vm.currentScreen)
        val result = vm.latestSubmission!!
        val server = Tasks.await(db.collection("submissions").document(result.id).get(Source.SERVER), 15, TimeUnit.SECONDS)
        assertEquals(text, server.getString("content"))
        assertEquals(result.id, server.getString("evaluation.submissionId"))
        assertEquals("EVALUATED", server.getString("status"))
        assertEquals(80.0, result.evaluation!!.finalScore, 0.0)
        assertEquals("Explicit local transport fixture. No provider called.", result.evaluation!!.feedback)
        assertEquals("", DraftManager.getDraft(ui.activity, key))
        ui.onNodeWithText("80.00%").assertExists()
        return result.id
    }

    @Test fun practicePersistsEvaluatesAndShowsActualResults() {
        val id = writeAndWait(PlayMode.Practice)
        val serverUser = Tasks.await(db.collection("users").document(user.id).get(Source.SERVER), 15, TimeUnit.SECONDS)
        assertEquals(1L, serverUser.getLong("submissionsCount"))
        assertEquals(1305L, serverUser.getLong("merit"))
        assertEquals(305L, vm.latestSubmission!!.evaluation!!.meritEarned)
        ui.onNodeWithText("+305").assertExists()
        ui.onNodeWithText("Explicit local transport fixture. No provider called.").assertExists()
        File(ui.activity.filesDir, "integrated-practice.json").writeText(JSONObject(mapOf(
            "submissionId" to id, "screen" to vm.currentScreen.name,
            "score" to 80, "merit" to serverUser.getLong("merit"),
            "delivery" to "automatic local Firestore event", "r8" to "HTTP double"
        )).toString(2))
    }

    @Test fun rankedCommitsReciprocalMatchAndShowsFinalRating() {
        val opponentId = "lab-integrated-opponent-" + UUID.randomUUID()
        val candidateId = "lab-integrated-candidate-" + UUID.randomUUID()
        val opponent = Users(id = opponentId, name = "Local opponent", rating = 5000,
            isPlaced = true, hasSeenPlacementReveal = true)
        Tasks.await(db.collection("users").document(opponentId).set(opponent), 15, TimeUnit.SECONDS)
        // Already evaluated candidate is a fixture; engine skips it on creation.
        Tasks.await(db.collection("submissions").document(candidateId).set(mapOf(
            "id" to candidateId, "authorId" to opponentId, "playmode" to "RANKED",
            "status" to "EVALUATED", "matchStatus" to "PENDING", "timestamp" to System.currentTimeMillis(),
            "evaluation" to mapOf("submissionId" to candidateId, "finalScore" to 70.0)
        )), 15, TimeUnit.SECONDS)
        val id = writeAndWait(PlayMode.Ranked)
        var server = Tasks.await(db.collection("submissions").document(id).get(Source.SERVER), 15, TimeUnit.SECONDS)
        val deadline = System.currentTimeMillis() + 20000
        while (server.getString("matchStatus") != "MATCHED" && System.currentTimeMillis() < deadline) {
            Thread.sleep(200)
            server = Tasks.await(db.collection("submissions").document(id).get(Source.SERVER), 15, TimeUnit.SECONDS)
        }
        assertEquals("MATCHED", server.getString("matchStatus"))
        val candidate = Tasks.await(db.collection("submissions").document(candidateId).get(Source.SERVER), 15, TimeUnit.SECONDS)
        val me = Tasks.await(db.collection("users").document(user.id).get(Source.SERVER), 15, TimeUnit.SECONDS)
        val them = Tasks.await(db.collection("users").document(opponentId).get(Source.SERVER), 15, TimeUnit.SECONDS)
        assertEquals(opponentId, server.getString("matchResult.opponentId"))
        assertEquals(user.id, candidate.getString("matchResult.opponentId"))
        assertEquals("MATCHED", candidate.getString("matchStatus"))
        assertEquals(2L, server.getLong("matchResult.ratingChange"))
        assertEquals(-6L, candidate.getLong("matchResult.ratingChange"))
        assertEquals(5002L, me.getLong("rating"))
        assertEquals(4994L, them.getLong("rating"))
        assertEquals(1457L, me.getLong("merit"))
        assertEquals(1L, me.getLong("submissionsCount"))
        ui.onNodeWithText("+457").assertExists()
        // Evidence is saved BEFORE the final UI acceptance assertions, including failures.
        File(ui.activity.filesDir, "integrated-ranked.json").writeText(JSONObject(mapOf(
            "submissionId" to id, "candidateId" to candidateId,
            "serverMatchStatus" to server.getString("matchStatus"),
            "serverRatingChange" to server.getLong("matchResult.ratingChange"),
            "viewModelMatchStatus" to vm.latestSubmission!!.matchStatus,
            "viewModelRatingChange" to vm.latestSubmission!!.evaluation!!.ratingChange,
            "serverMerit" to me.getLong("merit"),
            "delivery" to "automatic local Firestore event", "r8" to "HTTP double"
        )).toString(2))
        ui.waitUntil(8000) { vm.latestSubmission?.matchStatus == "MATCHED" }
        assertEquals(2L, vm.latestSubmission!!.evaluation!!.ratingChange)
        ui.onNodeWithText("+2").assertExists()
    }

    @Test fun laterAutomaticMatchUpdatesAlreadyDisplayedSameResult() {
        // Separate high-rating pool: A has no candidate when it is evaluated.
        user = user.copy(rating = 9000)
        Tasks.await(db.collection("users").document(user.id).set(user), 15, TimeUnit.SECONDS)
        val id = writeAndWait(PlayMode.Ranked)
        assertEquals("PENDING", vm.latestSubmission!!.matchStatus)
        ui.onNodeWithText("Pending").assertExists()
        val opponentId = "lab-later-opponent-" + UUID.randomUUID()
        val opponentSubmissionId = "lab-later-submission-" + UUID.randomUUID()
        Tasks.await(db.collection("users").document(opponentId).set(
            Users(id = opponentId, name = "Later local opponent", rating = 9000,
                isPlaced = true, hasSeenPlacementReveal = true)
        ), 15, TimeUnit.SECONDS)
        // Second writer is a fixture, but evaluation/event/match delivery are real.
        Tasks.await(db.collection("submissions").document(opponentSubmissionId).set(mapOf(
            "id" to opponentSubmissionId, "authorId" to opponentId, "content" to text,
            "wordCount" to 51, "characterCount" to text.length,
            "gamemodeName" to "STANDARD", "playmode" to "RANKED",
            "status" to "PENDING", "timestamp" to System.currentTimeMillis(),
            "wordsUsed" to emptyList<String>()
        )), 15, TimeUnit.SECONDS)
        var server = Tasks.await(db.collection("submissions").document(id).get(Source.SERVER), 15, TimeUnit.SECONDS)
        val deadline = System.currentTimeMillis() + 30000
        while (server.getString("matchStatus") != "MATCHED" && System.currentTimeMillis() < deadline) {
            Thread.sleep(200)
            server = Tasks.await(db.collection("submissions").document(id).get(Source.SERVER), 15, TimeUnit.SECONDS)
        }
        assertEquals("MATCHED", server.getString("matchStatus"))
        assertEquals(1L, server.getLong("matchResult.ratingChange"))
        File(ui.activity.filesDir, "integrated-later-match.json").writeText(JSONObject(mapOf(
            "submissionId" to id, "opponentSubmissionId" to opponentSubmissionId,
            "serverMatchStatus" to server.getString("matchStatus"),
            "serverRatingChange" to server.getLong("matchResult.ratingChange"),
            "viewModelMatchStatus" to vm.latestSubmission!!.matchStatus,
            "viewModelRatingChange" to vm.latestSubmission!!.evaluation!!.ratingChange,
            "delivery" to "automatic local Firestore event", "r8" to "HTTP double"
        )).toString(2))
        // Keep strict acceptance: a later match cannot leave Results stale forever.
        ui.waitUntil(8000) { vm.latestSubmission?.matchStatus == "MATCHED" }
        assertEquals(id, vm.latestSubmission!!.id)
        ui.onNodeWithText("+1").assertExists()
    }
}
