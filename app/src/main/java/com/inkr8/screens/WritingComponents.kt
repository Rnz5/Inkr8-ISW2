package com.inkr8.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inkr8.data.Theme
import com.inkr8.data.Topic
import com.inkr8.data.Words

@Composable
fun DirectiveCard(
    theme: Theme,
    topic: Topic,
    onThemeClick: () -> Unit,
    onTopicClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.surface).border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Directive",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelSmall,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Black
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth().clickable { onThemeClick() }.padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Theme", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(theme.name, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                }
                Box(
                    modifier = Modifier.size(18.dp).border(1.dp, Color.DarkGray, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("i", color = Color.DarkGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.05f))
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth().clickable { onTopicClick() }.padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Topic", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(topic.name, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier.size(18.dp).border(1.dp, Color.DarkGray, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("i", color = Color.DarkGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun LexiconChip(
    word: Words,
    isUsed: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(if (isUsed) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.White.copy(alpha = 0.05f))
            .border(
                1.dp, 
                if (isUsed) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f) else Color.White.copy(alpha = 0.1f), 
                RoundedCornerShape(8.dp)
            ).clickable { onClick() }.padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Text(
            text = word.word,
            color = if (isUsed) MaterialTheme.colorScheme.primary else Color.LightGray,
            fontWeight = if (isUsed) FontWeight.Black else FontWeight.Medium,
            fontSize = 13.sp,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun WordInfoDialog(word: Words, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Column {
                Text(
                    text = word.word,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(text = word.type.lowercase(), style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Column {
                    Text("Definition", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(word.definition, color = Color.White, style = MaterialTheme.typography.bodyMedium)
                }
                Column {
                    Text("Example", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text("\"${word.sentence}\"", color = Color.LightGray, style = MaterialTheme.typography.bodyMedium)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Dismiss", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
fun ThemeInfoDialog(theme: Theme, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Text(
                text = theme.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Column {
                    Text("Directive", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(theme.description, color = Color.White, style = MaterialTheme.typography.bodyMedium)
                }
                Column {
                    Text("Complexity", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(theme.difficulty, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Dismiss", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
fun TopicInfoDialog(topic: Topic, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Text(
                text = topic.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Column {
                    Text("Specification", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(topic.description, color = Color.White, style = MaterialTheme.typography.bodyMedium)
                }
                Column {
                    Text("Complexity", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(topic.difficulty, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Dismiss", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    )
}

