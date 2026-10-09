package com.inkr8.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inkr8.data.*
import com.inkr8.AuthManager
import com.inkr8.evaluation.SubmissionFactory
import com.inkr8.evaluation.isWritingAdmitted
import com.inkr8.repository.WordRepository
import com.inkr8.ui.theme.Inkr8Theme
import kotlinx.coroutines.launch
import com.google.firebase.analytics.FirebaseAnalytics
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.analytics.logEvent
import com.inkr8.utils.ValidationUtils
import com.inkr8.utils.DraftManager
import kotlinx.coroutines.delay

private val writingWhitespacePattern = "\\s+".toRegex()
private val writingWordBoundaryPattern = Regex("\\W+")

@Composable
fun Writing(
    gamemode: Gamemode,
    playMode: PlayMode,
    tournamentContext: Tournament? = null,
    userId: String = AuthManager.currentUser()?.uid.orEmpty(),
    isPersisting: Boolean = false,
    onAddSubmission: (Submissions, () -> Unit, () -> Unit) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToResults: () -> Unit
) {
    val context = LocalContext.current
    val wordRepository = remember { WordRepository() }
    val analyticsContext = LocalContext.current
    val firebaseAnalytics = remember { FirebaseAnalytics.getInstance(analyticsContext) }

    val exerciseKey = remember(userId, gamemode, playMode, tournamentContext) {
        DraftManager.getExerciseKey(
            userId, if (gamemode is OnTopicWriting) "ON_TOPIC" else "STANDARD",
            when (playMode) { PlayMode.Practice -> "PRACTICE"; PlayMode.Ranked -> "RANKED"; is PlayMode.Tournament -> "TOURNAMENT" },
            tournamentContext?.id ?: (playMode as? PlayMode.Tournament)?.tournamentId,
            (gamemode as? OnTopicWriting)?.theme?.id, (gamemode as? OnTopicWriting)?.topic?.id
        )
    }
    // Existing IDs determine lifetime; refreshed descriptions/tournament metadata
    // must not reset an editor whose authenticated exercise is unchanged.
    val exerciseIdentity: Any = exerciseKey ?: listOf(gamemode, playMode, tournamentContext?.id)
    var selectedWords by remember(exerciseIdentity) {
        mutableStateOf(exerciseKey?.let { DraftManager.getExerciseWords(context, it) })
    }
    LaunchedEffect(exerciseIdentity) {
        if (selectedWords == null) {
            val words = when {
                playMode is PlayMode.Tournament && tournamentContext != null ->
                    wordRepository.getWordsByTexts(tournamentContext.requiredWords)
                else -> {
                    val required = gamemode.requiredWords ?: 0
                    if (required > 0) wordRepository.getRandomWords(required.toLong()) else emptyList()
                }
            }
            exerciseKey?.let { DraftManager.saveExerciseWords(context, it, words) }
            selectedWords = words
        }
    }
    LaunchedEffect(gamemode, playMode) {
        firebaseAnalytics.logEvent("writing_started") {
            param("gamemode", when (gamemode) {
                is StandardWriting -> "STANDARD"
                is OnTopicWriting -> "ON_TOPIC"
            })
            param("playmode", when (playMode) {
                PlayMode.Practice -> "PRACTICE"
                PlayMode.Ranked -> "RANKED"
                is PlayMode.Tournament -> "TOURNAMENT"
            })
        }
    }

    val words = selectedWords
    if (words == null) {
        InitialLoadingScreen()
        return
    }
    val draftKey = exerciseKey?.let { DraftManager.getScopedDraftKey(it, words) }
    key(exerciseIdentity, draftKey) {
        WritingEditor(gamemode, playMode, words, draftKey, exerciseKey, isPersisting,
            onAddSubmission, onNavigateBack)
    }
}

