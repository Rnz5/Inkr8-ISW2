package com.inkr8.lab

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.inkr8.data.*
import com.inkr8.screens.Writing
import com.inkr8.utils.DraftManager
import org.json.JSONObject
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import java.io.File
import java.util.concurrent.TimeUnit

// Actual Compose/editor/preferences and SubmissionFactory. Confirmation is an
// explicit callback double, no Firestore success/rejection is claimed here.
@RunWith(AndroidJUnit4::class)
class LabDraftConcurrencyTest {
    @get:Rule val ui=createAndroidComposeRule<LabHostActivity>()
    private lateinit var key: String
    private val text="Bright rivers carry ancient stories while curious writers explore the quiet valley. Every morning a traveler observes changing shadows beside patient trees and remembers distant friends. Gentle rain reveals new colors across the landscape, inspiring thoughtful descriptions of journeys, discoveries, and ordinary moments that deserve careful attention from everyone around us."
    @Before fun setup() {
        FirebaseAuth.getInstance().signOut()
        Tasks.await(FirebaseAuth.getInstance().signInAnonymously(),15,TimeUnit.SECONDS)
        key = LabDraftFixtures.seed(ui.activity,text)
    }
    @Test fun laterUnsavedRevisionIsLostOnEarlierConfirmation_probe() {
        var confirm:(()->Unit)?=null
        ui.activityRule.scenario.onActivity{a->a.show{Writing(StandardWriting,PlayMode.Practice,
            onAddSubmission={_,saved,_->confirm=saved},onNavigateBack={},onNavigateToResults={})}}
        ui.onNode(hasClickAction() and hasText("Submit")).performClick()
        val edited="$text New observations remain unsaved."
        ui.onNode(hasSetTextAction()).performTextReplacement(edited)
        ui.waitUntil(6000){DraftManager.getDraft(ui.activity,key)==edited}
        ui.runOnIdle{confirm!!()}
        File(ui.activity.filesDir,"draft-new-revision.json").writeText(JSONObject(mapOf(
            "submittedText" to text,"newRevision" to edited,"draftAfterEarlierSuccess" to DraftManager.getDraft(ui.activity,key),
            "confirmation" to "explicit callback double, no server write"
        )).toString(2))
        // Original red assertion retained under DEC-DRAFT-REV-001.
        assertEquals("An earlier confirmation must not discard the newer unsaved revision",edited,DraftManager.getDraft(ui.activity,key))
        ui.onNode(hasSetTextAction()).assertTextContains(edited)
    }
    @Ignore("Historical characterization superseded by DEC-DRAFT-REV-001; assertions preserved in source/history")
    @Test fun secondClickProducesTwoDistinctSubmissionIntents_characterization() {
        val sent=mutableListOf<Submissions>()
        ui.activityRule.scenario.onActivity{a->a.show{Writing(StandardWriting,PlayMode.Practice,
            onAddSubmission={s,_,_->sent+=s},onNavigateBack={},onNavigateToResults={})}}
        repeat(2){ui.onNode(hasClickAction() and hasText("Submit")).performClick()}
        assertEquals(2,sent.size);assertNotEquals(sent[0].id,sent[1].id)
        assertEquals(text,sent[0].content);assertEquals(text,sent[1].content)
        assertEquals(text,DraftManager.getDraft(ui.activity,key))
        File(ui.activity.filesDir,"draft-second-intent.json").writeText(JSONObject(mapOf(
            "ids" to sent.map{it.id},"count" to sent.size,"persisted" to false,
            "scope" to "actual Writing/factory, pending callback fixture; no duplicate server reward claimed"
        )).toString(2))
    }
}
