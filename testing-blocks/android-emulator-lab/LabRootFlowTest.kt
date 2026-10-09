package com.inkr8.lab

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.text.AnnotatedString
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
import com.inkr8.viewmodel.AppViewModel
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.json.JSONObject
import java.io.File
import java.util.UUID
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

// Actual AppRoot -> Writing -> VM -> repository -> local Firestore SDK route.
// Anonymous Auth emulator is real; OAuth/Ads/MainActivity are bypassed.
// Evaluator delivery is disabled. No R8 response or result identity is tested here.
@RunWith(AndroidJUnit4::class)
class LabRootFlowTest {
    @get:Rule val ui = createAndroidComposeRule<LabHostActivity>()
    private val db get() = FirebaseFirestore.getInstance()
    private lateinit var key: String
    private val text = "Bright rivers carry ancient stories while curious writers explore the quiet valley. Every morning a traveler observes changing shadows beside patient trees and remembers distant friends. Gentle rain reveals new colors across the landscape, inspiring thoughtful descriptions of journeys, discoveries, and ordinary moments that deserve careful attention from everyone around us."
    private lateinit var user: Users
    private lateinit var vm: AppViewModel
    private var registration: ListenerRegistration? = null

    @Before fun localOnly() {
        assertEquals("demo-inkr8-local", FirebaseApp.getInstance().options.projectId)
        assertEquals("com.inkr8.lab", ui.activity.packageName)
        Tasks.await(db.enableNetwork(), 15, TimeUnit.SECONDS)
        FirebaseAuth.getInstance().signOut()
        val uid = Tasks.await(FirebaseAuth.getInstance().signInAnonymously(), 15, TimeUnit.SECONDS).user!!.uid
        user = Users(id = uid, name = "A05 local fixture", isPlaced = true, hasSeenPlacementReveal = true)
        Tasks.await(db.collection("users").document(uid).set(user), 15, TimeUnit.SECONDS)
        key = LabDraftFixtures.seed(ui.activity, text)
        ui.runOnIdle {
            // Same Activity owner and account VM key as production AppRoot.viewModel().
            vm = ViewModelProvider(ui.activity, AppViewModelFactory(user))[user.id, AppViewModel::class.java]
            vm.startWriting(StandardWriting, PlayMode.Practice)
        }
    }

    @After fun release() {
        registration?.remove()
        // Settle the intentional offline fixture; do not delete local documents.
        Tasks.await(db.enableNetwork(), 15, TimeUnit.SECONDS)
        Tasks.await(db.waitForPendingWrites(), 15, TimeUnit.SECONDS)
        ui.runOnIdle { ui.activity.viewModelStore.clear() }
    }

    private fun showRoot() {
        ui.activityRule.scenario.onActivity { activity ->
            activity.show {
                val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { }
                AppRoot(initialUser = user, googleLauncher = launcher, onSessionEnded = {})
            }
        }
        ui.onNode(hasSetTextAction()).assertTextContains(text)
    }

    private fun submit() = ui.onNode(hasClickAction() and hasText("Submit")).performClick()
    private fun drafts() = DraftManager.getDraft(ui.activity, key)
    private fun observe(name: String, fields: Map<String, Any>) {
        File(ui.activity.filesDir, name).writeText(JSONObject(fields).toString(2))
    }

    @Test fun pendingPreservesEditorAndDraft_throughAppRoot() {
        showRoot()
        Tasks.await(db.disableNetwork(), 15, TimeUnit.SECONDS)
        submit()
        ui.waitUntil(10000) { vm.allSubmissions.any { it.content == text } }
        val pending = Tasks.await(db.collection("submissions").whereEqualTo("authorId", user.id).get(Source.CACHE), 15, TimeUnit.SECONDS).documents.single()
        observe("root-pending.json", mapOf("submissionId" to pending.id, "hasPendingWrites" to pending.metadata.hasPendingWrites(), "draftLength" to drafts().length, "screen" to vm.currentScreen.name))
        assertTrue("Write must be locally pending, not server-confirmed", pending.metadata.hasPendingWrites())
        assertEquals(Screen.writing, vm.currentScreen)
        ui.onNode(hasSetTextAction()).assertTextContains(text)
        assertEquals(text, drafts())
    }

