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
import com.inkr8.repository.FirestoreSubmissionRepository
import com.inkr8.repository.FirestoreSubmission
import com.inkr8.mappers.toDomain
import com.inkr8.utils.DraftManager
import com.inkr8.viewmodel.AppViewModel
import org.json.JSONObject
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import java.io.File
import java.util.UUID
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

// SDK/snapshot/repository matrix. Root methods use actual automatic local Functions
// delivery and R8 HTTP double. No real OAuth/provider/Home entry or topic-name repair.
@RunWith(AndroidJUnit4::class)
class LabModeCompatibilityTest {
    @get:Rule val ui=createAndroidComposeRule<LabHostActivity>()
    private val db get()=FirebaseFirestore.getInstance()
    private val repo=FirestoreSubmissionRepository()
    private lateinit var user:Users
    @Before fun setup() {
        assertEquals("demo-inkr8-local",FirebaseApp.getInstance().options.projectId)
        assertEquals("com.inkr8.lab",ui.activity.packageName)
        FirebaseAuth.getInstance().signOut()
        val uid=Tasks.await(FirebaseAuth.getInstance().signInAnonymously(),15,TimeUnit.SECONDS).user!!.uid
        user=Users(id=uid,isPlaced=true,hasSeenPlacementReveal=true)
        Tasks.await(db.collection("users").document(uid).set(user),15,TimeUnit.SECONDS)
    }
    @After fun release() {ui.runOnIdle {ui.activity.viewModelStore.clear()}}
    private fun <T> waitRead(call: ((T)->Unit,(Exception)->Unit)->Unit):T {
        val done=CountDownLatch(1);val value=AtomicReference<T>();val error=AtomicReference<Exception>()
        call({value.set(it);done.countDown()},{error.set(it);done.countDown()})
        assertTrue(done.await(15,TimeUnit.SECONDS));assertNull(error.get());return value.get()
    }
    @Test fun everyRepositoryReaderPreservesModeAndPlaymode() {
        val fields=mutableListOf<Map<String,String>>()
        val expected=mutableListOf<String>()
        for(mode in listOf("STANDARD","ON_TOPIC")) {
            fields+=mapOf("gamemode" to mode);expected+=mode
            fields+=mapOf("gamemodeName" to mode);expected+=mode
            fields+=mapOf("gamemode" to mode,"gamemodeName" to mode);expected+=mode
            fields+=mapOf("gamemode" to mode,"gamemodeName" to if(mode=="STANDARD") "ON_TOPIC" else "STANDARD");expected+=mode
        }
        fields+=emptyMap();expected+="" // Android's original absent-both fallback.
        val ids=fields.indices.map{"lab-mode-$it-"+UUID.randomUUID()}
        val base=System.currentTimeMillis()
        for(i in fields.indices) Tasks.await(db.collection("submissions").document(ids[i]).set(
            mapOf("authorId" to user.id,"timestamp" to base+i,"playmode" to if(i%2==0) "RANKED" else "PRACTICE","status" to "EVALUATED")+fields[i]
        ),15,TimeUnit.SECONDS)
        val all=waitRead<List<Submissions>>{yes,no->repo.getAllSubmissions(user.id,yes,no)}
        File(ui.activity.filesDir,"android-mode-matrix.json").writeText(JSONObject(mapOf(
            "ids" to ids,"expected" to expected,"actual" to ids.map{id->all.single{it.id==id}.gamemode}
        )).toString(2))
        assertEquals(expected,ids.map{id->all.single{it.id==id}.gamemode})
        for(i in ids.indices) {
            val one=waitRead<Submissions?>{yes,no->repo.getSubmission(ids[i],yes,no)}!!
            assertEquals(expected[i],one.gamemode)
            assertEquals(if(i%2==0) "RANKED" else "PRACTICE",one.playmode)
            val seen=CountDownLatch(1);val value=AtomicReference<Submissions>()
            val listener=repo.listenToSubmission(ids[i],{value.set(it);seen.countDown()},{throw it})!!
            try {assertTrue(seen.await(15,TimeUnit.SECONDS));assertEquals(expected[i],value.get().gamemode)} finally {listener.remove()}
        }
        val last=waitRead<Submissions?>{yes,no->repo.getLastSubmission(yes,no)}!!
        assertEquals(ids.last(),last.id);assertEquals(expected.last(),last.gamemode)
        val registrations=mutableListOf<ListenerRegistration>()
        try {
            val allDone=CountDownLatch(1)
            registrations+=repo.listenToAllSubmissions(user.id,{if(it.size==9){assertEquals(expected,ids.map{id->it.single{s->s.id==id}.gamemode});allDone.countDown()}},{throw it})
            assertTrue(allDone.await(15,TimeUnit.SECONDS))
            val recentDone=CountDownLatch(1)
            registrations+=repo.listenToRecentRankedSubmissions({if(it.size==5){assertTrue(it.all{s->s.playmode=="RANKED"});for(s in it)assertEquals(expected[ids.indexOf(s.id)],s.gamemode);recentDone.countDown()}},{throw it})!!
            assertTrue(recentDone.await(15,TimeUnit.SECONDS))
            val lastDone=CountDownLatch(1)
            registrations+=repo.getLastSubmissionRealtime({assertEquals(ids.last(),it.id);lastDone.countDown()},{throw it})!!
            assertTrue(lastDone.await(15,TimeUnit.SECONDS))
        } finally {registrations.forEach{it.remove()}}
    }

