package com.inkr8.lab

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.ViewModelProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.inkr8.AppRoot
import com.inkr8.AppViewModelFactory
import com.inkr8.data.*
import com.inkr8.screens.Writing
import com.inkr8.utils.DraftManager
import com.inkr8.viewmodel.AppViewModel
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

// Real Compose/preferences/auth. Callback delivery is explicit where stated.
// Root tests use actual repository/Firestore writes with evaluator disabled.
@RunWith(AndroidJUnit4::class)
class LabDraftContractTest {
    @get:Rule val ui=createAndroidComposeRule<LabHostActivity>()
    private val db get()=FirebaseFirestore.getInstance()
    private val text="Bright rivers carry ancient stories while curious writers explore the quiet valley. Every morning a traveler observes changing shadows beside patient trees and remembers distant friends. Gentle rain reveals new colors across the landscape, inspiring thoughtful descriptions of journeys, discoveries, and ordinary moments that deserve careful attention from everyone around us."
    private lateinit var uid:String
    private lateinit var key:String
    private val sent=mutableListOf<Submissions>()
    private val success=mutableListOf<()->Unit>()
    private val failure=mutableListOf<()->Unit>()
    @Before fun setup() {
        Tasks.await(db.enableNetwork(),15,TimeUnit.SECONDS)
        FirebaseAuth.getInstance().signOut()
        uid=Tasks.await(FirebaseAuth.getInstance().signInAnonymously(),15,TimeUnit.SECONDS).user!!.uid
        key=LabDraftFixtures.seed(ui.activity,text)
    }
    @After fun cleanup() {
        Tasks.await(db.enableNetwork(),15,TimeUnit.SECONDS)
        Tasks.await(db.waitForPendingWrites(),15,TimeUnit.SECONDS)
        ui.runOnIdle {ui.activity.viewModelStore.clear()}
    }
    private fun show()=ui.activityRule.scenario.onActivity {a->a.show {
        Writing(StandardWriting,PlayMode.Practice,onAddSubmission={s,ok,bad->
            sent+=s;success+=ok;failure+=bad},onNavigateBack={},onNavigateToResults={})
    }}
    private fun button()=ui.onNode(hasClickAction() and hasText("Submit"))
    private fun editor()=ui.onNode(hasSetTextAction())
    private fun draft()=DraftManager.getDraft(ui.activity,key)
    private fun assertEmpty()=editor().assert(SemanticsMatcher.expectValue(SemanticsProperties.EditableText,AnnotatedString("")))

