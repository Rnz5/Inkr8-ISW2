package com.inkr8.viewmodel

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ListenerRegistration
import com.inkr8.AdManager
import com.inkr8.data.*
import com.inkr8.repository.*
import com.inkr8.utils.SystemConfig
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AppViewModel(
    initialUser: Users,
    private val submissionRepository: FirestoreSubmissionRepository = FirestoreSubmissionRepository(),
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    var currentUser by mutableStateOf(initialUser)
        private set

    var currentScreen by mutableStateOf(Screen.home)
    var pagerInitialPage by mutableIntStateOf(1)
    
    var currentGamemode by mutableStateOf<Gamemode?>(null)
    var currentPlayMode by mutableStateOf<PlayMode>(PlayMode.Practice)
    
    var latestSubmission by mutableStateOf<Submissions?>(null)
    var allSubmissions by mutableStateOf<List<Submissions>>(emptyList())
    var isLoadingSubmissions by mutableStateOf(true)
    
    var submissionAdCounter by mutableIntStateOf(0)
    var pendingNavigationAfterAd by mutableStateOf<Screen?>(null)
    
    private var previousUserIsPlaced = initialUser.isPlaced

    val isPlacementRevealRequired by derivedStateOf {
        currentUser.isPlaced && !currentUser.hasSeenPlacementReveal
    }

    // Loading screen state
    var loadingResolved by mutableStateOf(false)
    var loadingTimeout by mutableStateOf(false)
    var loadingElapsedSeconds by mutableIntStateOf(0)
    private var loadingPollJob: Job? = null
    private var loadingWaitGeneration = 0L
    private var confirmedSubmissionId: String? = null
    var loadingQueryError by mutableStateOf<String?>(null)
        private set
    var isPersistingSubmission by mutableStateOf(false)
        private set
    private var persistenceGeneration = 0L

    // Listeners
    private var submissionsListener: ListenerRegistration? = null
    private var loadingResultListener: ListenerRegistration? = null
    private var userObserverJob: Job? = null

    init {
        observeCurrentUser()
        observeSubmissions()
    }

    private fun observeCurrentUser() {
        userObserverJob?.cancel()
        userObserverJob = viewModelScope.launch {
            userRepository.listenToUser(currentUser.id).collectLatest { updated ->
                updated?.let { 
                    currentUser = it 
                    }
            }
        }
    }

    fun navigateTo(screen: Screen, page: Int? = null) {
        page?.let { pagerInitialPage = it }
        currentScreen = screen
    }

    fun startWriting(gamemode: Gamemode, playMode: PlayMode) {
        // Leaving the old result context invalidates its queued responses immediately.
        loadingWaitGeneration++
        loadingResultListener?.remove()
        loadingResultListener = null
        loadingPollJob?.cancel()
        loadingPollJob = null
        confirmedSubmissionId = null
        loadingQueryError = null
        loadingResolved = false
        loadingTimeout = false
        loadingElapsedSeconds = 0
        currentGamemode = gamemode
        currentPlayMode = playMode
        latestSubmission = null
        navigateTo(Screen.writing)
    }

    private fun observeSubmissions() {
        submissionsListener?.remove()
        isLoadingSubmissions = true
        submissionsListener = submissionRepository.listenToAllSubmissions(
            authorId = currentUser.id,
            onUpdate = { updated ->
                allSubmissions = updated
                isLoadingSubmissions = false
            },
            onError = { error ->
                Log.e("AppViewModel", "Submissions listener error: ${error.message}")
                isLoadingSubmissions = false
            }
        )
    }

    fun submitWriting(
        submission: Submissions,
        onPersisted: () -> Unit = {},
        onError: (String) -> Unit
    ) {
        if (isPersistingSubmission) {
            onError("An entry is still being saved. Please wait for confirmation.")
            return
        }
        isPersistingSubmission = true
        val generation = ++persistenceGeneration
        fun finishPersistence(): Boolean {
            if (generation != persistenceGeneration || !isPersistingSubmission) return false
            isPersistingSubmission = false
            return true
        }
        val finalSubmission = submission.copy(
            authorId = currentUser.id,
            status = SubmissionStatus.PENDING,
            evaluation = null
        )

        submissionRepository.addSubmission(
            submission = finalSubmission,
            onSuccess = {
                if (finishPersistence()) {
                    onPersisted()
                    startLoadingResult(finalSubmission.id)
                }
            },
            onError = { e ->
                if (finishPersistence()) {
                    userRepository.finishRankedSession(currentUser.id)
                    onError(e.message ?: "Submission failed")
                }
            }
        )
    }

    fun saveSubmission(submissionId: String, onError: (String) -> Unit) {
        submissionRepository.saveSubmission(
            submissionId = submissionId,
            onSuccess = {},
            onError = { e -> onError(e.message ?: "Failed to save") }
        )
    }

    fun deleteSubmission(submissionId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        submissionRepository.deleteSubmission(
            submissionId = submissionId,
            onSuccess = onSuccess,
            onError = { e -> onError(e.message ?: "Failed to delete") }
        )
    }

    fun deleteAccount(onSuccess: () -> Unit, onError: (String) -> Unit) {
        userRepository.deleteAccount(
            userId = currentUser.id,
            onSuccess = onSuccess,
            onError = { e -> onError(e.message ?: "Failed to delete account") }
        )
    }

    fun enablePhilosopher(onSuccess: () -> Unit, onError: (String) -> Unit) {
        userRepository.enablePhilosopher(
            purchaseToken = "test_token",
            productId = "philosopher_sub",
            onSuccess = {
                onSuccess()
            },
            onError = { e -> onError(e.message ?: "Failed to enable Philosopher") }
        )
    }

    fun changeUsername(newName: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        userRepository.changeUsernameWithMerit(
            newUsername = newName,
            onSuccess = {
                onSuccess()
            },
            onError = { e -> onError(e.message ?: "Failed to change username") }
        )
    }

    fun checkUsernameAvailability(name: String, callback: (Boolean) -> Unit) {
        userRepository.isUsernameAvailable(name, callback)
    }

    fun validateUsername(name: String): String? {
        return userRepository.validateUsername(name)
    }

    fun loadLatestSubmission() {
        submissionRepository.getLastSubmission(
            onSuccess = { latestSubmission = it },
            onError = { it.printStackTrace() }
        )
    }

    private fun startLoadingResult(submissionId: String) {
        val waitGeneration = ++loadingWaitGeneration
        loadingResolved = false
        loadingTimeout = false
        loadingElapsedSeconds = 0
        loadingQueryError = null
        confirmedSubmissionId = submissionId
        navigateTo(Screen.loading)
        
        loadingResultListener?.remove()
        loadingResultListener = submissionRepository.listenToSubmission(
            submissionId = submissionId,
            onUpdate = { submission ->
                handleSubmissionUpdate(submission, submissionId, waitGeneration)
            },
            onError = { handleLoadingQueryError(submissionId, waitGeneration) }
        )

        loadingPollJob?.cancel()
        loadingPollJob = viewModelScope.launch {
            var pollCount = 0
            while (waitGeneration == loadingWaitGeneration && !loadingResolved && !loadingTimeout) {
                delay(3000)
                if (waitGeneration != loadingWaitGeneration) break
                pollCount++
                loadingElapsedSeconds = resultWaitElapsedSeconds(pollCount)
                if (hasResultWaitTimedOut(loadingElapsedSeconds)) {
                    loadingTimeout = true
                    break
                }
                submissionRepository.getSubmission(
                    submissionId = submissionId,
                    onSuccess = { submission ->
                        submission?.let { handleSubmissionUpdate(it, submissionId, waitGeneration) }
                    },
                    onError = { handleLoadingQueryError(submissionId, waitGeneration) }
                )
            }
        }
    }

    private fun handleLoadingQueryError(submissionId: String, waitGeneration: Long) {
        if (waitGeneration != loadingWaitGeneration || confirmedSubmissionId != submissionId ||
            currentScreen != Screen.loading || loadingResolved || loadingTimeout) return
        loadingQueryError = "We could not check your result. Your entry is saved. Retry checking the same entry."
        Log.w("ResultWait", "query_error submissionId=$submissionId generation=$waitGeneration")
    }

    fun retryLoadingResult() {
        val id = confirmedSubmissionId ?: return
        if (currentScreen != Screen.loading || loadingResolved || (!loadingTimeout && loadingQueryError == null)) return
        startLoadingResult(id)
        val generation = loadingWaitGeneration
        Log.i("ResultWait", "query_retry submissionId=$id generation=$generation")
        submissionRepository.getSubmission(
            submissionId = id,
            onSuccess = { it?.let { submission -> handleSubmissionUpdate(submission, id, generation) } },
            onError = { handleLoadingQueryError(id, generation) }
        )
    }

    private fun handleSubmissionUpdate(submission: Submissions, submissionId: String, waitGeneration: Long) {
        if (waitGeneration != loadingWaitGeneration || submission.id != submissionId) return
        if (loadingResolved && !loadingTimeout && currentScreen == Screen.results &&
            latestSubmission?.id == submissionId && isEvaluatedResult(submission.status) &&
            submission.playmode == "RANKED" &&
            (submission.matchStatus == "MATCHED" || submission.matchStatus == "GHOST")) {
            latestSubmission = submission
            return
        }
        if (!loadingResolved && !loadingTimeout) {
            if (isEvaluatedResult(submission.status)) {
                loadingResolved = true
                latestSubmission = submission
                
                val justGotPlaced = !previousUserIsPlaced && currentUser.isPlaced && !currentUser.hasSeenPlacementReveal
                previousUserIsPlaced = currentUser.isPlaced
                navigateTo(if (justGotPlaced) Screen.placementReveal else Screen.results)
                
            } else if (isFailedResult(submission.status)) {
                loadingResolved = true
                navigateTo(Screen.home)
            }
        }
    }

    fun continueWithAd(activity: Activity?, nextScreen: Screen, beforeNavigate: (() -> Unit)? = null) {
        if (currentUser.isPhilosopher) {
            beforeNavigate?.invoke()
            navigateTo(nextScreen)
            return
        }
        submissionAdCounter++
        pendingNavigationAfterAd = nextScreen
        if (submissionAdCounter % 2 == 0) {
            activity?.let { AdManager.showAd(it) }
            beforeNavigate?.invoke()
            navigateTo(nextScreen)
        } else {
            beforeNavigate?.invoke()
            navigateTo(Screen.postSubmissionAd)
        }
    }

    fun applyMeritAction(action: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        userRepository.applyMeritAction(
            action = action,
            onSuccess = {
                onSuccess()
            },
            onError = { e -> onError(e.message ?: "Action failed") }
        )
    }

    fun onPlacementRevealSeen(onComplete: () -> Unit) {
        userRepository.markPlacementRevealSeen(
            userId = currentUser.id,
            onSuccess = {
                navigateTo(Screen.results)
                onComplete()
            },
            onError = { it.printStackTrace() }
        )
    }

    override fun onCleared() {
        loadingWaitGeneration++
        submissionsListener?.remove()
        loadingResultListener?.remove()
        userObserverJob?.cancel()
        loadingPollJob?.cancel()
        super.onCleared()
    }
}
