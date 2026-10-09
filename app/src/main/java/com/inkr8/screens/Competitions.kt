package com.inkr8.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.inkr8.data.*
import com.inkr8.economy.EconomyConfig
import com.inkr8.economy.RankedCostCalculator
import com.inkr8.repository.FirestoreSubmissionRepository
import com.inkr8.repository.ThemeRepository
import com.inkr8.repository.TopicRepository
import com.inkr8.repository.UserRepository
import com.inkr8.ui.theme.Inkr8Theme
import com.inkr8.utils.SystemConfig
import com.inkr8.utils.UserHeaderCard
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun Competitions(
    user: Users,
    onNavigateBack: () -> Unit,
    onNavigateToWriting: (Gamemode) -> Unit,
    onNavigateToProfile: () -> Unit,
) {
    val themeRepository = remember { ThemeRepository() }
    val topicRepository = remember { TopicRepository() }
    val submissionRepository = remember { FirestoreSubmissionRepository() }
    val userRepository = remember { UserRepository() }
    val context = LocalContext.current
    val firebaseAnalytics = remember { FirebaseAnalytics.getInstance(context) }

    var selectedRankedMode by remember(user.id) { mutableStateOf("STANDARD") }
    val scope = rememberCoroutineScope()
    var rankedEntryError by remember(user.id) { mutableStateOf<String?>(null) }
    var recentRankedSubmissions by remember { mutableStateOf<List<Submissions>>(emptyList()) }
    var isEnteringRanked by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        val registration = submissionRepository.listenToRecentRankedSubmissions(
            onUpdate = { recentRankedSubmissions = it },
            onError = { it.printStackTrace() }
        )
        onDispose { registration?.remove() }
    }

    val entryCost = RankedCostCalculator.calculateCost(
        EconomyConfig.BASE_COST_RANKED,
        user.rankedWinStreak,
        user.rankedLossStreak,
        user.reputation
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                UserHeaderCard(
                    user = user,
                    onClick = onNavigateToProfile
                )
            }

            item {
                Column {
                    Text(
                        text = "Competitive Module",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelSmall,
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier.size(24.dp).border(1.dp, Color.Gray, CircleShape).clickable { },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("i", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "Ranked Arena",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White,
                                        letterSpacing = 0.5.sp
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(selected = selectedRankedMode == "STANDARD", enabled = !isEnteringRanked,
                                    onClick = { selectedRankedMode = "STANDARD" })
                                Text("Standard")
                                RadioButton(selected = selectedRankedMode == "ON_TOPIC", enabled = !isEnteringRanked,
                                    onClick = { selectedRankedMode = "ON_TOPIC" })
                                Text("On-Topic")
                            }

                            rankedEntryError?.let { Text(it, color = MaterialTheme.colorScheme.error) }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    if (user.isPlaced) {
                                        Text(
                                            text = "Rating: ${user.rating}",
                                            color = Color.Gray,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    } else {
                                        Text(
                                            text = "Rank Calibration",
                                            color = Color.Gray,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 12.sp,
                                            letterSpacing = 1.sp
                                        )
                                        Text(
                                            text = "Phase: ${user.placementMatchesPlayed}/6",
                                            color = MaterialTheme.colorScheme.primary,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                Button(
                                    onClick = {
                                        if (isEnteringRanked) return@Button
                                        firebaseAnalytics.logEvent("ranked_entry_attempted", null)
                                        if (user.merit < entryCost) {
                                            Toast.makeText(context, EconomyConfig.insufficientMerit(), Toast.LENGTH_SHORT).show()
                                            return@Button
                                        }

                                        rankedEntryError = null
                                        isEnteringRanked = true
                                        scope.launch(Dispatchers.Main.immediate) {
                                            val gamemode: Gamemode = try {
                                                if (selectedRankedMode == "ON_TOPIC") {
                                                    val theme = themeRepository.getRandomTheme()
                                                    val topic = theme?.let { topicRepository.getRandomTopicFromTheme(it.id) }
                                                    if (theme == null || topic == null) {
                                                        throw IllegalStateException("Missing On-Topic context")
                                                    }
                                                    OnTopicWriting(theme, topic)
                                                } else StandardWriting
                                            } catch (e: CancellationException) {
                                                isEnteringRanked = false
                                                throw e
                                            } catch (_: Exception) {
                                                isEnteringRanked = false
                                                rankedEntryError = "No se pudo iniciar la partida On-Topic. Inténtalo nuevamente"
                                                Toast.makeText(context, rankedEntryError, Toast.LENGTH_SHORT).show()
                                                return@launch
                                            }
                                            userRepository.applyMeritAction(
                                                action = "ENTER_RANKED",
                                                onSuccess = {
                                                    isEnteringRanked = false
                                                    firebaseAnalytics.logEvent("ranked_entry_success", null)
                                                    onNavigateToWriting(gamemode)
                                                },
                                                onError = { e ->
                                                    isEnteringRanked = false
                                                    Toast.makeText(context, e.message ?: "Access Denied", Toast.LENGTH_SHORT).show()
                                                }
                                            )
                                        }
                                    },
                                    enabled = !isEnteringRanked,
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                                    modifier = Modifier.height(40.dp)
                                ) {
                                    Text(
                                        text = if (isEnteringRanked) "Decrypting..." else "Enter • $entryCost",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            if (!user.isPlaced) {
                                Spacer(modifier = Modifier.height(12.dp))
                                LinearProgressIndicator(
                                    progress = { user.placementMatchesPlayed.toFloat() / 6f },
                                    modifier = Modifier.fillMaxWidth().height(2.dp).clip(CircleShape),
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                    trackColor = Color.White.copy(alpha = 0.05f)
                                )
                            }

                            if (recentRankedSubmissions.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(16.dp))
                                HorizontalDivider(color = Color.White.copy(alpha = 0.05f))
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Recent Matches",
                                    color = Color.Gray,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 2.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                recentRankedSubmissions.take(5).forEach { submission ->
                                    val statusColor = when (submission.matchStatus) {
                                        "MATCHED" -> when (submission.matchResult?.outcome) {
                                            "WIN" -> MaterialTheme.colorScheme.primary
                                            "LOSS" -> MaterialTheme.colorScheme.error
                                            else -> Color.Gray
                                        }
                                        "GHOST" -> Color(0xFF9E9E9E)
                                        "PENDING" -> MaterialTheme.colorScheme.primary
                                        else -> Color.DarkGray
                                    }
                                    val statusLabel = when (submission.matchStatus) {
                                        "MATCHED" -> when (submission.matchResult?.outcome) {
                                            "WIN" -> "Won vs ${submission.matchResult.opponentName}"
                                            "LOSS" -> "Lost vs ${submission.matchResult.opponentName}"
                                            "DRAW" -> "Draw vs ${submission.matchResult.opponentName}"
                                            else -> "Matched"
                                        }
                                        "GHOST" -> "Unmatched — resolved"
                                        "PENDING" -> "Awaiting opponent..."
                                        else -> "Unknown"
                                    }
                                    val ratingText = when (submission.matchStatus) {
                                        "MATCHED", "GHOST" -> {
                                            val change = submission.matchResult?.ratingChange ?: 0L
                                            val sign = if (change >= 0) "+" else ""
                                            "$sign$change"
                                        }
                                        else -> ""
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier.size(6.dp).clip(CircleShape).background(statusColor)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = statusLabel,
                                                color = Color.LightGray,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Medium
                                             )
                                        }
                                        if (ratingText.isNotEmpty()) {
                                            Text(
                                                text = ratingText,
                                                color = statusColor,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Black
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onNavigateBack,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
            ) {
                Text("Return", fontWeight = FontWeight.Bold)
            }

            Text(
                text = SystemConfig.APP_VERSION,
                color = Color.DarkGray,
                fontSize = 8.sp,
                letterSpacing = 1.sp
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CompetitionsPreview() {
    val fakeUser = Users(id = "1", name = "MintCake", rating = 850, merit = 5000)
    Inkr8Theme {
        Competitions(
            user = fakeUser,
            onNavigateBack = {},
            onNavigateToWriting = {},
            onNavigateToProfile = {}
        )
    }
}
