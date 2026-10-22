package com.dalawingo.app.ui

import android.speech.tts.TextToSpeech
import android.media.MediaPlayer
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import com.dalawingo.app.R
import androidx.compose.animation.AnimatedVisibility as ComposeAnimatedVisibility
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.foundation.Canvas
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.geometry.Offset as ScreenOffset
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import kotlin.math.sin
import kotlin.math.PI
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dalawingo.app.model.AnswerAttempt
import com.dalawingo.app.model.vocabularyKey
import com.dalawingo.app.model.Exercise
import com.dalawingo.app.model.ExerciseType
import com.dalawingo.app.model.pictureFor
import com.dalawingo.app.model.DailyQuest
import com.dalawingo.app.model.WordTile
import java.util.Locale

private fun normalized(text: String) = text.lowercase(Locale.ROOT)
    .replace(Regex("[^\\p{L}\\p{N}\\s]"), "")
    .replace(Regex("\\s+"), " ").trim()

@Composable
fun LessonScreen(
    title: String,
    exercises: List<Exercise>,
    practice: Boolean,
    roundNumber: Int = 1,
    onExit: () -> Unit,
    onFinished: (Int, Int, List<AnswerAttempt>) -> Unit
) {
    var current by remember { mutableIntStateOf(0) }
    var questionQueue by remember { mutableStateOf(exercises) }
    var requeuedWords by remember { mutableStateOf<Set<String>>(emptySet()) }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var typedText by remember { mutableStateOf("") }
    var selectedTileIds by remember { mutableStateOf<List<Int>>(emptyList()) }
    var checked by remember { mutableStateOf(false) }
    var answerIsCorrect by remember { mutableStateOf(false) }
    var wasSkipped by remember { mutableStateOf(false) }
    var correctCount by remember { mutableIntStateOf(0) }
    var skippedCount by remember { mutableIntStateOf(0) }
    var inARow by remember { mutableIntStateOf(0) }
    var answerHistory by remember { mutableStateOf<List<AnswerAttempt>>(emptyList()) }
    var showTranscript by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var ttsReady by remember { mutableStateOf(false) }
    // Remember a tapped audio tile even if Android TTS is still initializing.
    var pendingAudio by remember { mutableStateOf<Pair<String, Boolean>?>(null) }
    val tts = remember { TextToSpeech(context.applicationContext) { status ->
        ttsReady = status == TextToSpeech.SUCCESS
    } }
    fun listenTo(text: String, slower: Boolean = false) {
        if (!ttsReady) {
            pendingAudio = text to slower
            return
        }
        tts.setSpeechRate(if (slower) .65f else .88f)
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "dala_${current}")
    }
    DisposableEffect(tts) { onDispose { tts.stop(); tts.shutdown() } }
    LaunchedEffect(ttsReady) {
        if (ttsReady) {
            val supported = listOf(
                Locale.forLanguageTag("fil-PH"),
                Locale.forLanguageTag("tl-PH"),
                Locale.getDefault()
            ).firstOrNull { tts.setLanguage(it) >= TextToSpeech.LANG_AVAILABLE }
            if (supported == null) tts.setLanguage(Locale.US)
            tts.setSpeechRate(.88f)
            pendingAudio?.let { (text, slower) ->
                tts.setSpeechRate(if (slower) .65f else .88f)
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "dala_queued_${current}")
                pendingAudio = null
            }
        }
    }
    if (exercises.isEmpty()) {
        Column(Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text("No questions available yet.", color = Ink,
                fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(14.dp))
            DalaButton("BACK", onExit, Modifier.fillMaxWidth())
        }
        return
    }
    val exercise = questionQueue[current]
    val automatic = exercise.type in setOf(
        ExerciseType.SPEAK_TAGALOG, ExerciseType.SPEAK_SET,
        ExerciseType.SPEAK_FLASHCARDS, ExerciseType.MATCH_AUDIO,
        ExerciseType.MATCH_TEXT, ExerciseType.IMAGE_FLASHCARD
    )
    val response = when (exercise.type) {
        ExerciseType.WORD_BANK, ExerciseType.COMPLETE_SENTENCE -> selectedTileIds.mapNotNull { id ->
            exercise.tiles.find { it.id == id }?.text }.joinToString(" ")
        ExerciseType.TYPE_TAGALOG, ExerciseType.TYPE_HEARD -> typedText
        else -> selectedOption.orEmpty()
    }
    val canCheck = response.isNotBlank()
    val answer = exercise.correctAnswer

    fun completeAutomatic(success: Boolean, attempts: List<AnswerAttempt>, skipped: Boolean = false) {
        if (checked) return
        answerHistory = answerHistory + attempts.map { it.copy(exerciseType = exercise.type) }
        wasSkipped = skipped
        answerIsCorrect = success
        checked = true
        if (skipped) skippedCount++
        if (success) { correctCount += 1; inARow += 1 }
        else if (!skipped) inARow = 0
    }

    LaunchedEffect(checked, current) {
        if (checked && !wasSkipped) {
            val resource = if (answerIsCorrect) R.raw.sfx_correct else R.raw.sfx_wrong
            val player = runCatching { MediaPlayer.create(context.applicationContext, resource) }.getOrNull()
            player?.setOnCompletionListener { it.release() }
            runCatching { player?.start() }.onFailure { player?.release() }
        }
    }

    if (showExitDialog) {
        AlertDialog(onDismissRequest = { showExitDialog = false },
            shape = RoundedCornerShape(24.dp), containerColor = Color.White,
            title = { Text("Quit this lesson?", fontWeight = FontWeight.ExtraBold) },
            text = { Text("Your answers for this round won't be saved.") },
            confirmButton = { DalaOutlineButton("QUIT", onExit, Modifier.width(113.dp), Red) },
            dismissButton = { DalaOutlineButton("KEEP GOING", { showExitDialog = false }, Modifier.width(147.dp)) })
    }

    Column(Modifier.fillMaxSize().background(Color.White)) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(38.dp).clickable { showExitDialog = true },
                contentAlignment = Alignment.Center) {
                GameIcon(GameGlyph.CLOSE, Modifier.size(27.dp), Color(0xFFAAAAAA))
            }
            Spacer(Modifier.width(13.dp))
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                if (inARow >= 2) Text("ON A ROLL ×$inARow", color = LeafDark,
                    fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                ProgressLine((current + if (checked) 1 else 0).toFloat() / questionQueue.size,
                    if (inARow >= 3) Color(0xFFFFCA31) else Leaf,
                    Modifier.fillMaxWidth(), 13.dp)
            }
        }
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState())
            .padding(horizontal = 23.dp, vertical = 16.dp)) {
            Heading(when (exercise.type) {
                ExerciseType.CHOOSE_TAGALOG -> "Choose the translation"
                ExerciseType.CHOOSE_ENGLISH -> "What does this mean?"
                ExerciseType.WORD_BANK -> "Translate this sentence"
                ExerciseType.LISTEN -> "What do you hear?"
                ExerciseType.TYPE_TAGALOG -> "Type the translation"
                ExerciseType.TYPE_HEARD -> "Type what you hear"
                ExerciseType.COMPLETE_CHAT -> "Complete the chat"
                ExerciseType.PICTURE_MATCH -> "Choose the picture"
                ExerciseType.IMAGE_FLASHCARD -> "Say the word in Tagalog"
                ExerciseType.SPEAK_TAGALOG -> "Repeat after your friend"
                ExerciseType.STORY -> "What happens next?"
                ExerciseType.MATCH_AUDIO -> "Tap the matching pairs"
                ExerciseType.SPEAK_SET -> "Say the words in Tagalog"
                ExerciseType.SPEAK_FLASHCARDS -> "Say the words in Tagalog"
                ExerciseType.MATCH_TEXT -> "Tap the matching pairs"
                ExerciseType.FILL_BLANK -> "Fill in the blank"
                ExerciseType.COMPLETE_SENTENCE -> "Complete the sentence"
                ExerciseType.LISTEN_MISSING -> "Listen for the missing word"
            }, 24.sp)
            Spacer(Modifier.height(23.dp))
            when (exercise.type) {
                ExerciseType.MATCH_AUDIO, ExerciseType.MATCH_TEXT, ExerciseType.SPEAK_SET,
                ExerciseType.SPEAK_FLASHCARDS, ExerciseType.SPEAK_TAGALOG,
                ExerciseType.IMAGE_FLASHCARD -> Unit
                ExerciseType.LISTEN, ExerciseType.TYPE_HEARD -> {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        ) {
                        Box(Modifier.size(91.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFF168ACB))
                            .clickable { listenTo(exercise.phrase.tagalog) }
                            .padding(bottom = 5.dp), contentAlignment = Alignment.Center) {
                            Box(Modifier.fillMaxSize().clip(RoundedCornerShape(19.dp))
                                .background(Color(0xFF1CB0F6)), contentAlignment = Alignment.Center) {
                                DalaIcon(R.drawable.ic_speaker, "Listen", 52.dp, Color.White)
                            }
                        }
                        Spacer(Modifier.width(15.dp))
                        Box(Modifier.size(59.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .background(Color(0xFF168ACB))
                            .clickable { listenTo(exercise.phrase.tagalog, slower = true) }
                            .padding(bottom = 4.dp), contentAlignment = Alignment.Center) {
                            Box(Modifier.fillMaxSize().clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFF1CB0F6)), contentAlignment = Alignment.Center) {
                                DalaIcon(R.drawable.ic_slow, "Slow audio", 38.dp, Color.White)
                            }
                        }
                    }
                }
                ExerciseType.FILL_BLANK, ExerciseType.COMPLETE_SENTENCE,
                ExerciseType.LISTEN_MISSING -> {
                    Row(Modifier.fillMaxWidth().heightIn(min = 80.dp)
                        .clip(RoundedCornerShape(21.dp))
                        .border(2.dp, Border, RoundedCornerShape(21.dp))
                        .padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
                        if (exercise.type == ExerciseType.LISTEN_MISSING) {
                            Box(Modifier.clickable { listenTo(exercise.phrase.tagalog) }) {
                                DalaIcon(R.drawable.ic_speaker, "Play complete sentence", 30.dp, Sky)
                            }
                            Spacer(Modifier.width(10.dp))
                        }
                        Text(exercise.promptText, color = Ink, fontSize = 20.sp,
                            lineHeight = 29.sp)
                    }
                    if (exercise.type != ExerciseType.LISTEN_MISSING) {
                        Spacer(Modifier.height(10.dp))
                        Text(exercise.phrase.english, color = Muted, fontSize = 14.sp)
                    }
                }
                ExerciseType.COMPLETE_CHAT -> {
                    Column(Modifier.fillMaxWidth().heightIn(min = 214.dp),
                        verticalArrangement = Arrangement.spacedBy(17.dp)) {
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            TarsierMascot(Modifier.size(58.dp))
                            Spacer(Modifier.width(12.dp))
                            Row(Modifier.weight(1f)
                                .clip(RoundedCornerShape(21.dp))
                                .border(2.dp, Border, RoundedCornerShape(21.dp))
                                .clickable { listenTo(exercise.promptText) }
                                .padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
                                DalaIcon(R.drawable.ic_speaker, "Play message", 27.dp, Sky)
                                Spacer(Modifier.width(9.dp))
                                Text(exercise.promptText, color = Ink, fontSize = 18.sp,
                                    lineHeight = 24.sp)
                            }
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.widthIn(min = 107.dp).height(54.dp)
                                .clip(RoundedCornerShape(17.dp))
                                .border(2.dp, Border, RoundedCornerShape(17.dp)),
                                contentAlignment = Alignment.Center) {
                                Text("________", color = Muted, fontSize = 20.sp)
                            }
                            Spacer(Modifier.width(10.dp))
                            TarsierMascot(Modifier.size(49.dp))
                        }
                    }
                }
                ExerciseType.STORY -> {
                    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp))
                        .background(Pale).padding(17.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TarsierMascot(Modifier.size(55.dp))
                            Spacer(Modifier.width(9.dp))
                            Text("${exercise.speaker}  •  MINI STORY", color = LeafDark,
                                fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                        }
                        Spacer(Modifier.height(9.dp))
                        Text(exercise.scene, color = Ink,
                            fontSize = 19.sp, fontWeight = FontWeight.Bold)
                    }
                }
                ExerciseType.PICTURE_MATCH -> {
                    Text("${exercise.phrase.english}", fontSize = 23.sp,
                        fontWeight = FontWeight.ExtraBold, color = Ink,
                        modifier = Modifier.fillMaxWidth().padding(9.dp),
                        textAlign = TextAlign.Center)
                }
                else -> {
                    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp))
                        .background(Pale).padding(19.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        TarsierMascot(Modifier.size(64.dp))
                        Spacer(Modifier.width(14.dp))
                        Text(if (exercise.type == ExerciseType.CHOOSE_ENGLISH)
                            exercise.phrase.tagalog else exercise.phrase.english,
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.ExtraBold,
                            color = Ink, fontSize = 20.sp, lineHeight = 25.sp)
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
            when (exercise.type) {
                ExerciseType.MATCH_AUDIO -> AudioPairMatching(
                    phrases = exercise.pairs,
                    onSpeakAudio = { listenTo(it) },
                    onDone = { success, attempts -> completeAutomatic(success, attempts) },
                    onSkip = { completeAutomatic(false, emptyList(), skipped = true) }
                )
                ExerciseType.MATCH_TEXT -> WrittenPairMatching(
                    phrases = exercise.pairs,
                    onSpeakAudio = { listenTo(it) },
                    onDone = { success, attempts -> completeAutomatic(success, attempts) }
                )
                ExerciseType.SPEAK_SET, ExerciseType.SPEAK_FLASHCARDS -> SpeakingSequence(
                    phrases = exercise.pairs,
                    isCardStack = exercise.type == ExerciseType.SPEAK_FLASHCARDS,
                    onSpeakAudio = { listenTo(it) },
                    onDone = { success, attempts -> completeAutomatic(success, attempts) },
                    onSkip = { completeAutomatic(false, emptyList(), skipped = true) }
                )
                ExerciseType.SPEAK_TAGALOG -> SingleSpeakingExercise(
                    phrase = exercise.phrase,
                    onSpeakAudio = { listenTo(it) },
                    onSuccess = { completeAutomatic(true, listOf(AnswerAttempt(
                        vocabularyKey(exercise.phrase), true, exercise.type))) },
                    onMiss = { answerHistory = answerHistory + AnswerAttempt(
                        vocabularyKey(exercise.phrase), false, exercise.type) },
                    onSkip = { completeAutomatic(false, emptyList(), skipped = true) }
                )
                ExerciseType.IMAGE_FLASHCARD -> SpeakingSequence(
                    phrases = listOf(exercise.phrase),
                    isCardStack = true,
                    onSpeakAudio = { listenTo(it) },
                    onDone = { success, attempts -> completeAutomatic(success, attempts) },
                    onSkip = { completeAutomatic(false, emptyList(), skipped = true) }
                )
                ExerciseType.WORD_BANK, ExerciseType.COMPLETE_SENTENCE -> WordBank(
                    exercise.tiles, selectedTileIds, checked,
                    onSelect = { id ->
                        selectedTileIds = selectedTileIds + id
                        exercise.tiles.find { it.id == id }?.let { listenTo(it.text) }
                    },
                    onRemove = { id -> selectedTileIds = selectedTileIds - id }
                )
                ExerciseType.TYPE_TAGALOG, ExerciseType.TYPE_HEARD -> {
                    OutlinedTextField(
                        value = typedText, onValueChange = { typedText = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Type in Tagalog…") },
                        singleLine = false, maxLines = 3,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        enabled = !checked,
                        shape = RoundedCornerShape(18.dp)
                    )
                }
                ExerciseType.LISTEN_MISSING -> {
                    exercise.answers.forEachIndexed { i, option ->
                        ChoiceAudioTile(i, selectedOption == option,
                            checked && selectedOption == option,
                            checked && normalized(option) == normalized(answer),
                            onClick = {
                                selectedOption = option
                                listenTo(option)
                            })
                        Spacer(Modifier.height(10.dp))
                    }
                }
                ExerciseType.PICTURE_MATCH -> {
                    exercise.answers.chunked(2).forEach { pair ->
                        Row(Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(11.dp)) {
                            pair.forEach { option ->
                                PictureChoice(
                                    emoji = pictureFor(option) ?: "❔",
                                    name = option,
                                    selected = selectedOption == option,
                                    checked = checked,
                                    correct = normalized(option) == normalized(answer),
                                    modifier = Modifier.weight(1f),
                                    onClick = { selectedOption = option }
                                )
                            }
                            if (pair.size == 1) Spacer(Modifier.weight(1f))
                        }
                        Spacer(Modifier.height(11.dp))
                    }
                }
                else -> exercise.answers.forEach { option ->
                    ChoiceTile(
                        text = option,
                        selected = selectedOption == option,
                        checked = checked,
                        correctAnswer = normalized(option) == normalized(answer),
                        onClick = { selectedOption = option }
                    )
                    Spacer(Modifier.height(11.dp))
                }
            }
        }
        key(current) {
        AnimatedVisibility(visible = checked,
            enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(260)) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(180))) {
            Column(Modifier.fillMaxWidth().background(when {
                wasSkipped -> Color(0xFFFFF2CD)
                answerIsCorrect -> Color(0xFFE8F8D9)
                else -> Color(0xFFFFE6E4)
            }).padding(horizontal = 22.dp, vertical = 16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (wasSkipped) DalaIcon(R.drawable.ic_sparkle, null, 32.dp,
                        Color(0xFFE4A323))
                    else TarsierMascot(Modifier.size(39.dp), happy = answerIsCorrect)
                    Spacer(Modifier.width(10.dp))
                    Text(when {
                        wasSkipped -> "We'll skip this for now!"
                        answerIsCorrect -> listOf("Awesome!", "Excellent!", "Tama!", "Great job!")[current % 4]
                        else -> "Try the next one!"
                    }, fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = when {
                            wasSkipped -> Color(0xFFAC7A11)
                            answerIsCorrect -> LeafDark
                            else -> Red
                        })
                }
                if (!answerIsCorrect && !wasSkipped &&
                    exercise.type !in setOf(ExerciseType.MATCH_AUDIO,
                        ExerciseType.MATCH_TEXT, ExerciseType.SPEAK_SET,
                        ExerciseType.SPEAK_FLASHCARDS, ExerciseType.IMAGE_FLASHCARD,
                        ExerciseType.SPEAK_TAGALOG)) {
                    Spacer(Modifier.height(4.dp))
                    Text("Correct answer: $answer", color = Ink, fontSize = 14.sp)
                }
            }
        }
        }
        Column(Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 14.dp)) {
            if (!checked && !automatic) {
                DalaButton("CHECK", onClick = {
                    if (!canCheck) return@DalaButton
                    answerIsCorrect = normalized(response) == normalized(answer)
                    checked = true
                    answerHistory = answerHistory + AnswerAttempt(
                        vocabularyKey(exercise.phrase), answerIsCorrect, exercise.type
                    )
                    if (!answerIsCorrect) inARow = 0
                    if (answerIsCorrect) {
                        correctCount += 1
                        inARow += 1
                    } else if (practice || questionQueue.any {
                            it.type == ExerciseType.IMAGE_FLASHCARD
                        }) {
                        // Bring a missed word back once near the end of the round.
                        val id = vocabularyKey(exercise.phrase)
                        if (id !in requeuedWords) {
                            requeuedWords = requeuedWords + id
                            questionQueue = questionQueue + exercise
                        }
                    }
                }, Modifier.fillMaxWidth(), enabled = canCheck)

            } else if (checked) {
                DalaButton(
                    if (current == questionQueue.lastIndex) "FINISH" else "CONTINUE",
                    onClick = {
                        if (current == questionQueue.lastIndex) {
                            // A player choosing "can't listen/speak now" has not made a mistake.
                            // Exclude skipped questions from accuracy and perfect-lesson eligibility.
                            // If everything was skipped, total=0 prevents an undeserved perfect award.
                            onFinished(correctCount, questionQueue.size - skippedCount, answerHistory)
                        } else {
                            checked = false
                            selectedOption = null
                            typedText = ""
                            selectedTileIds = emptyList()
                            current += 1
                            answerIsCorrect = false
                            wasSkipped = false
                            showTranscript = false
                        }
                    },
                    Modifier.fillMaxWidth(),
                    color = if (wasSkipped) Color(0xFFE8B23B) else if (answerIsCorrect) Leaf else Red,
                    pressedColor = if (wasSkipped) Color(0xFFC39322) else if (answerIsCorrect) LeafDark else Color(0xFFD63838)
                )
            }
        }
    }
}

