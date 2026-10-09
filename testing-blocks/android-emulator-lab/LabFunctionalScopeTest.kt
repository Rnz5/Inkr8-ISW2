package com.inkr8.lab

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.test.*
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.ViewModelProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.tasks.Tasks
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.inkr8.AppRoot
import com.inkr8.AppViewModelFactory
import com.inkr8.data.*
import com.inkr8.screens.*
import com.inkr8.viewmodel.AppViewModel
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import java.util.UUID
import java.util.concurrent.TimeUnit

/** Real Compose/Auth/Firestore/Functions SDK. Historical rows are explicit local fixtures. */
@RunWith(AndroidJUnit4::class)
class LabFunctionalScopeTest {
    @get:Rule val ui = createAndroidComposeRule<LabHostActivity>()
    private val db get() = FirebaseFirestore.getInstance()
    private lateinit var user: Users
    private lateinit var vm: AppViewModel
    @Before fun localOnly() {
        assertEquals("demo-inkr8-local", FirebaseApp.getInstance().options.projectId)
        assertEquals("com.inkr8.lab", ui.activity.packageName)
        FirebaseAuth.getInstance().signOut()
        val uid = Tasks.await(FirebaseAuth.getInstance().signInAnonymously(),15,TimeUnit.SECONDS).user!!.uid
        user = Users(id=uid,name="Functional scope fixture",rating=500,merit=2345,reputation=900,isPlaced=true,hasSeenPlacementReveal=true)
        Tasks.await(db.collection("users").document(uid).set(user),15,TimeUnit.SECONDS)
        ui.runOnIdle { vm=ViewModelProvider(ui.activity,AppViewModelFactory(user))[uid,AppViewModel::class.java];vm.navigateTo(Screen.profile) }
    }
    @After fun release() { ui.runOnIdle { ui.activity.viewModelStore.clear() } }
    private fun showRoot() { ui.activityRule.scenario.onActivity { it.show {
        val launcher=rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()){}
        AppRoot(user,launcher,{})
    } } }
    @Test fun ownerProfileRetainsMeritAndHasNoRetiredPresentation() {
        showRoot()
        ui.onNodeWithText(user.name).assertExists()
        assertTrue(ui.onAllNodes(hasText("Merit",substring=true)).fetchSemanticsNodes().isNotEmpty())
        for (text in listOf("Reputation","League","Tournament","Pantheon")) ui.onAllNodes(hasText(text,substring=true)).assertCountEquals(0)
        ui.onNodeWithText("Temporadas").performScrollTo().performClick()
        ui.waitUntil(15000) {ui.onAllNodes(hasText("Temporada actual:",substring=true)).fetchSemanticsNodes().isNotEmpty()}
        assertEquals(Screen.seasons,vm.currentScreen)
        ui.onNodeWithText("Volver").performClick();assertEquals(Screen.profile,vm.currentScreen)
    }
    @Test fun authenticatedSeasonCallableShowsOwnRankAndSelectableHistory() {
        val result=Tasks.await(FirebaseFunctions.getInstance().getHttpsCallable("getSeasonRanking").call(),15,TimeUnit.SECONDS).data as Map<*,*>
        val id=result["id"] as String
        // Add a unique tied rating pool. No original or existing documents are deleted.
        val members=db.collection("seasons").document(id).collection("members")
        Tasks.await(members.document(user.id).set(mapOf("name" to user.name,"userId" to user.id,"rating" to 987654L,"meritEarned" to 457L)),15,TimeUnit.SECONDS)
        val tie="lab-tie-"+UUID.randomUUID()
        Tasks.await(members.document(tie).set(mapOf("name" to "Same rating fixture","userId" to tie,"rating" to 987654L,"meritEarned" to 50L)),15,TimeUnit.SECONDS)
        val historic="1998-03"
        if (!Tasks.await(db.collection("seasons").document(historic).get(),15,TimeUnit.SECONDS).exists())
            Tasks.await(db.collection("seasons").document(historic).set(mapOf("start" to 888796800000L,"end" to 891475200000L,"status" to "CLOSED","pendingCount" to 0L)),15,TimeUnit.SECONDS)
        Tasks.await(db.collection("seasons").document(historic).collection("members").document(user.id).set(mapOf("name" to user.name,"rating" to 14L,"meritEarned" to 457L)),15,TimeUnit.SECONDS)
        ui.runOnIdle {vm.navigateTo(Screen.seasons)};showRoot()
        ui.waitUntil(15000) {ui.onAllNodes(hasText("Temporada actual: $id")).fetchSemanticsNodes().isNotEmpty()}
        // The real ranking can exceed the viewport after previous additive fixtures.
        ui.onNode(hasScrollToNodeAction()).performScrollToNode(hasText("1. ${user.name} (Tú) — Rating: 987654"))
        ui.onNodeWithText("1. ${user.name} (Tú) — Rating: 987654").assertExists()
        ui.onNode(hasScrollToNodeAction()).performScrollToNode(hasText(historic))
        ui.onNodeWithText(historic).performClick()
        ui.onNode(hasScrollToNodeAction()).performScrollToNode(hasText("Posición final: 1"))
        ui.onNodeWithText("Posición final: 1").assertExists()
        ui.onNode(hasScrollToNodeAction()).performScrollToNode(hasText("Rating de cierre: 14"))
        ui.onNodeWithText("Rating de cierre: 14").assertExists()
        ui.onNode(hasScrollToNodeAction()).performScrollToNode(hasText("Merit ganado: 457"))
        ui.onNodeWithText("Merit ganado: 457").assertExists()
        val persisted=Tasks.await(db.collection("users").document(user.id).get(),15,TimeUnit.SECONDS)
        assertEquals(500L,persisted.getLong("rating"));assertEquals(2345L,persisted.getLong("merit"))
    }
    @Test fun rankedPresentationRetainsPaidEntryWithoutTournamentFeed() {
        ui.activityRule.scenario.onActivity {it.show {Competitions(user=user,onNavigateBack={},onNavigateToWriting={},onNavigateToProfile={})}}
        ui.onNodeWithText("Ranked Arena").assertExists()
        // Existing price: 100 * historical reputation factor .80, no new charge formula.
        ui.onNodeWithText("Enter • 80").assertExists()
        for (text in listOf("Tournament","League","Reputation","Pantheon")) ui.onAllNodes(hasText(text,substring=true)).assertCountEquals(0)
    }
    @Test fun rankedOnTopicFailureKeepsChoiceAndDoesNotCharge() {
        var chosen: Gamemode? = null
        Tasks.await(db.collection("labDeniedExercise").document(user.id).set(mapOf("denied" to true)),15,TimeUnit.SECONDS)
        ui.activityRule.scenario.onActivity {it.show {Competitions(user=user,onNavigateBack={},onNavigateToWriting={chosen=it},onNavigateToProfile={})}}
        val radios=ui.onAllNodes(SemanticsMatcher.keyIsDefined(SemanticsProperties.Selected))
        radios.assertCountEquals(2);radios.onLast().performClick();radios.onLast().assertIsSelected()
        ui.onNodeWithText("Enter • 80").performClick()
        ui.waitUntil(15000) {ui.onAllNodes(hasText("No se pudo iniciar la partida On-Topic. Inténtalo nuevamente")).fetchSemanticsNodes().isNotEmpty()}
        ui.onNodeWithText("No se pudo iniciar la partida On-Topic. Inténtalo nuevamente").assertExists()
        radios.onLast().assertIsSelected();ui.onNodeWithText("Enter • 80").assertIsEnabled();assertNull(chosen)
        val data=Tasks.await(db.collection("users").document(user.id).get(),15,TimeUnit.SECONDS)
        assertEquals(2345L,data.getLong("merit"));assertFalse(data.getBoolean("currentlyInRanked")!!)
        assertTrue(Tasks.await(db.collection("users").document(user.id).collection("meritTransactions").get(),15,TimeUnit.SECONDS).isEmpty)
        // Reset this owned rejection marker by update, no data deletion.
        Tasks.await(db.collection("labDeniedExercise").document(user.id).update("denied",false),15,TimeUnit.SECONDS)
    }
    @Test fun rankedOnTopicSuccessUsesExistingThemeTopicAndPaidEntry() {
        val themeId="lab-selector-theme-"+UUID.randomUUID()
        Tasks.await(db.collection("themes").document(themeId).set(Theme(id=themeId,name="Nature",randomIndex=.5)),15,TimeUnit.SECONDS)
        val themes=Tasks.await(db.collection("themes").get(),15,TimeUnit.SECONDS)
        for (theme in themes.documents) {
            val topicId="lab-selector-topic-"+UUID.randomUUID()
            Tasks.await(db.collection("topics").document(topicId).set(mapOf("themeId" to theme.id,"name" to "Rivers","randomIndex" to .5)),15,TimeUnit.SECONDS)
        }
        var chosen: Gamemode? = null
        ui.activityRule.scenario.onActivity {it.show {Competitions(user=user,onNavigateBack={},onNavigateToWriting={chosen=it},onNavigateToProfile={})}}
        ui.onAllNodes(SemanticsMatcher.keyIsDefined(SemanticsProperties.Selected)).onLast().performClick()
        ui.onNodeWithText("Enter • 80").performClick()
        ui.waitUntil(15000) {chosen!=null}
        assertTrue(chosen is OnTopicWriting)
        val mode=chosen as OnTopicWriting
        assertTrue(mode.theme.id.isNotEmpty());assertEquals(mode.theme.id,mode.topic.themeId)
        val stored=Tasks.await(db.collection("users").document(user.id).get(),15,TimeUnit.SECONDS)
        assertEquals(2265L,stored.getLong("merit"));assertEquals(900L,stored.getLong("reputation"));assertTrue(stored.getBoolean("currentlyInRanked")!!)
        val transactions=Tasks.await(db.collection("users").document(user.id).collection("meritTransactions").get(),15,TimeUnit.SECONDS)
        assertEquals(1,transactions.size());assertEquals(-80L,transactions.documents.single().getLong("amount"))
    }
    @Test fun explicitStandardEntryUsesRealCallableThenWritingAndResults() {
        val text="Bright rivers carry ancient stories while curious writers explore the quiet valley. Every morning a traveler observes changing shadows beside patient trees and remembers distant friends. Gentle rain reveals new colors across the landscape, inspiring thoughtful descriptions of journeys, discoveries, and ordinary moments that deserve careful attention from everyone around us."
        com.inkr8.lab.LabDraftFixtures.seed(ui.activity,text,play=PlayMode.Ranked)
        ui.runOnIdle {vm.navigateTo(Screen.home,page=2)};showRoot()
        ui.onNodeWithText("Enter • 80").performClick()
        ui.waitUntil(15000) {ui.onAllNodes(hasSetTextAction()).fetchSemanticsNodes().isNotEmpty()}
        assertEquals(Screen.writing,vm.currentScreen);assertTrue(vm.currentGamemode is StandardWriting)
        ui.onNode(hasSetTextAction()).assertTextContains(text)
        ui.onNode(hasClickAction() and hasText("Submit")).performClick()
        ui.waitUntil(40000) {vm.currentScreen==Screen.results}
        assertEquals("RANKED",vm.latestSubmission!!.playmode);assertEquals("STANDARD",vm.latestSubmission!!.gamemode)
        ui.onNodeWithText("80.00%").assertExists();ui.onNodeWithText("+457").assertExists()
        val data=Tasks.await(db.collection("users").document(user.id).get(),15,TimeUnit.SECONDS)
        assertEquals(2722L,data.getLong("merit"));assertEquals(900L,data.getLong("reputation"))
        val transactions=Tasks.await(db.collection("users").document(user.id).collection("meritTransactions").get(),15,TimeUnit.SECONDS)
        assertEquals(2,transactions.size());assertTrue(transactions.documents.any{it.getString("reason")=="ENTER_RANKED"&&it.getLong("amount")==-80L})
        val assignment=Tasks.await(db.collection("seasonAssignments").document(vm.latestSubmission!!.id).get(),15,TimeUnit.SECONDS)
        assertTrue(assignment.exists());assertTrue(assignment.getBoolean("eligible")!!)
    }

}
