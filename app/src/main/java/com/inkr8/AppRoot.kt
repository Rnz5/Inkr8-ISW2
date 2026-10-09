package com.inkr8

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.lifecycle.viewmodel.compose.viewModel
import com.inkr8.data.*
import com.inkr8.screens.*
import com.inkr8.viewmodel.AppViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inkr8.utils.SystemConfig

@Suppress("UNCHECKED_CAST")
class AppViewModelFactory(private val initialUser: Users) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AppViewModel(initialUser) as T
    }
}

@Composable
fun AppRoot(
    initialUser: Users,
    googleLauncher: androidx.activity.result.ActivityResultLauncher<android.content.Intent>,
    onSessionEnded: () -> Unit
) {
    val viewModel: AppViewModel = viewModel(
        key = initialUser.id,
        factory = AppViewModelFactory(initialUser)
    )
    
    val context = LocalContext.current
    val activity = LocalActivity.current

    // Force Placement Reveal if required, regardless of the current navigation state.
    val effectiveScreen = if (viewModel.isPlacementRevealRequired) {
        Screen.placementReveal
    } else {
        viewModel.currentScreen
    }

    when(effectiveScreen) {
        Screen.home -> MainPagerScreen(
            user = viewModel.currentUser,
            initialPage = viewModel.pagerInitialPage,
            onNavigateToProfile = { viewModel.navigateTo(Screen.profile) },
            onNavigateToWriting = { gamemode, playMode ->
                viewModel.startWriting(gamemode, playMode)
            }
        )
        
        Screen.practice -> {
            viewModel.navigateTo(Screen.home, page = 0)
        }

        Screen.competitions -> {
            viewModel.navigateTo(Screen.home, page = 2)
        }

        Screen.writing -> Writing(
            gamemode = viewModel.currentGamemode ?: StandardWriting,
            playMode = viewModel.currentPlayMode,
            userId = viewModel.currentUser.id,
            isPersisting = viewModel.isPersistingSubmission,
            onAddSubmission = { submission, onPersisted, onFailure ->
                viewModel.submitWriting(
                    submission = submission,
                    onPersisted = onPersisted
                ) { error ->
                    onFailure()
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            },
            onNavigateBack = { viewModel.navigateTo(Screen.home, page = 1) },
            onNavigateToResults = { viewModel.navigateTo(Screen.results) }
        )

        Screen.submissions -> SubmissionsScreen(
            user = viewModel.currentUser,
            submissions = viewModel.allSubmissions,
            isLoading = viewModel.isLoadingSubmissions,
            onNavigateToProfile = { viewModel.navigateTo(Screen.profile) },
            onSaveSubmission = { submissionId ->
                viewModel.saveSubmission(submissionId) { error ->
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            }
        )

        Screen.savedSubmissions -> SavedSubmissionsScreen(
            savedSubmissions = viewModel.allSubmissions.filter { it.isSaved },
            isLoading = viewModel.isLoadingSubmissions,
            onNavigateBack = { viewModel.navigateTo(Screen.profile) },
            onDeleteSubmission = { submissionId ->
                viewModel.deleteSubmission(submissionId, {
                    Toast.makeText(context, "Entry Dissolved", Toast.LENGTH_SHORT).show()
                }) { error ->
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            }
        )

        Screen.profile -> Profile(
            user = viewModel.currentUser,
            isOwner = true,
            onNavigateBack = { viewModel.navigateTo(Screen.home, page = 1) },
            onNavigateToSubmissions = { viewModel.navigateTo(Screen.submissions) },
            onNavigateToSavedSubmissions = { viewModel.navigateTo(Screen.savedSubmissions) },
            onNavigateToSettings = { viewModel.navigateTo(Screen.settings) },
            onNavigateToSeasons = { viewModel.navigateTo(Screen.seasons) }
        )

        Screen.seasons -> SeasonsScreen(
            userId = viewModel.currentUser.id,
            onNavigateBack = { viewModel.navigateTo(Screen.profile) }
        )

        Screen.settings -> Settings(
            user = viewModel.currentUser,
            onNavigateBack = { viewModel.navigateTo(Screen.profile) },
            onLogout = {
                AuthManager.signOut()
                onSessionEnded()
            },
            onDeleteAccount = {
                viewModel.deleteAccount({
                    AuthManager.signOut()
                    onSessionEnded()
                }) { error ->
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            },
            onChangeUsername = { viewModel.navigateTo(Screen.usernameSetup) },
            onExpandCap = {
                viewModel.applyMeritAction(SystemConfig.ACTION_EXPAND_MERIT_CAP, {
                    Toast.makeText(context, "Cap Expanded", Toast.LENGTH_SHORT).show()
                }) { error ->
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            }
        )

        Screen.results -> {
            val res = viewModel.latestSubmission
            if (res == null) {
                LaunchedEffect(Unit) {
                    viewModel.loadLatestSubmission()
                }
                Box(
                    modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else {
                Results(
                    submission = res,
                    isPlaced = viewModel.currentUser.isPlaced,
                    onNavigateBack = {
                        viewModel.continueWithAd(activity, Screen.home) {
                            viewModel.pagerInitialPage = 1
                        }
                    },
                    onNavigateToPractice = {
                        viewModel.continueWithAd(activity, Screen.home) {
                            viewModel.pagerInitialPage = 0
                        }
                    }
                )
            }
        }

        Screen.loading -> LoadingScreen(
            elapsedSeconds = viewModel.loadingElapsedSeconds,
            isTimeout = viewModel.loadingTimeout,
            queryError = viewModel.loadingQueryError,
            onRetry = { viewModel.retryLoadingResult() },
            onReturnHome = { viewModel.navigateTo(Screen.home) }
        )

        Screen.postSubmissionAd -> PostSubmissionAdScreen(
            onContinue = {
                val next = viewModel.pendingNavigationAfterAd
                viewModel.pendingNavigationAfterAd = null
                viewModel.navigateTo(next ?: Screen.home)
            },
            onGoAdFree = { viewModel.navigateTo(Screen.paywall) }
        )

        Screen.paywall -> PaywallScreen(
            onBack = { viewModel.navigateTo(Screen.home, page = 1) },
            onSubscribe = {
                viewModel.enablePhilosopher({
                    Toast.makeText(context, "Status Elevated", Toast.LENGTH_SHORT).show()
                    viewModel.navigateTo(Screen.home, page = 1)
                }) { error ->
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            }
        )

        Screen.usernameSetup -> UsernameSetupScreen(
            isSaving = false,
            errorMessage = null,
            onSubmit = { newName ->
                viewModel.changeUsername(newName, {
                    viewModel.navigateTo(Screen.settings) // Return to settings after name change
                }) { error ->
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            },
            checkAvailability = { name, callback ->
                viewModel.checkUsernameAvailability(name, callback)
            },
            validateUsername = { name ->
                viewModel.validateUsername(name)
            }
        )

        Screen.placementReveal -> PlacementRevealScreen(
            rating = viewModel.currentUser.rating,
            onContinue = {
                viewModel.onPlacementRevealSeen { }
            }
        )
    }
}
