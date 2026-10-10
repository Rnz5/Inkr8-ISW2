package com.inkr8.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.inkr8.presentation.viewmodel.HomeViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel, practice: () -> Unit, ranked: () -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        if (state.loading) LinearProgressIndicator(Modifier.fillMaxWidth())
        ErrorMessage(state.error, viewModel::refresh)
        state.home?.let { home ->
            SectionCard {
                Text("Palabra del día", style = MaterialTheme.typography.titleMedium)
                Text(home.word.text, style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
                Text(home.word.definition)
                if (home.word.sentence.isNotBlank()) Text(home.word.sentence)
            }
            SectionCard {
                Text("Temporada ${home.season.id}", style = MaterialTheme.typography.titleMedium)
                Text(seasonDates(home.season))
                Text("El rating de todos vuelve a 0 cada 14 días. Tu Merit se conserva.")
                Button(onClick = practice) { Text("Practicar") }
                OutlinedButton(onClick = ranked) { Text("Ranked · ${home.rankedCost} Merit por entrada") }
            }
        }
    }
}
