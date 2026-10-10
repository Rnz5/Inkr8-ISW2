package com.inkr8.presentation.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.inkr8.domain.model.*
import com.inkr8.presentation.state.Destination
import com.inkr8.presentation.viewmodel.*

@Composable
fun MainScreen(user: User, sessionError: String?, refreshSession: () -> Unit, logout: () -> Unit,
    home: HomeViewModel, profile: ProfileViewModel, ranking: RankingViewModel, practice: GameViewModel, ranked: GameViewModel) {
    var destination by rememberSaveable { mutableStateOf(Destination.HOME) }
    BackHandler(destination != Destination.HOME) { destination = Destination.HOME }
    LaunchedEffect(destination) {
        when (destination) {
            Destination.HOME -> home.refresh()
            Destination.RANKED -> { ranking.refresh(); ranked.refreshRecent() }
            Destination.PRACTICE -> practice.refreshRecent()
            Destination.PROFILE -> Unit
        }
    }
    Scaffold(bottomBar = {
        NavigationBar { Destination.entries.forEach { entry ->
            NavigationBarItem(selected = destination == entry, onClick = { destination = entry },
                icon = { Text(when (entry) { Destination.HOME -> "⌂"; Destination.PRACTICE -> "✎"; Destination.RANKED -> "6"; Destination.PROFILE -> "●" }) }, label = { Text(entry.title) })
        } }
    }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp)) {
            Text("Inkr8 · ${destination.title}", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(vertical = 16.dp))
            Text("${user.name} · ${user.merit} Merit · Liga ${user.league} · Rating ${user.rating}")
            ErrorMessage(sessionError, refreshSession)
            Spacer(Modifier.height(16.dp))
            when (destination) {
                Destination.HOME -> HomeScreen(home, { destination = Destination.PRACTICE }, { destination = Destination.RANKED })
                Destination.PRACTICE -> GameScreen(practice, PlayMode.PRACTICE)
                Destination.RANKED -> GameScreen(ranked, PlayMode.RANKED, ranking)
                Destination.PROFILE -> ProfileScreen(user, profile, logout)
            }
        }
    }
}