@Composable
private fun WritingEditor(
    gamemode: Gamemode, playMode: PlayMode, selectedWords: List<Words>,
    draftKey: String?, exerciseKey: String?, isPersisting: Boolean,
    onAddSubmission: (Submissions, () -> Unit, () -> Unit) -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val firebaseAnalytics = remember { FirebaseAnalytics.getInstance(context) }
    var selectedWordForDialog by remember { mutableStateOf<Words?>(null) }
    var selectedThemeForDialog by remember { mutableStateOf<Theme?>(null) }
    var selectedTopicForDialog by remember { mutableStateOf<Topic?>(null) }
    var userText by remember { mutableStateOf(draftKey?.let { DraftManager.getDraft(context, it) }.orEmpty()) }
    var revision by remember { mutableLongStateOf(draftKey?.let { DraftManager.getRevision(context, it) } ?: 0L) }
    var pendingId by remember { mutableStateOf<String?>(null) }

    fun saveCurrentRevision() {
        draftKey?.let {
            if (DraftManager.getRevision(context, it) == revision &&
                DraftManager.getConfirmedRevision(context, it) != revision) DraftManager.saveDraft(context, it, userText)
        }
    }
    LaunchedEffect(userText) {
        if (userText.isNotBlank()) {
            delay(3000) // Preserve normal debounce; pending edits and leaving flush immediately.
            saveCurrentRevision()
        }
    }
    DisposableEffect(draftKey) {
        val stopObserving = draftKey?.let { key ->
            DraftManager.observeConfirmation(context, key) { confirmed ->
                if (revision == confirmed && DraftManager.getRevision(context, key) == confirmed) {
                    userText = ""
                    pendingId = null
                }
            }
        }
        onDispose {
            stopObserving?.invoke()
            if (userText.isNotBlank()) saveCurrentRevision()
        }
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val wordCount by remember {
        derivedStateOf {
            if (userText.isBlank()) 0 else userText.trim().split(writingWhitespacePattern).size
        }
    }

    val normalizedUserWords by remember {
        derivedStateOf {
            userText.lowercase().split(writingWordBoundaryPattern).filter { it.isNotBlank() }.toSet()
        }
    }

    val canSubmit by remember {
        derivedStateOf {
            isWritingAdmitted(userText, gamemode) { wordCount }
        }
    }

    selectedWordForDialog?.let { word ->
        WordInfoDialog(word = word, onDismiss = { selectedWordForDialog = null })
    }

    selectedThemeForDialog?.let { theme ->
        ThemeInfoDialog(theme = theme, onDismiss = { selectedThemeForDialog = null })
    }

    selectedTopicForDialog?.let { topic ->
        TopicInfoDialog(topic = topic, onDismiss = { selectedTopicForDialog = null })
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).statusBarsPadding().navigationBarsPadding().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        firebaseAnalytics.logEvent("writing_abandoned") {
                            param("reason", "back_pressed")
                        }
                        onNavigateBack()
                    },
                    modifier = Modifier.background(Color.White.copy(alpha = 0.05f), CircleShape)
                ) {
                    Text("←", color = Color.White, fontWeight = FontWeight.Bold)
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    val modeTitle = when(playMode) {
                        is PlayMode.Practice -> "PRACTICE"
                        is PlayMode.Ranked -> "RANKED ARENA"
                        is PlayMode.Tournament -> "TOURNAMENT"
                    }
                    Text(
                        text = modeTitle,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelSmall,
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "IDENTITY VERIFIED",
                        color = Color.Gray,
                        fontSize = 9.sp,
                        letterSpacing = 1.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (gamemode is OnTopicWriting) {
                DirectiveCard(
                    theme = gamemode.theme,
                    topic = gamemode.topic,
                    onThemeClick = { selectedThemeForDialog = gamemode.theme },
                    onTopicClick = { selectedTopicForDialog = gamemode.topic }
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.surface).border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp)).padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = "Standard Writing",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelSmall,
                            letterSpacing = 1.5.sp,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Construct a superior linguistic entry within standard parameters.",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (selectedWords.isNotEmpty()) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Required Words",
                        color = Color.Gray,
                        style = MaterialTheme.typography.labelSmall,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                    )
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(selectedWords) { word ->
                            val isUsed = normalizedUserWords.contains(word.word.lowercase())
                            LexiconChip(
                                word = word,
                                isUsed = isUsed,
                                onClick = { selectedWordForDialog = word }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier.weight(1f).fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Color.White.copy(alpha = 0.02f)).border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
            ) {
                TextField(
                    value = userText,
                    onValueChange = {
                        if (it != userText) {
                            userText = it
                            revision = draftKey?.let { key -> DraftManager.recordRevision(context, key) } ?: (revision + 1L)
                            if (pendingId != null || isPersisting || it.isBlank()) saveCurrentRevision()
                        }
                    },
                    placeholder = { 
                        Text(
                            "Start writing...",
                            color = Color.DarkGray,
                            style = MaterialTheme.typography.bodyLarge
                        ) 
                    },
                    modifier = Modifier.fillMaxSize(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent
                    ),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(lineHeight = 24.sp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    val min = gamemode.minWords ?: 0
                    val max = gamemode.maxWords ?: 1000
                    val isError = (wordCount < min || wordCount > max) && userText.isNotEmpty()
                    Text(
                        text = "Words: $wordCount",
                        color = if (isError) MaterialTheme.colorScheme.error else Color.Gray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "Constraint: $min - $max",
                        color = Color.DarkGray,
                        fontSize = 10.sp
                    )
                }
                
                Button(
                    onClick = {
                        if (canSubmit && pendingId == null && !isPersisting) {
                            val qualityCheck = ValidationUtils.isContentLowQuality(userText)
                            if (qualityCheck.first) {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = qualityCheck.second ?: "Low quality submission detected.",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                                return@Button
                            }

                            val submission = SubmissionFactory.create(
                                content = userText,
                                gamemode = when (gamemode) {
                                    is StandardWriting -> "STANDARD"
                                    is OnTopicWriting -> "ON_TOPIC"
                                },
                                playMode = when (playMode) {
                                    PlayMode.Practice -> "PRACTICE"
                                    PlayMode.Ranked -> "RANKED"
                                    is PlayMode.Tournament -> "TOURNAMENT"
                                },
                                wordsUsed = selectedWords.filter {
                                    normalizedUserWords.contains(it.word.lowercase())
                                },
                                topicId = if (gamemode is OnTopicWriting) gamemode.topic.id else null,
                                themeId = if (gamemode is OnTopicWriting) gamemode.theme.id else null,
                            )
                            
                            val sentRevision = revision
                            val sentText = userText
                            pendingId = submission.id
                            saveCurrentRevision()
                            onAddSubmission(submission, {
                                if (pendingId == submission.id) {
                                    pendingId = null
                                    val storedRevision = draftKey?.let { DraftManager.getRevision(context, it) } ?: revision
                                    if (revision == sentRevision && storedRevision == sentRevision && userText == sentText) {
                                        draftKey?.let { DraftManager.confirmRevision(context, it, sentRevision) }
                                        userText = ""
                                        exerciseKey?.let { DraftManager.releaseExercise(context, it) }
                                    } else {
                                        saveCurrentRevision()
                                    }
                                }
                            }, {
                                if (pendingId == submission.id) {
                                    pendingId = null
                                    saveCurrentRevision()
                                }
                            })
                        }
                    },
                    enabled = canSubmit && pendingId == null && !isPersisting,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (canSubmit) Color.White else Color.White.copy(alpha = 0.1f),
                        contentColor = Color.Black,
                        disabledContainerColor = Color.White.copy(alpha = 0.05f),
                        disabledContentColor = Color.Gray
                    ),
                    modifier = Modifier.height(48.dp).width(150.dp)
                ) {
                    Text(
                        text = if (canSubmit) "Submit" else "Incomplete",
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WritingPreview() {
    Inkr8Theme {
        Writing(
            gamemode = StandardWriting,
            playMode = PlayMode.Practice,
            onAddSubmission = { _, _, _ -> },
            onNavigateBack = {},
            onNavigateToResults = {}
        )
    }
}
