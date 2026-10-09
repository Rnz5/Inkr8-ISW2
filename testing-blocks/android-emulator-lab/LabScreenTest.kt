package com.inkr8.lab

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.text.AnnotatedString
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.tasks.Tasks
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.inkr8.data.*
import com.inkr8.repository.FirestoreSubmissionRepository
import com.inkr8.screens.*
import com.inkr8.utils.DraftManager
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

// Instrumentation must execute on a booted device. Compiling it is not a test pass.
@RunWith(AndroidJUnit4::class)
class LabScreenTest {
    @get:Rule val ui = createAndroidComposeRule<LabHostActivity>()
    private lateinit var key: String
    private val paragraph = "Bright rivers carry ancient stories while curious writers explore the quiet valley. Every morning a traveler observes changing shadows beside patient trees and remembers distant friends. Gentle rain reveals new colors across the landscape, inspiring thoughtful descriptions of journeys, discoveries, and ordinary moments that deserve careful attention from everyone around us."

    @Before fun localOnly() {
        assertEquals("demo-inkr8-local", FirebaseApp.getInstance().options.projectId)
        assertEquals("com.inkr8.lab", ui.activity.packageName)
        FirebaseAuth.getInstance().signOut()
        Tasks.await(FirebaseAuth.getInstance().signInAnonymously(), 15, TimeUnit.SECONDS)
        // New local words only; existing documents and historical data are retained.
        val db = FirebaseFirestore.getInstance()
        val assigned = mutableListOf<Words>()
        for ((index, word) in listOf("bright", "rivers", "ancient", "stories").withIndex()) {
            val id = "lab-" + UUID.randomUUID()
            assigned += Words(id=id,word=word,randomIndex=index/4.0)
            Tasks.await(db.collection("words").document(id).set(
                mapOf("word" to word, "isActive" to true, "randomIndex" to index / 4.0)), 15, TimeUnit.SECONDS)
        }
        key = LabDraftFixtures.seed(ui.activity, "", words=assigned)
    }
    private fun writing(callback: (Submissions, () -> Unit, () -> Unit) -> Unit = { _, _, _ -> }) {
        ui.activityRule.scenario.onActivity { activity ->
            activity.show { Writing(StandardWriting, PlayMode.Practice,
                onAddSubmission=callback, onNavigateBack={}, onNavigateToResults={}) }
        }
    }
    private fun submit() = ui.onNode(hasClickAction() and (hasText("Submit") or hasText("Incomplete")))

    @Test fun editorUsesInclusiveLimitsAndWhitespaceCount() {
        DraftManager.saveDraft(ui.activity, key, "")
        writing()
        submit().assertIsNotEnabled()
        for (count in listOf(49, 50, 150, 151)) {
            ui.onNode(hasSetTextAction()).performTextReplacement((1..count).joinToString("\n") { "word$it" })
            ui.onNodeWithText("Words: $count").assertExists()
            if (count in 50..150) submit().assertIsEnabled() else submit().assertIsNotEnabled()
        }
    }

    @Test fun draftRestoresAndAutosavesActualSharedPreferences() {
        DraftManager.saveDraft(ui.activity, key, paragraph)
        writing()
        ui.onNode(hasSetTextAction()).assertTextContains(paragraph)
        val edited = "$paragraph Additional observations follow."
        ui.onNode(hasSetTextAction()).performTextReplacement(edited)
        ui.waitUntil(7000) { DraftManager.getDraft(ui.activity, key) == edited }
        assertEquals(edited, DraftManager.getDraft(ui.activity, key))
    }

    @Test fun recognitionKeepsCaseAndPunctuationTokenBoundaries() {
        val text=paragraph.replace("Bright rivers", "BRIGHT, rivers!")
        DraftManager.saveDraft(ui.activity,key,text)
        val captured=AtomicReference<Submissions?>()
        writing { submission, _, _ -> captured.set(submission) }
        ui.waitUntil(15000) { ui.onAllNodesWithText("Required Words").fetchSemanticsNodes().isNotEmpty() }
        submit().performClick()
        assertEquals(51,captured.get()!!.wordCount)
        assertEquals(4,captured.get()!!.wordsUsed.size)
        assertTrue(captured.get()!!.wordsUsed.all { it.word in listOf("bright","rivers","ancient","stories") })
    }

