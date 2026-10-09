package com.inkr8.lab

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.lifecycle.ViewModelStore
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.inkr8.data.Submissions
import com.inkr8.data.Users
import com.inkr8.data.Screen
import com.inkr8.data.Evaluation
import com.inkr8.data.SubmissionStatus
import com.inkr8.viewmodel.AppViewModel
import com.inkr8.repository.FirestoreSubmissionRepository
import com.inkr8.repository.FirestoreSubmission
import com.inkr8.mappers.toSubmission
import com.inkr8.mappers.toDomain
import com.inkr8.mappers.toFirestore
import org.junit.Assert.*
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

// Real Android SDK + local Firestore. No production data or evaluator involved.
@RunWith(AndroidJUnit4::class)
class LabDataContractTest {
    @get:Rule val ui = createAndroidComposeRule<LabHostActivity>()
    private val db get() = FirebaseFirestore.getInstance()
    private lateinit var uid: String
    @Before fun localOnly() {
        FirebaseAuth.getInstance().signOut()
        uid = Tasks.await(FirebaseAuth.getInstance().signInAnonymously(),15,TimeUnit.SECONDS).user!!.uid
    }

    @Test fun userWriterUsesTheExistingServerFlagNames() {
        val ref = db.collection("users").document(uid)
        Tasks.await(ref.set(Users(id=uid,isPlaced=true,isPhilosopher=true)),15,TimeUnit.SECONDS)
        val stored = Tasks.await(ref.get(),15,TimeUnit.SECONDS)
        assertEquals("Writer must use the existing server field",true,stored.getBoolean("isPlaced"))
        assertEquals(true,stored.getBoolean("isPhilosopher"))
        assertFalse("No JavaBean alias must be created",stored.contains("placed"))
        assertFalse(stored.contains("philosopher"))
        val actual = stored.toObject(Users::class.java)!!
        assertTrue(actual.isPlaced)
        assertTrue(actual.isPhilosopher)
    }

    @Test fun canonicalServerFlagsAreReadDespiteOldFalseAliases() {
        val ref = db.collection("users").document(uid)
        // Initial profiles written by the old Android writer contain these false aliases.
        Tasks.await(ref.set(mapOf("id" to uid,"placed" to false,"philosopher" to false,
            "isPlaced" to true,"isPhilosopher" to true)),15,TimeUnit.SECONDS)
        val actual = Tasks.await(ref.get(),15,TimeUnit.SECONDS).toObject(Users::class.java)!!
        assertTrue("Server placement must be visible",actual.isPlaced)
        assertTrue("Server philosopher flag must be visible",actual.isPhilosopher)
    }

    @Test fun serverPlacementFlagReachesTheViewModelAndRevealConsumer() {
        val user=Users(id=uid,isPlaced=false,hasSeenPlacementReveal=false)
        val ref=db.collection("users").document(uid)
        Tasks.await(ref.set(user),15,TimeUnit.SECONDS)
        val store=ViewModelStore()
        lateinit var vm:AppViewModel
        ui.runOnIdle {vm=AppViewModel(user);store.put("profile-consumer",vm)}
        try {
            val a=Submissions(id="lab-core-placement-"+UUID.randomUUID(),authorId=uid,
                content="Pending fixture",timestamp=System.currentTimeMillis())
            ui.runOnIdle {vm.submitWriting(a) {fail("Unexpected persistence failure: $it")}}
            ui.waitUntil(15000) {vm.currentScreen==Screen.loading}
            Tasks.await(ref.update(mapOf("isPlaced" to true,"isPhilosopher" to true)),15,TimeUnit.SECONDS)
            ui.waitUntil(15000) {vm.currentUser.isPlaced&&vm.currentUser.isPhilosopher}
            assertTrue(vm.isPlacementRevealRequired)
            Tasks.await(db.collection("submissions").document(a.id).update(mapOf(
                "status" to "EVALUATED","evaluation" to Evaluation(finalScore=80.0,
                    resultStatus=SubmissionStatus.EVALUATED,feedback="Explicit placement fixture").toFirestore())),15,TimeUnit.SECONDS)
            ui.waitUntil(15000) {vm.loadingResolved}
            assertEquals(a.id,vm.latestSubmission?.id)
            assertEquals(Screen.placementReveal,vm.currentScreen)
            // Genuine user listener + navigation decision; not automatic backend placement.
        } finally {ui.runOnIdle {store.clear()}}
    }

    @Test fun clientOnTopicPayloadCharacterizesTheTwoExistingNames() {
        val submission = Submissions(id="lab-core-mode-"+UUID.randomUUID(),authorId=uid,
            content="Client payload fixture",gamemode="ON_TOPIC",playmode="PRACTICE",
            wordCount=51,timestamp=System.currentTimeMillis(),topicId="lab-topic",themeId="lab-theme")
        val done = CountDownLatch(1)
        val error = AtomicReference<Exception?>()
        FirestoreSubmissionRepository().addSubmission(submission,
            onSuccess={done.countDown()},onError={error.set(it);done.countDown()})
        assertTrue(done.await(15,TimeUnit.SECONDS)); assertNull(error.get())
        val stored = Tasks.await(db.collection("submissions").document(submission.id).get(),15,TimeUnit.SECONDS)
        assertEquals("ON_TOPIC",stored.getString("gamemodeName"))
        assertFalse("Current Android writer has no engine gamemode field; this is characterization",stored.contains("gamemode"))
        assertEquals("ON_TOPIC",stored.toObject(FirestoreSubmission::class.java)!!.toDomain().gamemode)
        File(ui.activity.filesDir,"core-mode-payload.json").writeText(JSONObject(mapOf(
            "submissionId" to submission.id,"project" to "demo-inkr8-local",
            "expectedMode" to submission.gamemode,"storedGamemodeName" to stored.getString("gamemodeName"),
            "hasGamemode" to stored.contains("gamemode"))).toString(2))
    }

    @Test fun authenticServerGamemodeIsReadByTheAndroidModel_acceptanceProbe() {
        val ref = db.collection("submissions").document("lab-core-server-mode-"+UUID.randomUUID())
        Tasks.await(ref.set(mapOf("authorId" to uid,"gamemode" to "ON_TOPIC","status" to "PENDING")),15,TimeUnit.SECONDS)
        val actual = Tasks.await(ref.get(),15,TimeUnit.SECONDS).toSubmission()!!
        // Deliberately red until the integration/compatibility contract is implemented.
        assertEquals("Authentic server gamemode must not be lost","ON_TOPIC",actual.gamemode)
    }
}
