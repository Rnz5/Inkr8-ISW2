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
import com.google.firebase.firestore.*
import com.inkr8.AppRoot
import com.inkr8.AppViewModelFactory
import com.inkr8.data.*
import com.inkr8.utils.DraftManager
import com.inkr8.repository.UserRepository
import com.inkr8.viewmodel.AppViewModel
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/** Strict proposed rules; Admin fixture endpoint is explicit, never product authorization. */
@RunWith(AndroidJUnit4::class)
class LabSecureAcceptanceTest {
 @get:Rule val ui=createAndroidComposeRule<LabHostActivity>()
 private val db get()=FirebaseFirestore.getInstance()
 private lateinit var user:Users
 private lateinit var vm:AppViewModel
 private fun fixture(kind:String) {
  val c=URL("http://127.0.0.1:5011/fixture/$kind").openConnection() as HttpURLConnection
  c.requestMethod="POST";c.doOutput=true;c.connectTimeout=15000;c.readTimeout=15000
  c.setRequestProperty("Content-Type","application/json");c.outputStream.use{it.write(JSONObject(mapOf("uid" to user.id)).toString().toByteArray())}
  assertEquals(200,c.responseCode);c.disconnect()
 }
 @Before fun setup(){
  assertEquals("demo-inkr8-local",FirebaseApp.getInstance().options.projectId)
  FirebaseAuth.getInstance().signOut()
  val uid=Tasks.await(FirebaseAuth.getInstance().signInAnonymously(),15,TimeUnit.SECONDS).user!!.uid
  user=Users(id=uid,name="Secure local fixture",rating=500,merit=2345,isPlaced=true,hasChosenUsername=true,hasSeenPlacementReveal=true,reputation=900)
  fixture("bootstrap")
  ui.runOnIdle{vm=ViewModelProvider(ui.activity,AppViewModelFactory(user))[uid,AppViewModel::class.java]}
 }
 @After fun release(){ui.runOnIdle{ui.activity.viewModelStore.clear()}}
 private fun show(){ui.activityRule.scenario.onActivity{it.show{
  val launcher=rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()){}
  AppRoot(user,launcher,{})
 }}}
 private fun mode(topic:Boolean):Gamemode=if(topic) OnTopicWriting(Theme(id="fin-theme",name="Nature"),Topic(id="fin-topic",themeId="fin-theme",name="Rivers")) else StandardWriting
 private fun root(topic:Boolean,ranked:Boolean){
  val m=mode(topic);val play=if(ranked) PlayMode.Ranked else PlayMode.Practice
  if(ranked){val done=CountDownLatch(1);var error:Exception?=null;UserRepository().applyMeritAction("ENTER_RANKED",{done.countDown()},{error=it;done.countDown()});assertTrue(done.await(15,TimeUnit.SECONDS));assertNull(error)}
  val text="Bright rivers carry ancient stories while curious writers explore the quiet valley. Every morning a traveler observes changing shadows beside patient trees and remembers distant friends. Gentle rain reveals new colors across the landscape, inspiring thoughtful descriptions of journeys, discoveries, and ordinary moments that deserve careful attention from everyone around us."
  val key=LabDraftFixtures.seed(ui.activity,text,mode=m,play=play)
  ui.runOnIdle{vm.startWriting(m,play)};show()
  ui.onNode(hasSetTextAction()).assertTextContains(text)
  ui.onNode(hasClickAction() and hasText("Submit")).performClick()
  ui.waitUntil(45000){vm.currentScreen==Screen.results||vm.currentScreen==Screen.home}
  assertEquals(Screen.results,vm.currentScreen);assertEquals(if(topic)"ON_TOPIC" else "STANDARD",vm.latestSubmission!!.gamemode)
  assertEquals(if(ranked)"RANKED" else "PRACTICE",vm.latestSubmission!!.playmode)
  assertEquals(80.0,vm.latestSubmission!!.evaluation!!.finalScore,0.0)
  assertEquals("",DraftManager.getDraft(ui.activity,key));ui.onNodeWithText("80.00%").assertExists()
  val snap=Tasks.await(db.collection("submissions").document(vm.latestSubmission!!.id).get(Source.SERVER),15,TimeUnit.SECONDS)
  assertEquals(user.id,snap.getString("authorId"));assertEquals("EVALUATED",snap.getString("status"))
 }
 @Test fun practiceStandardWithRestrictedRules()=root(false,false)
 @Test fun practiceOnTopicWithRestrictedRules()=root(true,false)
 @Test fun paidRankedStandardWithRestrictedRules()=root(false,true)
 @Test fun paidRankedOnTopicWithRestrictedRules()=root(true,true)
 @Test fun confirmedMissingDeleteUsesTrustedRepositoryCallback(){
  val done=CountDownLatch(1);var error:Exception?=null;var successes=0
  com.inkr8.repository.FirestoreSubmissionRepository().deleteSubmission(
   "fin-confirmed-missing-${user.id}", {successes++;done.countDown()}, {error=it;done.countDown()})
  assertTrue(done.await(20,TimeUnit.SECONDS));assertNull(error);assertEquals(1,successes)
  val snapshot=Tasks.await(db.collection("users").document(user.id).get(Source.SERVER),15,TimeUnit.SECONDS)
  assertEquals(2345L,snapshot.getLong("merit"));assertEquals(500L,snapshot.getLong("rating"))
 }
 @Test fun profileAndOwnSeasonHistoryWithAdminCollectionsDenied(){
  fixture("history");ui.runOnIdle{vm.navigateTo(Screen.profile)};show()
  ui.onNodeWithText(user.name).assertExists();ui.onNodeWithText("Temporadas").performScrollTo().performClick()
  // The heading exists with an ellipsis before either HTTP query has completed.
  // Wait for the actual history result, then scroll using the existing acceptance target.
  ui.waitUntil(20000){
   try {
    ui.onNode(hasScrollToNodeAction()).performScrollToNode(hasText("1997-01"))
    ui.onAllNodes(hasText("1997-01",substring=false)).fetchSemanticsNodes().isNotEmpty()
   } catch (_: AssertionError) { false }
  }
  ui.onNode(hasScrollToNodeAction()).performScrollToNode(hasText("1997-01"));ui.onNodeWithText("1997-01").performClick()
  ui.onNode(hasScrollToNodeAction()).performScrollToNode(hasText("Rating de cierre: 14"));ui.onNodeWithText("Rating de cierre: 14").assertExists()
  val failure=try{Tasks.await(db.collection("seasons").document("1997-01").get(Source.SERVER),15,TimeUnit.SECONDS);null}catch(e:Exception){e}
  assertNotNull(failure)
 }
 @Test fun realPermissionRejectionPreservesDraftAndText(){
  val text="A pending draft must survive a rejected write while its author remains in the same exercise and revision. These patient words provide enough characters for the normal admission contract and a clear preservation assertion. The writer describes quiet rivers, distant mountains, changing skies, gentle rain, and thoughtful journeys through a peaceful valley while keeping this exact revision available for recovery."
  val key=LabDraftFixtures.seed(ui.activity,text,mode=StandardWriting,play=PlayMode.Ranked)
  fixture("failure");ui.runOnIdle{vm.startWriting(StandardWriting,PlayMode.Ranked)};show()
  // Writing first resolves the scoped assignment asynchronously; no editor yet on its loading screen.
  ui.waitUntil(15000){ui.onAllNodes(hasSetTextAction()).fetchSemanticsNodes().isNotEmpty()}
  ui.onNode(hasSetTextAction()).assertTextContains(text)
  ui.onNode(hasClickAction() and hasText("Submit")).performClick()
  ui.waitUntil(15000){!vm.isPersistingSubmission}
  assertEquals(Screen.writing,vm.currentScreen);ui.onNode(hasSetTextAction()).assertTextContains(text)
  assertEquals(text,DraftManager.getDraft(ui.activity,key))
  assertTrue(Tasks.await(db.collection("submissions").whereEqualTo("authorId",user.id).get(Source.SERVER),15,TimeUnit.SECONDS).isEmpty)
 }
}