    @Test fun omittedWordsKeepCurrentAdmissionAndEmptySubmissionFilter() {
        val text=paragraph.replace("Bright","Brightly").replace("rivers","riverside")
            .replace("ancient","ancients").replace("stories","storybooks")
        DraftManager.saveDraft(ui.activity,key,text)
        val captured=AtomicReference<Submissions?>()
        writing { submission, _, _ -> captured.set(submission) }
        ui.waitUntil(15000) { ui.onAllNodesWithText("Required Words").fetchSemanticsNodes().isNotEmpty() }
        submit().assertIsEnabled().performClick()
        assertTrue(captured.get()!!.wordsUsed.isEmpty()) // Existing rule; no new requirement imposed.
    }

    @Test fun persistenceSuccessUsesActualRepository() {
        DraftManager.saveDraft(ui.activity, key, paragraph)
        val saved = CountDownLatch(1)
        val error = AtomicReference<Exception?>()
        writing { submission, onPersisted, onFailure ->
            FirestoreSubmissionRepository().addSubmission(
                submission.copy(authorId=FirebaseAuth.getInstance().currentUser!!.uid),
                onSuccess={onPersisted(); saved.countDown()}, onError={onFailure();error.set(it); saved.countDown()})
        }
        submit().performClick()
        assertTrue(saved.await(15, TimeUnit.SECONDS))
        assertNull(error.get())
        ui.onNode(hasSetTextAction()).assert(SemanticsMatcher.expectValue(SemanticsProperties.EditableText,AnnotatedString("")))
        assertEquals("", DraftManager.getDraft(ui.activity, key))
    }

    @org.junit.Ignore("Historical pre-A05 characterization; strict rejection acceptance remains LabCoreAcceptanceTest/LabRootFlowTest")
    @Test fun persistenceRejectionCharacterizesUnresolvedDraftLoss() {
        DraftManager.saveDraft(ui.activity, key, paragraph)
        val completed = CountDownLatch(1)
        val error = AtomicReference<Exception?>()
        writing { submission, onPersisted, onFailure ->
            // Local rules deliberately reject this prefix. This is not a product rule.
            FirestoreSubmissionRepository().addSubmission(submission.copy(
                id="lab-denied-" + UUID.randomUUID(), authorId=FirebaseAuth.getInstance().currentUser!!.uid),
                onSuccess={onPersisted(); completed.countDown()}, onError={onFailure();error.set(it); completed.countDown()})
        }
        submit().performClick()
        assertTrue(completed.await(15, TimeUnit.SECONDS))
        assertNotNull(error.get())
        // Characterizes the gap; these assertions do NOT satisfy the human requirement.
        ui.onNode(hasSetTextAction()).assert(SemanticsMatcher.expectValue(SemanticsProperties.EditableText,AnnotatedString("")))
        assertEquals("", DraftManager.getDraft(ui.activity, key))
    }

    @Test fun resultsShowScoreFeedbackMeritRatingAndCallback() {
        var back = false
        val submission = Submissions(id="lab-result", playmode="RANKED", status=SubmissionStatus.EVALUATED,
            matchStatus="MATCHED", evaluation=Evaluation(finalScore=82.47, feedback="Laboratory feedback",
                meritEarned=57, ratingChange=5, resultStatus=SubmissionStatus.EVALUATED))
        ui.activityRule.scenario.onActivity { it.show { Results(submission, true,
            onNavigateBack={back=true}, onNavigateToPractice={}) } }
        ui.onNodeWithText("82.47", substring=true).assertExists()
        ui.onNodeWithText("Laboratory feedback", substring=true).assertExists()
        ui.onNodeWithText("Merit Gain").assertExists()
        ui.onNodeWithText("Rating").assertExists()
        ui.onNodeWithText("Back").performScrollTo().performClick()
        assertTrue(back)
    }

    @Test fun placementRetainsContinueAndOmitsLeaguePresentation() {
        var continued = false
        ui.activityRule.scenario.onActivity { it.show { PlacementRevealScreen(120L) { continued=true } } }
        ui.onNodeWithText("Calibration Complete").assertExists()
        ui.waitUntil(6000) { ui.onAllNodes(hasClickAction()).fetchSemanticsNodes().isNotEmpty() }
        ui.onNode(hasClickAction()).performClick()
        assertTrue(continued)
    }
}