    @Test fun invalidPayloadFallbacksCharacterization() {
        val cases=listOf(
            mapOf("gamemode" to null,"gamemodeName" to "ON_TOPIC"),
            mapOf("gamemode" to 7,"gamemodeName" to "ON_TOPIC"),
            mapOf("gamemode" to "ALIEN","gamemodeName" to "ON_TOPIC"),
            mapOf("gamemode" to "","gamemodeName" to "ON_TOPIC"),
            mapOf("gamemodeName" to null),mapOf("gamemodeName" to 7),
            mapOf("gamemode" to "STANDARD","gamemodeName" to 7),
            emptyMap<String,Any?>()
        )
        val observations=cases.mapIndexed {index,fields->
            val ref=db.collection("submissions").document("lab-invalid-mode-"+UUID.randomUUID())
            Tasks.await(ref.set(mapOf("status" to "EVALUATED","authorId" to "invalid-fixture")+fields),15,TimeUnit.SECONDS)
            val snap=Tasks.await(ref.get(Source.SERVER),15,TimeUnit.SECONDS)
            try {
                // Same source matrix can run before/after reader addition. Reflection
                // selects the real mapper API, not a mode/Firestore implementation double.
                val method=Class.forName("com.inkr8.mappers.SubmissionMapperKt").methods.firstOrNull{it.name=="toSubmission"}
                val value=if(method==null) snap.toObject(FirestoreSubmission::class.java)!!.toDomain()
                    else method.invoke(null,snap) as Submissions
                mapOf("index" to index,"fields" to fields,"mode" to value.gamemode)
            } catch(error:Exception) {
                val actual=(error as? java.lang.reflect.InvocationTargetException)?.targetException?:error
                mapOf("index" to index,"fields" to fields,"errorType" to actual.javaClass.name,"error" to (actual.message?:""))
            }
        }
        File(ui.activity.filesDir,"android-invalid-modes.json").writeText(JSONObject(mapOf("rows" to observations)).toString(2))
        assertEquals("ON_TOPIC",observations[0]["mode"])
        assertEquals("ON_TOPIC",observations[1]["mode"])
        assertTrue(observations[5].containsKey("errorType"))
        assertTrue(observations[6].containsKey("errorType"))
        assertEquals("",observations[7]["mode"])
    }

    private fun root(mode:Gamemode,name:String) {
        val text="Mode$name fixture. Bright rivers carry ancient stories while curious writers explore the quiet valley. Every morning a traveler observes changing shadows beside patient trees and remembers distant friends. Gentle rain reveals new colors across the landscape, inspiring thoughtful descriptions of journeys, discoveries, and ordinary moments that deserve careful attention from everyone around us."
        val key=LabDraftFixtures.seed(ui.activity,text,mode=mode)
        lateinit var vm:AppViewModel
        ui.runOnIdle {vm=ViewModelProvider(ui.activity,AppViewModelFactory(user))[user.id, AppViewModel::class.java];vm.startWriting(mode,PlayMode.Practice)}
        ui.activityRule.scenario.onActivity{activity->activity.show{
            val launcher=rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()){}
            AppRoot(user,launcher,{})
        }}
        ui.onNode(hasSetTextAction()).assertTextContains(text)
        ui.onNode(hasClickAction() and hasText("Submit")).performClick()
        ui.waitUntil(40000){vm.currentScreen==Screen.results||vm.currentScreen==Screen.home}
        assertEquals(Screen.results,vm.currentScreen)
        val result=vm.latestSubmission!!
        val snap=Tasks.await(db.collection("submissions").document(result.id).get(Source.SERVER),15,TimeUnit.SECONDS)
        assertEquals(name,snap.getString("gamemodeName"));assertFalse(snap.contains("gamemode"))
        assertEquals(name,result.gamemode);assertEquals("PRACTICE",result.playmode)
        assertEquals(80.0,result.evaluation!!.finalScore,0.0)
        assertEquals("",DraftManager.getDraft(ui.activity,key))
        ui.onNodeWithText("80.00%").assertExists()
        ui.onNodeWithText("Explicit local transport fixture. No provider called.").assertExists()
        File(ui.activity.filesDir,"root-mode-$name.json").writeText(JSONObject(mapOf(
            "submissionId" to result.id,"mode" to name,"text" to text,"writerGamemodeAbsent" to true,
            "domainMode" to result.gamemode,"screen" to vm.currentScreen.name,"score" to result.evaluation!!.finalScore,
            "topicId" to (snap.getString("topicId")?:""),"themeId" to (snap.getString("themeId")?:"")
        )).toString(2))
    }
    @Test fun standardRootReachesLocalEvaluatorAndResults()=root(StandardWriting,"STANDARD")
    @Test fun onTopicRootReachesLocalEvaluatorAndResults()=root(
        OnTopicWriting(Theme(id="lab-mode-theme",name="Nature"),Topic(id="lab-mode-topic",themeId="lab-mode-theme",name="Rivers")),"ON_TOPIC")
}
