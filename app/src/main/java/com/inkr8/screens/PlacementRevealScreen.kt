package com.inkr8.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inkr8.rating.League
import kotlinx.coroutines.delay

@Composable
fun PlacementRevealScreen(
    league: League,
    onContinue: () -> Unit
) {
    var showLeague by remember { mutableStateOf(false) }
    var showVerdict by remember { mutableStateOf(false) }
    var showButton by remember { mutableStateOf(false) }

    val leagueAlpha by animateFloatAsState(
        targetValue = if (showLeague) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "leagueAlpha"
    )

    val verdictAlpha by animateFloatAsState(
        targetValue = if (showVerdict) 1f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "verdictAlpha"
    )

    LaunchedEffect(Unit) {
        delay(600)
        showLeague = true
        delay(1200)
        showVerdict = true
        delay(900)
        showButton = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Calibration Complete",
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 3.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            if (showButton) {
                Button(
                    onClick = onContinue,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
                ) {
                    Text(
                        "Enter the System",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}
