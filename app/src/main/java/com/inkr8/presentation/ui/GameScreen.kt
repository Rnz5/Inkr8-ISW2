package com.inkr8.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.inkr8.domain.model.*
import com.inkr8.R
import com.inkr8.domain.policy.GamePolicy
import com.inkr8.presentation.viewmodel.*

@Composable
fun GameScreen(viewModel: GameViewModel, mode: PlayMode, ranking: RankingViewModel? = null) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            ErrorMessage(state.error, viewModel::reconnect)
            if (state.busy || state.loadingRecent) LinearProgressIndicator(Modifier.fillMaxWidth())
            val game = state.game
            if (game == null) SectionCard {
                Text(if (mode == PlayMode.PRACTICE) "Práctica libre" else "Competitivo", style = MaterialTheme.typography.titleLarge)
                if (mode == PlayMode.RANKED) Text("La entrada consume ${GamePolicy.RANKED_COST} Merit al empezar, incluso si abandonas la partida.")
                WritingMode.entries.forEach { writingMode ->
                    Row {
                        RadioButton(selected = state.writingMode == writingMode, enabled = !state.busy && state.requestId == null, onClick = { viewModel.select(writingMode) })
                        Text(if (writingMode == WritingMode.STANDARD) "Estándar · 4 palabras" else "Sobre un tema · 2 palabras", modifier = Modifier.padding(top = 12.dp))
                    }
                }
                Button(onClick = viewModel::start, enabled = !state.busy) { Text(if (state.requestId == null) "Empezar" else "Reintentar entrada") }
            } else SectionCard {
                Text("${if (mode == PlayMode.RANKED) "Ranked" else "Práctica"} · ${game.seasonId}")
                Text("Palabras: ${game.challenge.words.joinToString { it.text }}")
                game.challenge.topic?.let { Text("Tema: $it") }
                game.challenge.theme?.let { Text("Categoría: $it") }
                when (game.status) {
                    GameStatus.DRAFT -> {
                        OutlinedTextField(value = state.text, onValueChange = viewModel::edit,
                            modifier = Modifier.fillMaxWidth().heightIn(min = 220.dp), label = { Text("Tu texto") }, enabled = !state.busy)
                        Text("${state.wordCount} / ${game.writingMode.minWords}–${game.writingMode.maxWords} palabras")
                        Button(onClick = viewModel::send, enabled = !state.busy && state.canSubmit) { Text("Enviar para evaluar") }
                    }
                    GameStatus.PENDING -> { CircularProgressIndicator(); Text("R8 está evaluando tu texto. Puedes regresar más tarde y consultar esta partida.") }
                    GameStatus.FAILED -> {
                        Text(game.error ?: "La evaluación falló.", color = MaterialTheme.colorScheme.error)
                        Button(onClick = viewModel::retryEvaluation, enabled = !state.busy) { Text("Reintentar evaluación sin otro costo") }
                    }
                    GameStatus.EVALUATED -> {
                        Image(painter = painterResource(R.drawable.r8pfp), contentDescription = "R8", modifier = Modifier.size(48.dp))
                        game.evaluation?.let { evaluation ->
                            Text("Puntuación: ${evaluation.score}", style = MaterialTheme.typography.headlineMedium)
                            Text(evaluation.feedback)
                            Text("+${evaluation.meritEarned} Merit")
                        }
                        game.match?.let { match -> Text("${match.outcome} · ${match.opponentName} (${match.opponentScore}) · Rating ${if (match.ratingChange >= 0) "+" else ""}${match.ratingChange}") }
                        if (game.matchStatus == MatchStatus.EXPIRED) Text("La temporada terminó antes del emparejamiento. La recompensa de Merit se conserva; esta partida no cambia el rating de la nueva temporada.")
                        else if (mode == PlayMode.RANKED && game.match == null) Text("Buscando rival. Después de 48 horas sin rival se usa la referencia R8 de 65 puntos.")
                    }
                }
                TextButton(onClick = viewModel::newGame, enabled = !state.busy) { Text("Otra partida") }
            }
        }
        if (ranking != null) item { SeasonRankingCard(ranking) }
        item { Row { Text("Partidas recientes", style = MaterialTheme.typography.titleMedium); TextButton(onClick = viewModel::refreshRecent) { Text("Actualizar") } } }
        items(state.recent, key = { it.id }) { game ->
            OutlinedButton(onClick = { viewModel.open(game) }, enabled = !state.busy, modifier = Modifier.fillMaxWidth()) {
                Text("${game.writingMode} · ${game.status} · ${game.evaluation?.score ?: "—"}")
            }
        }
        item { Spacer(Modifier.height(8.dp)) }
    }
}

@Composable
private fun SeasonRankingCard(viewModel: RankingViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    SectionCard {
        Text("Temporada · Ligas 1–6", style = MaterialTheme.typography.titleMedium)
        if (state.loading) LinearProgressIndicator(Modifier.fillMaxWidth())
        ErrorMessage(state.error, viewModel::refresh)
        state.ranking?.let { ranking ->
            Text(seasonDates(ranking.season))
            if (ranking.members.isEmpty()) Text("La temporada aún no tiene participantes.")
            ranking.members.forEachIndexed { index, member -> Text("${index + 1}. ${member.name} · Liga ${member.league} · ${member.rating}") }
        }
        TextButton(onClick = viewModel::refresh) { Text("Actualizar clasificación") }
    }
}