    @Test fun secondSendBlockedUntilFailureThenNewUuidAllowed() {
        show();button().performClick();button().assertIsNotEnabled()
        button().performClick();assertEquals(1,sent.size)
        assertEquals(text,draft());editor().assertTextContains(text)
        ui.runOnIdle {failure[0]()};button().assertIsEnabled().performClick()
        assertEquals(2,sent.size);assertNotEquals(sent[0].id,sent[1].id)
        ui.runOnIdle {success[0]();failure[0]()}
        assertEquals(text,draft());button().assertIsNotEnabled()
        ui.runOnIdle {failure[1]()};button().assertIsEnabled()
    }
    @Test fun aToBToAPreservesRevisionDespiteEqualText() {
        show();button().performClick()
        editor().performTextReplacement("$text Revision B.")
        editor().performTextReplacement(text)
        ui.runOnIdle {success[0]()}
        assertEquals(text,draft());editor().assertTextContains(text);button().assertIsEnabled()
    }
    @Test fun repeatedConfirmationCannotClearPostSuccessEdit() {
        show();button().performClick();ui.runOnIdle {success[0]()}
        assertEquals("",draft());assertEmpty()
        val next="$text New revision after success."
        editor().performTextReplacement(next)
        ui.waitUntil(7000){draft()==next}
        ui.runOnIdle {success[0]();failure[0]()}
        assertEquals(next,draft());editor().assertTextContains(next);button().assertIsEnabled()
    }
    @Test fun failedAndRepeatedCallbacksPreserveEditedRevision() {
        show();button().performClick();val next="$text Revision B."
        editor().performTextReplacement(next)
        ui.runOnIdle {failure[0]();failure[0]();success[0]()}
        assertEquals(next,draft());editor().assertTextContains(next);button().assertIsEnabled()
    }
    @Test fun emptyNewRevisionIsAlsoProtected() {
        show();button().performClick();editor().performTextReplacement("")
        ui.runOnIdle {success[0]()}
        assertEquals("",draft());assertEmpty()
        assertNotNull(DraftManager.getExerciseWords(ui.activity,LabDraftFixtures.exercise()))
    }
    @Test fun legacyDraftNotClaimedAndNoAnonymousScope() {
        val legacy=DraftManager.getDraftKey("STANDARD","PRACTICE",null)
        DraftManager.saveDraft(ui.activity,legacy,"unverifiable historical owner")
        LabDraftFixtures.seed(ui.activity,"")
        show();assertEmpty()
        assertEquals("unverifiable historical owner",DraftManager.getDraft(ui.activity,legacy))
        assertNull(DraftManager.getExerciseKey("","STANDARD","PRACTICE",null))
    }
    @Test fun accountAndExerciseSwitchRestoreOnlyTheirOwnDraft() {
        val auth=FirebaseAuth.getInstance()
        val tag=java.util.UUID.randomUUID().toString()
        val emailA="draft-a-$tag@example.test";val emailB="draft-b-$tag@example.test"
        val password="local-fixture-only-123"
        auth.signOut()
        val a=Tasks.await(auth.createUserWithEmailAndPassword(emailA,password),15,TimeUnit.SECONDS).user!!.uid
        auth.signOut()
        val second=Tasks.await(auth.createUserWithEmailAndPassword(emailB,password),15,TimeUnit.SECONDS).user!!.uid
        auth.signOut();Tasks.await(auth.signInWithEmailAndPassword(emailA,password),15,TimeUnit.SECONDS)
        assertNotEquals(a,second)
        val topic1=OnTopicWriting(Theme(id="scope-theme",name="Nature"),Topic(id="scope-topic-1",name="Rivers"))
        val topic2=OnTopicWriting(Theme(id="scope-theme",name="Nature"),Topic(id="scope-topic-2",name="Trees"))
        val firstKey=LabDraftFixtures.seed(ui.activity,"first topic draft",a,topic1)
        val secondKey=LabDraftFixtures.seed(ui.activity,"second account draft",second,topic1)
        val otherKey=LabDraftFixtures.seed(ui.activity,"another exercise draft",a,topic2)
        var owner by mutableStateOf(a);var mode by mutableStateOf<Gamemode>(topic1)
        var unrelated by mutableIntStateOf(0)
        ui.activityRule.scenario.onActivity {it.show {
            unrelated // Force recomposition without changing exercise identity.
            Writing(mode,PlayMode.Practice,userId=owner,onAddSubmission={_,_,_->},onNavigateBack={},onNavigateToResults={})
        }}
        editor().assertTextContains("first topic draft")
        editor().performTextReplacement("first topic revised")
        auth.signOut();Tasks.await(auth.signInWithEmailAndPassword(emailB,password),15,TimeUnit.SECONDS)
        ui.runOnIdle {unrelated++;owner=second}
        assertEquals(second,auth.currentUser!!.uid)
        editor().assertTextContains("second account draft")
        assertEquals("first topic revised",DraftManager.getDraft(ui.activity,firstKey))
        auth.signOut();Tasks.await(auth.signInWithEmailAndPassword(emailA,password),15,TimeUnit.SECONDS)
        ui.runOnIdle {owner=a;mode=topic2}
        assertEquals(a,auth.currentUser!!.uid)
        editor().assertTextContains("another exercise draft")
        ui.runOnIdle {mode=topic1}
        editor().assertTextContains("first topic revised")
        assertEquals("second account draft",DraftManager.getDraft(ui.activity,secondKey))
        assertEquals("another exercise draft",DraftManager.getDraft(ui.activity,otherKey))
    }
    @Test fun assignedWordIdentityAndMetadataSurviveReentry() {
        val words=listOf(Words(id="word-existing-1",word="bright",definition="Existing definition",randomIndex=0.25),
            Words(id="word-existing-2",word="rivers",sentence="Existing sentence"))
        val scope=LabDraftFixtures.exercise()
        key=LabDraftFixtures.seed(ui.activity,text,words=words)
        assertEquals(words,DraftManager.getExerciseWords(ui.activity,scope))
        val reordered=DraftManager.getScopedDraftKey(scope,words.reversed())
        assertNotEquals(key,reordered)
        var visible by mutableStateOf(true)
        ui.activityRule.scenario.onActivity {it.show {
            if(visible) Writing(StandardWriting,PlayMode.Practice,onAddSubmission={_,_,_->},onNavigateBack={},onNavigateToResults={})
        }}
        editor().assertTextContains(text)
        val next="$text Stable assignment."
        editor().performTextReplacement(next)
        ui.runOnIdle {visible=false};ui.waitForIdle()
        ui.runOnIdle {visible=true}
        editor().assertTextContains(next)
        assertEquals(words,DraftManager.getExerciseWords(ui.activity,scope))
        assertEquals(key,DraftManager.getScopedDraftKey(scope,words))
    }
    @Test fun remountedEditorCannotBeClearedByOldPendingConfirmation() {
        var visible by mutableStateOf(true)
        ui.activityRule.scenario.onActivity {it.show {
            if(visible) Writing(StandardWriting,PlayMode.Practice,isPersisting=sent.isNotEmpty(),
                onAddSubmission={s,ok,bad->sent+=s;success+=ok;failure+=bad},onNavigateBack={},onNavigateToResults={})
        }}
        button().performClick();ui.runOnIdle {visible=false};ui.waitForIdle()
        ui.runOnIdle {visible=true}
        val next="$text After reentry."
        editor().performTextReplacement(next)
        ui.runOnIdle {success[0]()}
        assertEquals(next,draft());editor().assertTextContains(next)
    }
    @Test fun actualPendingWriteAllowsEditBlocksSecondAndKeepsBOnSuccess() {
        val user=Users(id=uid,isPlaced=true,hasSeenPlacementReveal=true)
        Tasks.await(db.collection("users").document(uid).set(user),15,TimeUnit.SECONDS)
        lateinit var vm:AppViewModel
        ui.runOnIdle {vm=ViewModelProvider(ui.activity,AppViewModelFactory(user))[uid,AppViewModel::class.java];vm.startWriting(StandardWriting,PlayMode.Practice)}
        ui.activityRule.scenario.onActivity {it.show {
            val launcher=rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()){}
            AppRoot(user,launcher,{})
        }}
        Tasks.await(db.disableNetwork(),15,TimeUnit.SECONDS)
        button().performClick();button().assertIsNotEnabled()
        assertTrue(vm.isPersistingSubmission)
        val next="$text Actual later revision."
        editor().performTextReplacement(next)
        ui.runOnIdle {vm.submitWriting(Submissions(id="must-not-be-written")){}}
        assertEquals(1,Tasks.await(db.collection("submissions").whereEqualTo("authorId",uid).get(Source.CACHE),15,TimeUnit.SECONDS).size())
        assertEquals(next,draft())
        Tasks.await(db.enableNetwork(),15,TimeUnit.SECONDS)
        ui.waitUntil(15000){vm.currentScreen==Screen.loading && !vm.isPersistingSubmission}
        assertEquals(1,Tasks.await(db.collection("submissions").whereEqualTo("authorId",uid).get(Source.SERVER),15,TimeUnit.SECONDS).size())
        assertEquals(next,draft())
        ui.runOnIdle {vm.startWriting(StandardWriting,PlayMode.Practice)}
        editor().assertTextContains(next);button().assertIsEnabled()
    }

    @Test fun uncachedAssignmentFromRealRepositoryIsStableOnReentry() {
        val scope=LabDraftFixtures.exercise()
        DraftManager.releaseExercise(ui.activity,scope)
        var visible by mutableStateOf(true)
        ui.activityRule.scenario.onActivity {it.show {
            if(visible) Writing(StandardWriting,PlayMode.Practice,onAddSubmission={_,_,_->},onNavigateBack={},onNavigateToResults={})
        }}
        ui.waitUntil(15000){DraftManager.getExerciseWords(ui.activity,scope)!=null}
        val assigned=DraftManager.getExerciseWords(ui.activity,scope)!!
        assertEquals(4,assigned.size);assertTrue(assigned.all{it.id.isNotBlank()})
        key=DraftManager.getScopedDraftKey(scope,assigned)
        editor().performTextReplacement(text)
        ui.runOnIdle {visible=false};ui.waitForIdle()
        ui.runOnIdle {visible=true}
        editor().assertTextContains(text)
        assertEquals(assigned,DraftManager.getExerciseWords(ui.activity,scope))
        assertEquals(text,draft())
    }

    @Test fun unchangedRemountedEditorClearsOnOriginalConfirmation() {
        var visible by mutableStateOf(true)
        ui.activityRule.scenario.onActivity {it.show {
            if(visible) Writing(StandardWriting,PlayMode.Practice,isPersisting=sent.isNotEmpty(),
                onAddSubmission={s,ok,bad->sent+=s;success+=ok;failure+=bad},onNavigateBack={},onNavigateToResults={})
        }}
        button().performClick();ui.runOnIdle {visible=false};ui.waitForIdle()
        ui.runOnIdle {visible=true};editor().assertTextContains(text)
        ui.runOnIdle {success[0]()}
        assertEquals("",draft());assertEmpty()
    }
    @Test fun realPendingReentryMustNotResurrectConfirmedUnchangedDraft() {
        val user=Users(id=uid,isPlaced=true,hasSeenPlacementReveal=true)
        Tasks.await(db.collection("users").document(uid).set(user),15,TimeUnit.SECONDS)
        lateinit var vm:AppViewModel
        ui.runOnIdle {vm=ViewModelProvider(ui.activity,AppViewModelFactory(user))[uid,AppViewModel::class.java];vm.startWriting(StandardWriting,PlayMode.Practice)}
        ui.activityRule.scenario.onActivity {it.show {
            val launcher=rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()){}
            AppRoot(user,launcher,{})
        }}
        Tasks.await(db.disableNetwork(),15,TimeUnit.SECONDS)
        button().performClick()
        ui.runOnIdle {vm.navigateTo(Screen.home)};ui.waitForIdle()
        ui.runOnIdle {vm.startWriting(StandardWriting,PlayMode.Practice)}
        editor().assertTextContains(text);button().assertIsNotEnabled();assertTrue(vm.isPersistingSubmission)
        Tasks.await(db.enableNetwork(),15,TimeUnit.SECONDS)
        ui.waitUntil(15000){!vm.isPersistingSubmission && vm.currentScreen!=Screen.writing}
        assertEquals("A remounted editor must not resurrect the already confirmed unchanged draft","",draft())
        assertEquals(1,Tasks.await(db.collection("submissions").whereEqualTo("authorId",uid).get(Source.SERVER),15,TimeUnit.SECONDS).size())
        ui.runOnIdle {vm.startWriting(StandardWriting,PlayMode.Practice)}
        ui.waitUntil(15000){ui.onAllNodes(hasSetTextAction()).fetchSemanticsNodes().size==1}
        assertEmpty()
    }

    @Test fun metadataRefreshAndRecompositionKeepExistingExerciseRevision() {
        val initial=OnTopicWriting(Theme(id="stable-theme",name="Nature"),Topic(id="stable-topic",name="Rivers"))
        key=LabDraftFixtures.seed(ui.activity,text,mode=initial)
        var mode by mutableStateOf(initial)
        ui.activityRule.scenario.onActivity {it.show {
            Writing(mode,PlayMode.Practice,onAddSubmission={_,_,_->},onNavigateBack={},onNavigateToResults={})
        }}
        val next="$text Revision before metadata refresh."
        editor().performTextReplacement(next)
        ui.runOnIdle {mode=initial.copy(theme=initial.theme.copy(name="Updated theme"),topic=initial.topic.copy(name="Updated topic"))}
        editor().assertTextContains(next)
        ui.onNodeWithText("Updated theme",substring=true).assertExists()
        ui.waitUntil(7000){draft()==next}
        assertEquals(next,draft())
        assertEquals(LabDraftFixtures.exercise(mode=initial),LabDraftFixtures.exercise(mode=mode))
    }
}
