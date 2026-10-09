package com.inkr8.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.inkr8.data.*
import com.inkr8.repository.SeasonRepository
import kotlinx.coroutines.CancellationException

@Composable
fun SeasonsScreen(userId: String, onNavigateBack: () -> Unit) {
    val repository = remember { SeasonRepository() }
    var ranking by remember(userId) { mutableStateOf<SeasonRanking?>(null) }
    var history by remember(userId) { mutableStateOf<List<SeasonHistoryEntry>>(emptyList()) }
    var rankingError by remember(userId) { mutableStateOf<String?>(null) }
    var historyError by remember(userId) { mutableStateOf<String?>(null) }
    var selected by remember(userId) { mutableStateOf<SeasonHistoryEntry?>(null) }
    var generation by remember { mutableIntStateOf(0) }
    LaunchedEffect(userId, generation) {
        rankingError = null; historyError = null
        try { ranking = repository.ranking() }
        catch (e: CancellationException) { throw e }
        catch (_: Exception) { rankingError = "No se pudo cargar el ranking de la temporada" }
        try { history = repository.history() }
        catch (e: CancellationException) { throw e }
        catch (_: Exception) { historyError = "No se pudo cargar el historial de temporadas" }
    }
    Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().padding(16.dp)) {
        Text("Temporadas", style = MaterialTheme.typography.headlineMedium)
        LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                Text("Temporada actual: ${ranking?.id ?: "…"}")
                rankingError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                if (ranking != null && ranking!!.members.isEmpty()) Text("Sin participantes")
                if (ranking != null && ranking!!.members.none { it.userId == userId }) Text("Aún no participas en esta temporada")
            }
            items(ranking?.members ?: emptyList(), key = { it.userId }) { member ->
                Card(colors = CardDefaults.cardColors(containerColor = if (member.userId == userId)
                    MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)) {
                    Text("${member.position}. ${member.name}${if (member.userId == userId) " (Tú)" else ""} — Rating: ${member.rating}", Modifier.padding(12.dp))
                }
            }
            item {
                Text("Historial de temporadas", style = MaterialTheme.typography.titleMedium)
                historyError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                if (history.isEmpty() && historyError == null) Text("Sin temporadas finalizadas en las que hayas participado")
            }
            items(history, key = { it.id }) { entry ->
                OutlinedButton(onClick = { selected = entry }) { Text(entry.id) }
            }
            selected?.let { entry -> item {
                Text("Temporada ${entry.id}")
                Text("Posición final: ${entry.position}")
                Text("Rating de cierre: ${entry.rating}")
                Text("Merit ganado: ${entry.meritEarned}")
            } }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = onNavigateBack) { Text("Volver") }
            Button(onClick = { generation++ }) { Text("Actualizar") }
        }
    }
}