@Composable
private fun PictureChoice(
    emoji: String,
    name: String,
    selected: Boolean,
    checked: Boolean,
    correct: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val outline = when {
        checked && correct -> Leaf
        checked && selected -> Red
        selected -> Sky
        else -> Border
    }
    Column(modifier.height(128.dp)
        .clip(RoundedCornerShape(19.dp))
        .background(Color.White)
        .border(3.dp, outline, RoundedCornerShape(19.dp))
        .clickable(enabled = !checked, onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text(emoji, fontSize = 51.sp)
        if (checked) Text(name, fontSize = 12.sp,
            fontWeight = FontWeight.Bold, color = Ink, maxLines = 1)
    }
}

@Composable
private fun ChoiceAudioTile(
    number: Int, selected: Boolean, checked: Boolean, correct: Boolean,
    onClick: () -> Unit
) {
    val border = when {
        checked && correct -> Leaf
        checked -> Red
        selected -> Sky
        else -> Border
    }
    Row(Modifier.fillMaxWidth().height(63.dp).clip(RoundedCornerShape(18.dp))
        .background(if (selected) Sky.copy(alpha = .12f) else Color.White)
        .border(2.dp, border, RoundedCornerShape(18.dp)).clickable(onClick = onClick)
        .padding(12.dp), horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {
        DalaIcon(R.drawable.ic_speaker, "Listen option ${number + 1}", 28.dp,
            if (selected) SkyDark else Sky)
        Spacer(Modifier.width(12.dp))
        DalaIcon(R.drawable.ic_soundwave, null, 110.dp,
            if (selected) SkyDark else Sky)
    }
}

@Composable
private fun ChoiceTile(
    text: String,
    selected: Boolean,
    checked: Boolean,
    correctAnswer: Boolean,
    onClick: () -> Unit
) {
    val outline = when {
        checked && correctAnswer -> Leaf
        checked && selected -> Red
        selected -> Sky
        else -> Border
    }
    val fill = when {
        checked && correctAnswer -> Color(0xFFE9F9DC)
        checked && selected -> Color(0xFFFFE9E7)
        selected -> Color(0xFFE8F7FF)
        else -> Color.White
    }
    // One uniformly thick border. No second, offset backing layer / fake
    // drop shadow: the bottom edge is exactly as thick as the other edges.
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(fill)
            .border(2.dp, outline, RoundedCornerShape(15.dp))
            .clickable(enabled = !checked, onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = if (selected && !checked) SkyDark else Ink,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun WordBank(
    tiles: List<WordTile>, selectedIds: List<Int>, checked: Boolean,
    onSelect: (Int) -> Unit, onRemove: (Int) -> Unit
) {
    val originalSlots = remember(tiles) { mutableStateMapOf<Int, ScreenOffset>() }
    val destinationSlots = remember(tiles) { mutableStateMapOf<Int, ScreenOffset>() }
    val arrived = remember(tiles) { mutableStateMapOf<Int, Boolean>() }
    var origin by remember { mutableStateOf<ScreenOffset?>(null) }
    LaunchedEffect(selectedIds) {
        arrived.keys.filter { it !in selectedIds }.toList().forEach { arrived.remove(it) }
    }

    // The entire source+destination lives in one parent. Flying tiles are drawn
    // LAST in its topmost overlay, above BOTH answer lines and untouched cards.
    Box(Modifier.fillMaxWidth().zIndex(35f)
        .onGloballyPositioned { origin = it.positionInRoot() }) {
        Column(Modifier.fillMaxWidth()) {
            FlowRow(
                Modifier.fillMaxWidth().heightIn(min = 48.dp),
                horizontalArrangement = Arrangement.spacedBy(7.dp),
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                selectedIds.forEach { id ->
                    tiles.find { it.id == id }?.let { tile ->
                        Box(Modifier.onGloballyPositioned {
                            destinationSlots[id] = it.positionInRoot()
                        }) {
                            WordTileChip(tile.text, true, !checked,
                                { onRemove(tile.id) },
                                Modifier.graphicsLayer(alpha = if (arrived[id] == true) 1f else 0f))
                        }
                    }
                }
            }
            Spacer(Modifier.height(3.dp))
            Box(Modifier.fillMaxWidth().height(2.dp).background(Border))
            Spacer(Modifier.height(39.dp))
            Box(Modifier.fillMaxWidth().height(2.dp).background(Border))
            Spacer(Modifier.height(25.dp))
            FlowRow(
                Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                tiles.forEach { tile ->
                    Box(Modifier.onGloballyPositioned {
                        originalSlots[tile.id] = it.positionInRoot()
                    }) {
                        if (tile.id in selectedIds) {
                            Box(Modifier.clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFE7E7E7))
                                .padding(horizontal = 14.dp, vertical = 12.dp)) {
                                Text(tile.text, fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp, color = Color.Transparent)
                            }
                        } else {
                            WordTileChip(tile.text, false, !checked,
                                { onSelect(tile.id) })
                        }
                    }
                }
            }
        }

        // Unlike translating a destination child, this overlay is painted above
        // the ENTIRE word-bank including its source tray and sentence rules.
        Box(Modifier.matchParentSize().zIndex(100f), contentAlignment = Alignment.TopStart) {
            selectedIds.forEach { id ->
                if (arrived[id] != true) {
                    val tile = tiles.firstOrNull { it.id == id } ?: return@forEach
                    val source = originalSlots[id]
                    val destination = destinationSlots[id]
                    val parent = origin
                    if (source != null && destination != null && parent != null) {
                        key(id) {
                            val movement = remember { Animatable(0f) }
                            LaunchedEffect(id) {
                                movement.animateTo(1f,
                                    tween(350, easing = FastOutSlowInEasing))
                                arrived[id] = true
                            }
                            WordTileChip(
                                tile.text, true, false, {},
                                Modifier.graphicsLayer {
                                    translationX = source.x +
                                        (destination.x - source.x) * movement.value - parent.x
                                    translationY = source.y +
                                        (destination.y - source.y) * movement.value - parent.y
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WordTileChip(label: String, chosen: Boolean, enabled: Boolean,
    onClick: () -> Unit, modifier: Modifier = Modifier) {
    val jump by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (chosen && !enabled) 1.08f else 1f,
        animationSpec = tween(200), label = "Word tile confirmation")
    Box(modifier.graphicsLayer(scaleX = jump, scaleY = jump)
        .clip(RoundedCornerShape(12.dp))
        .background(if (chosen) Sky.copy(alpha = .11f) else Color.White)
        .border(2.dp, if (chosen) Sky else Border, RoundedCornerShape(12.dp))
        .clickable(enabled = enabled, onClick = onClick)
        .padding(horizontal = 14.dp, vertical = 12.dp)) {
        Text(label, color = if (chosen) SkyDark else Ink, fontWeight = FontWeight.Bold, fontSize = 15.sp)
    }
}

/** Celebration runs while the player is looking at the result and never modifies awards. */
@Composable
fun LessonResultScreen(
    title: String,
    correct: Int,
    total: Int,
    xp: Int,
    practice: Boolean,
    pesosEarned: Int = 0,
    roundCompleted: Int = 0,
    pathActivity: Boolean = false,
    earnedQuests: List<DailyQuest> = emptyList(),
    streakMilestone: Int = 0,
    secondsElapsed: Int = 0,
    onContinue: () -> Unit,
    onReview: () -> Unit
) {
    val perfect = total > 0 && correct == total
    val accuracy = if (total > 0) (correct * 100f / total).toInt() else 0
    val elapsed = "%d:%02d".format(secondsElapsed / 60, secondsElapsed % 60)
    val perfectLesson = perfect && !practice && !pathActivity && roundCompleted >= 4
    val flawlessRound = perfect && !practice && !pathActivity && roundCompleted in 1..3
    val heading = when {
        practice -> "Practice complete!"
        perfectLesson -> "Perfect lesson!"
        flawlessRound -> "Perfect round!"
        pathActivity -> "Challenge complete!"
        else -> "Lesson complete!"
    }
    val mascot = remember { Animatable(.25f) }
    val headingScale = remember { Animatable(.45f) }
    LaunchedEffect(Unit) {
        mascot.animateTo(1.20f, tween(480, easing = FastOutSlowInEasing))
        mascot.animateTo(1f, tween(210))
        headingScale.animateTo(1.12f, tween(250))
        headingScale.animateTo(1f, tween(210))
    }
    BoxWithConstraints(Modifier.fillMaxSize().background(Color.White)) {
        val compact = maxHeight < 715.dp
        ConfettiRain(Modifier.fillMaxSize())
        Column(Modifier.fillMaxSize().padding(horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.weight(if (compact) .08f else .32f))
            Box(Modifier.fillMaxWidth().height(if (compact) 165.dp else 220.dp),
                contentAlignment = Alignment.Center) {
                if (perfectLesson) {
                    val radiance = rememberInfiniteTransition(label = "Perfect gold celebration")
                    val spin by radiance.animateFloat(0f, 360f,
                        animationSpec = infiniteRepeatable(tween(4800), RepeatMode.Restart),
                        label = "Gold star rotation")
                    val shine by radiance.animateFloat(.42f, .94f,
                        animationSpec = infiniteRepeatable(tween(650), RepeatMode.Reverse),
                        label = "Gold star shimmer")
                    Canvas(Modifier.size(if (compact) 165.dp else 215.dp)
                        .graphicsLayer(rotationZ = spin)) {
                        val mid = Offset(size.width / 2f, size.height / 2f)
                        repeat(12) { i ->
                            val angle = (i * PI / 6.0)
                            val inner = size.minDimension * .34f
                            val outer = size.minDimension * .48f
                            val x0 = mid.x + kotlin.math.cos(angle).toFloat() * inner
                            val y0 = mid.y + kotlin.math.sin(angle).toFloat() * inner
                            val x1 = mid.x + kotlin.math.cos(angle).toFloat() * outer
                            val y1 = mid.y + kotlin.math.sin(angle).toFloat() * outer
                            drawLine(Color(0xFFFFC800).copy(alpha = shine),
                                Offset(x0,y0), Offset(x1,y1),
                                strokeWidth = 4.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                        }
                    }
                    DalaIcon(R.drawable.ic_star, null, 67.dp, Color(0xFFFFD026),
                        Modifier.align(Alignment.TopEnd).padding(end = 2.dp)
                            .graphicsLayer(rotationZ = -spin * .3f))
                }
                TarsierMascot(Modifier.size(if (compact) 148.dp else 191.dp)
                    .graphicsLayer(scaleX = mascot.value, scaleY = mascot.value))
                DalaIcon(R.drawable.ic_sparkle, null, 38.dp, Color(0xFFF9C73E),
                    Modifier.align(Alignment.TopStart).padding(start = 21.dp))
                DalaIcon(R.drawable.ic_star, null, 33.dp, Color(0xFF79CE28),
                    Modifier.align(Alignment.TopEnd).padding(end = 20.dp))
                DalaIcon(R.drawable.ic_sparkle, null, 30.dp, Sky,
                    Modifier.align(Alignment.BottomEnd).padding(end = 42.dp))
            }
            Spacer(Modifier.height(12.dp))
            Text(heading, color = if (perfectLesson) Color(0xFFE5AE00)
                else Color(0xFFFCC936),
                modifier = Modifier.graphicsLayer(scaleX = headingScale.value,
                    scaleY = headingScale.value),
                fontSize = if (compact) 29.sp else 34.sp,
                lineHeight = if (compact) 34.sp else 40.sp,
                fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center)
            if (perfectLesson) {
                Spacer(Modifier.height(7.dp))
                Text("Every answer correct!", color = Color(0xFFB28A00),
                    fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Spacer(Modifier.weight(if (compact) .14f else .48f))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CelebrationStatCard("TOTAL XP", R.drawable.ic_xp, "$xp",
                    Color(0xFFFFCC41), 0, Modifier.weight(1f))
                CelebrationStatCard(if (secondsElapsed < 240) "QUICK" else "COMMITTED",
                    R.drawable.ic_clock, elapsed, Color(0xFF51B0ED), 125, Modifier.weight(1f))
                CelebrationStatCard(if (perfect) "PERFECT!" else "ACCURACY",
                    R.drawable.ic_target, "$accuracy%", Color(0xFF79CA37), 250, Modifier.weight(1f))
            }
            if (pesosEarned > 0) {
                Spacer(Modifier.height(8.dp))
                CoinShower(pesosEarned)
            }
            if (streakMilestone > 0) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    DalaIcon(R.drawable.ic_fire, null, 23.dp, Tangerine)
                    Text("$streakMilestone day streak!", color = Tangerine,
                        fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                }
            }
            Spacer(Modifier.weight(if (compact) .12f else .65f))
            DalaButton("CONTINUE", onContinue, Modifier.fillMaxWidth(),
                color = Color(0xFF1CB0F6), pressedColor = Color(0xFF168ACA),
                textColor = Color.White)
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ConfettiRain(modifier: Modifier = Modifier) {
    val pulse = rememberInfiniteTransition(label = "Celebration confetti")
    val fall by pulse.animateFloat(0f, 1f,
        animationSpec = infiniteRepeatable(tween(3300), RepeatMode.Restart),
        label = "Confetti fall")
    val colors = listOf(Leaf, Sky, Color(0xFFFFC800), Purple, Color(0xFFED87A9))
    Canvas(modifier) {
        repeat(43) { i ->
            val x = (i * 137 % 101) / 101f * size.width
            val phase = ((i * 37 % 100) / 100f + fall) % 1f
            val drift = sin((phase * 6.28 + i).toDouble()).toFloat() * 13.dp.toPx()
            val y = phase * (size.height + 140.dp.toPx()) - 90.dp.toPx()
            drawLine(colors[i % colors.size],
                Offset(x + drift, y), Offset(x + drift + 6.dp.toPx(), y + 6.dp.toPx()),
                strokeWidth = 3.dp.toPx())
        }
    }
}

@Composable
private fun CoinShower(pesos: Int) {
    val fall = remember { Animatable(0f) }
    LaunchedEffect(pesos) {
        fall.snapTo(0f)
        fall.animateTo(1f, tween(1700, easing = FastOutSlowInEasing))
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        BoxWithConstraints(Modifier.fillMaxWidth().height(64.dp)) {
            val center = maxWidth / 2
            repeat(9) { i ->
                val x0 = maxWidth * ((i + .5f) / 9f)
                val t = fall.value
                val drop = (t * 2f).coerceIn(0f, 1f)
                val collect = ((t - .67f) * 3f).coerceIn(0f, 1f)
                val x = x0 + (center - x0) * collect
                val y = (-15 + (i % 3) * -16).dp + (64.dp * drop) - (33.dp * collect)
                DalaIcon(R.drawable.ic_peso_coin, null, 23.dp,
                    modifier = Modifier.offset(x = x - 12.dp, y = y)
                        .graphicsLayer(alpha = (1f - collect).coerceIn(0f, 1f),
                            rotationZ = i * 38f + t * 110f))
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.graphicsLayer(scaleX = .75f + fall.value * .25f,
                scaleY = .75f + fall.value * .25f)) {
            DalaIcon(R.drawable.ic_peso_coin, "Pesos earned", 37.dp)
            Spacer(Modifier.width(7.dp))
            Text("+$pesos Pesos", color = Color(0xFFBA8400),
                fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Composable
private fun CelebrationStatCard(
    label: String, icon: Int, value: String, color: Color,
    delayMillis: Long, modifier: Modifier = Modifier
) {
    val scale = remember { Animatable(.15f) }
    LaunchedEffect(Unit) {
        delay(delayMillis)
        scale.animateTo(1.10f, tween(300, easing = FastOutSlowInEasing))
        scale.animateTo(1f, tween(170))
    }
    Column(modifier.height(109.dp)
        .graphicsLayer(scaleX = scale.value, scaleY = scale.value)
        .clip(RoundedCornerShape(22.dp)).background(color).padding(3.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.height(27.dp), contentAlignment = Alignment.Center) {
            Text(label, color = Color.White, fontWeight = FontWeight.ExtraBold,
                fontSize = 11.sp, maxLines = 1)
        }
        Row(Modifier.fillMaxSize().clip(RoundedCornerShape(19.dp))
            .background(Color.White), horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            DalaIcon(icon, null, 20.dp, color)
            Spacer(Modifier.width(3.dp))
            Text(value, color = color, fontSize = 19.sp,
                fontWeight = FontWeight.ExtraBold, maxLines = 1)
        }
    }
}
