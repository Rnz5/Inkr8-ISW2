package com.inkr8.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inkr8.data.Evaluation
import com.inkr8.utils.FormatUtils

@Composable
internal fun ResultsScoreSummary(evaluation: Evaluation) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = FormatUtils.formatPercentage(evaluation.finalScore),
            fontSize = 76.sp,
            color = Color.White,
            fontWeight = FontWeight.Black,
            letterSpacing = (-3).sp
        )

        val appraisal = when {
            evaluation.finalScore >= 95 -> "GOD TIER. RARE PRECISION."
            evaluation.finalScore >= 90 -> "ELITE. SYSTEM ACKNOWLEDGED."
            evaluation.finalScore >= 80 -> "STRONG. ALMOST REFINED."
            evaluation.finalScore >= 70 -> "COMPETENT. STILL SAFE."
            evaluation.finalScore >= 60 -> "FINE. COMMON OUTPUT."
            else -> "WEAK. REWORK EVERYTHING."
        }

        Text(
            text = appraisal,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelMedium,
            letterSpacing = 1.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
internal fun ResultsFeedbackCard(evaluation: Evaluation, feedbackToShow: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (evaluation.isMock) "Mock Breakdown" else "R8 Breakdown",
                    color = Color.Gray,
                    style = MaterialTheme.typography.labelSmall,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = feedbackToShow,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 26.sp
            )
        }
    }
}