    @Test fun confirmedPersistenceClearsDraftAndRestoredEditor_throughAppRoot() {
        showRoot()
        submit()
        ui.waitUntil(15000) { vm.currentScreen == Screen.loading }
        val persisted = Tasks.await(db.collection("submissions").whereEqualTo("authorId", user.id).get(Source.SERVER), 15, TimeUnit.SECONDS).documents.single()
        assertEquals(text, persisted.getString("content"))
        assertEquals("PENDING", persisted.getString("status"))
        assertFalse(persisted.metadata.hasPendingWrites())
        assertEquals("", drafts())
        observe("root-success.json", mapOf("submissionId" to persisted.id, "serverConfirmed" to true, "draftLength" to drafts().length, "screenAfterSuccess" to vm.currentScreen.name))
        // Success navigates away in the real root. Re-enter the same exercise to
        // verify restoration; the isolated success test checks the same live editor.
        ui.runOnIdle { vm.startWriting(StandardWriting, PlayMode.Practice) }
        // A confirmed entry releases its assignment. Await the genuine next
        // repository assignment before asserting the restored editor, not a timer sleep.
        ui.waitUntil(15000) { ui.onAllNodes(hasSetTextAction()).fetchSemanticsNodes().size == 1 }
        ui.onNode(hasSetTextAction()).assert(SemanticsMatcher.expectValue(SemanticsProperties.EditableText, AnnotatedString("")))
    }

    @Test fun rejectedPersistencePreservesEditorAndDraft_throughAppRoot() {
        // Test-only per-account marker: production factory/UUID are untouched.
        Tasks.await(db.collection("labDeniedWrites").document(user.id).set(mapOf("fixture" to true)), 15, TimeUnit.SECONDS)
        val ruleProbe = db.collection("submissions").document("lab-root-rule-probe-" + UUID.randomUUID())
        try {
            Tasks.await(ruleProbe.set(mapOf("authorId" to user.id)), 15, TimeUnit.SECONDS)
            fail("The local rules must reject this account's write")
        } catch (failure: ExecutionException) {
            assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, (failure.cause as FirebaseFirestoreException).code)
        }
        val attemptedId = AtomicReference<String?>()
        val rolledBack = CountDownLatch(1)
        registration = db.collection("submissions").whereEqualTo("authorId", user.id)
            .addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, error ->
                assertNull(error)
                if (snapshot != null) {
                    snapshot.documents.firstOrNull { it.metadata.hasPendingWrites() && it.getString("content") == text }?.let { attemptedId.set(it.id) }
                    if (attemptedId.get() != null && snapshot.documents.none { it.id == attemptedId.get() }) rolledBack.countDown()
                }
            }
        showRoot()
        submit()
        assertTrue("The actual AppRoot submission must be rejected and rolled back", rolledBack.await(15, TimeUnit.SECONDS))
        val id = attemptedId.get()!!
        assertFalse("Factory UUID must not be replaced by the denial-prefix fixture", id.startsWith("lab-denied-"))
        assertFalse(Tasks.await(db.collection("submissions").document(id).get(Source.SERVER), 15, TimeUnit.SECONDS).exists())
        observe("root-rejected.json", mapOf("submissionId" to id, "ruleProbeCode" to "PERMISSION_DENIED", "actualSubmissionRolledBack" to true, "draftLength" to drafts().length, "screen" to vm.currentScreen.name))
        ui.onNode(hasSetTextAction()).assertTextContains(text)
        assertEquals(text, drafts())
        assertEquals(Screen.writing, vm.currentScreen)
        ui.waitUntil(10000) { !vm.isPersistingSubmission }
        ui.onNode(hasClickAction() and hasText("Submit")).assertIsEnabled()
    }
}
