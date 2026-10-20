package com.dalawingo.app.ui

import android.speech.tts.TextToSpeech
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.graphicsLayer
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
    var correctCount by remember { mutableIntStateOf(0) }
    var answerHistory by remember { mutableStateOf<List<AnswerAttempt>>(emptyList()) }
    var showTranscript by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var ttsReady by remember { mutableStateOf(false) }
    val tts = remember { TextToSpeech(context.applicationContext) { status ->
        ttsReady = status == TextToSpeech.SUCCESS
    } }
    fun listenTo(text: String, slower: Boolean = false) {
        if (ttsReady) {
            tts.setSpeechRate(if (slower) .65f else .88f)
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "dala_${current}")
        }
    }
    DisposableEffect(tts) { onDispose { tts.stop(); tts.shutdown() } }
    LaunchedEffect(ttsReady) {
        if (ttsReady) {
            if (tts.setLanguage(Locale.forLanguageTag("fil-PH")) < TextToSpeech.LANG_AVAILABLE) {
                tts.setLanguage(Locale.forLanguageTag("tl-PH"))
            }
            tts.setSpeechRate(.88f)
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
        ExerciseType.IMAGE_FLASHCARD, ExerciseType.TYPE_TAGALOG
    )
    val response = when (exercise.type) {
        ExerciseType.WORD_BANK -> selectedTileIds.mapNotNull { id ->
            exercise.tiles.find { it.id == id }?.text }.joinToString(" ")
        else -> selectedOption.orEmpty()
    }
    val canCheck = response.isNotBlank()
    val answer = exercise.correctAnswer

    fun completeAutomatic(success: Boolean, attempts: List<AnswerAttempt>) {
        if (checked) return
        answerHistory = answerHistory + attempts
        answerIsCorrect = success
        checked = true
        if (success) correctCount += 1
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
            RaisedIconButton(GameGlyph.CLOSE, "Quit lesson", { showExitDialog = true },
                size = 41.dp)
            Spacer(Modifier.width(13.dp))
            ProgressLine((current + if (checked) 1 else 0).toFloat() / questionQueue.size,
                Leaf, Modifier.weight(1f), 13.dp)
        }
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState())
            .padding(horizontal = 23.dp, vertical = 16.dp)) {
            Eyebrow(if (practice) "DAILY PRACTICE" else "$title  ·  ROUND $roundNumber/4", LeafDark)
            Spacer(Modifier.height(13.dp))
            Heading(when (exercise.type) {
                ExerciseType.CHOOSE_TAGALOG -> "Choose the translation"
                ExerciseType.CHOOSE_ENGLISH -> "What does this mean?"
                ExerciseType.WORD_BANK -> "Build the translation"
                ExerciseType.LISTEN -> "What do you hear?"
                ExerciseType.TYPE_TAGALOG -> "Say it in Tagalog"
                ExerciseType.PICTURE_MATCH -> "Choose the picture"
                ExerciseType.IMAGE_FLASHCARD -> "Say the word in Tagalog"
                ExerciseType.SPEAK_TAGALOG -> "Repeat after your friend"
                ExerciseType.STORY -> "What happens next?"
                ExerciseType.MATCH_AUDIO -> "Tap the matching pairs"
                ExerciseType.SPEAK_SET -> "Say the words in Tagalog"
                ExerciseType.SPEAK_FLASHCARDS -> "Say the words in Tagalog"
            }, 24.sp)
            Spacer(Modifier.height(23.dp))
            when (exercise.type) {
                ExerciseType.MATCH_AUDIO, ExerciseType.SPEAK_SET,
                ExerciseType.SPEAK_FLASHCARDS, ExerciseType.SPEAK_TAGALOG,
                ExerciseType.TYPE_TAGALOG, ExerciseType.IMAGE_FLASHCARD -> Unit
                ExerciseType.LISTEN -> {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                        .fillMaxWidth().clip(RoundedCornerShape(22.dp))
                        .background(Sky.copy(alpha = .08f))
                        .clickable { listenTo(exercise.phrase.tagalog) }.padding(17.dp)) {
                        TarsierMascot(Modifier.size(78.dp))
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text("🔊  LISTEN", fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold, color = SkyDark)
                            Text(if (showTranscript) exercise.phrase.tagalog else "Tap to play",
                                color = Muted, fontSize = 13.sp)
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
                        Text("🐢 SLOW", Modifier.clickable {
                            listenTo(exercise.phrase.tagalog, slower = true)
                        }.padding(vertical = 8.dp), fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold, color = SkyDark)
                        Text(if (showTranscript) "HIDE TEXT" else "SHOW TEXT",
                            Modifier.clickable { showTranscript = !showTranscript }
                                .padding(vertical = 8.dp), fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold, color = Muted)
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
                    onDone = ::completeAutomatic
                )
                ExerciseType.SPEAK_SET, ExerciseType.SPEAK_FLASHCARDS -> SpeakingSequence(
                    phrases = exercise.pairs,
                    isCardStack = exercise.type == ExerciseType.SPEAK_FLASHCARDS,
                    onSpeakAudio = { listenTo(it) },
                    onDone = ::completeAutomatic
                )
                ExerciseType.SPEAK_TAGALOG, ExerciseType.TYPE_TAGALOG -> SingleSpeakingExercise(
                    phrase = exercise.phrase,
                    onSpeakAudio = { listenTo(it) },
                    onSuccess = { completeAutomatic(true, listOf(AnswerAttempt(
                        vocabularyKey(exercise.phrase), true))) },
                    onMiss = { answerHistory = answerHistory + AnswerAttempt(
                        vocabularyKey(exercise.phrase), false) },
                    onSkip = { completeAutomatic(false, emptyList()) }
                )
                ExerciseType.IMAGE_FLASHCARD -> SpeakingSequence(
                    phrases = listOf(exercise.phrase),
                    isCardStack = true,
                    onSpeakAudio = { listenTo(it) },
                    onDone = ::completeAutomatic
                )
                ExerciseType.WORD_BANK -> WordBank(
                    exercise.tiles, selectedTileIds, checked,
                    onSelect = { id -> selectedTileIds = selectedTileIds + id },
                    onRemove = { id -> selectedTileIds = selectedTileIds - id }
                )
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
        AnimatedVisibility(visible = checked,
            enter = expandVertically() + fadeIn()) {
            Column(Modifier.fillMaxWidth().background(if (answerIsCorrect) Color(0xFFE8F8D9)
                else Color(0xFFFFE6E4)).padding(horizontal = 22.dp, vertical = 16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TarsierMascot(Modifier.size(39.dp), happy = answerIsCorrect)
                    Spacer(Modifier.width(10.dp))
                    Text(if (answerIsCorrect) listOf("Awesome!", "Excellent!", "Tama!", "Great job!")[current % 4]
                        else "Try the next one!", fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (answerIsCorrect) LeafDark else Red)
                }
                if (!answerIsCorrect) {
                    Spacer(Modifier.height(4.dp))
                    Text("Correct answer: $answer", color = Ink, fontSize = 14.sp)
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
                        vocabularyKey(exercise.phrase), answerIsCorrect
                    )
                    if (answerIsCorrect) {
                        correctCount += 1
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
                            onFinished(correctCount, questionQueue.size, answerHistory)
                        } else {
                            current += 1
                            selectedOption = null
                            typedText = ""
                            selectedTileIds = emptyList()
                            checked = false
                            answerIsCorrect = false
                            showTranscript = false
                        }
                    },
                    Modifier.fillMaxWidth(),
                    color = if (answerIsCorrect) Leaf else Red,
                    pressedColor = if (answerIsCorrect) LeafDark else Color(0xFFD63838)
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
private fun ChoiceTile(
    text: String,
    selected: Boolean,
    checked: Boolean,
    correctAnswer: Boolean,
    onClick: () -> Unit
) {
    val accent = when {
        checked && correctAnswer -> Leaf
        checked && selected -> Red
        selected -> Sky
        else -> Border
    }
    val pressed = when {
        checked && correctAnswer -> Color(0xFFE9F9DC)
        checked && selected -> Color(0xFFFFE9E7)
        selected -> Color(0xFFE8F7FF)
        else -> Color.White
    }
    Box(
        Modifier.fillMaxWidth().heightIn(min = 61.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (accent == Border) Color(0xFFD2D2D2) else accent.copy(alpha = .83f))
            .clickable(enabled = !checked, onClick = onClick)
    ) {
        Row(
            Modifier.fillMaxWidth().padding(bottom = 4.dp)
                .clip(RoundedCornerShape(15.dp)).background(pressed)
                .border(2.dp, accent, RoundedCornerShape(15.dp))
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text, color = Ink, fontSize = 16.sp,
                fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            if (selected || (checked && correctAnswer)) {
                Text(if (checked && selected && !correctAnswer) "✕" else "✓",
                    color = accent, fontSize = 18.sp, fontWeight = FontWeight.Black)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun WordBank(
    tiles: List<WordTile>, selectedIds: List<Int>, checked: Boolean,
    onSelect: (Int) -> Unit, onRemove: (Int) -> Unit
) {
    Text("YOUR TRANSLATION", color = Muted, fontWeight = FontWeight.ExtraBold,
        letterSpacing = 1.sp, fontSize = 11.sp)
    Spacer(Modifier.height(8.dp))
    FlowRow(Modifier.fillMaxWidth().defaultMinSize(minHeight = 94.dp)
        .clip(RoundedCornerShape(16.dp)).background(Pale).padding(11.dp),
        horizontalArrangement = Arrangement.spacedBy(7.dp),
        verticalArrangement = Arrangement.spacedBy(7.dp)) {
        selectedIds.forEach { id ->
            tiles.find { it.id == id }?.let { tile ->
                WordTileChip(tile.text, true, !checked) { onRemove(tile.id) }
            }
        }
    }
    Spacer(Modifier.height(19.dp))
    Text("TAP THE WORDS IN ORDER", color = Muted, fontWeight = FontWeight.ExtraBold,
        letterSpacing = 1.sp, fontSize = 11.sp)
    Spacer(Modifier.height(12.dp))
    FlowRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(9.dp)) {
        tiles.filter { it.id !in selectedIds }.forEach { tile ->
            WordTileChip(tile.text, false, !checked) { onSelect(tile.id) }
        }
    }
}

@Composable
private fun WordTileChip(label: String, chosen: Boolean, enabled: Boolean, onClick: () -> Unit) {
    Box(Modifier.clip(RoundedCornerShape(12.dp))
        .background(if (chosen) Sky.copy(alpha = .11f) else Color.White)
        .border(2.dp, if (chosen) Sky else Border, RoundedCornerShape(12.dp))
        .clickable(enabled = enabled, onClick = onClick)
        .padding(horizontal = 14.dp, vertical = 12.dp)) {
        Text(label, color = if (chosen) SkyDark else Ink, fontWeight = FontWeight.Bold, fontSize = 15.sp)
    }
}

/**
 * Duolingo-inspired result layout: spacious art, oversized yellow headline,
 * three colour-coded metric cards, and one prominent blue Continue button.
 * Continue always returns to the Learn path (wired by MainActivity).
 */
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
    val showTime = "%d:%02d".format(secondsElapsed / 60, secondsElapsed % 60)
    val timeLabel = if (secondsElapsed < 240) "QUICK" else "COMMITTED"
    val heading = when {
        practice -> "Practice complete!"
        pathActivity -> "Challenge complete!"
        else -> "Lesson complete!"
    }
    val mascotScale = remember { Animatable(0.75f) }
    LaunchedEffect(Unit) {
        mascotScale.animateTo(1.12f, tween(430))
        mascotScale.animateTo(1f, tween(240))
    }
    BoxWithConstraints(Modifier.fillMaxSize().background(Color.White)) {
        val compact = maxHeight < 690.dp
        Column(
            Modifier.fillMaxSize().padding(horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.weight(if (compact) 0.12f else 0.55f))
            Box(
                Modifier.fillMaxWidth().height(if (compact) 176.dp else 242.dp),
                contentAlignment = Alignment.Center
            ) {
                // Original Dalawingo mascot, not Duolingo's copyrighted characters.
                TarsierMascot(
                    Modifier.size(if (compact) 157.dp else 203.dp)
                        .graphicsLayer(scaleX = mascotScale.value, scaleY = mascotScale.value),
                    happy = true
                )
                Text("✨", Modifier.align(Alignment.TopStart)
                    .padding(start = 33.dp, top = 22.dp), fontSize = 30.sp)
                Text("🎉", Modifier.align(Alignment.TopEnd)
                    .padding(end = 32.dp, top = 10.dp), fontSize = 27.sp)
                Text("⭐", Modifier.align(Alignment.BottomEnd)
                    .padding(end = 64.dp), fontSize = 23.sp)
            }
            Spacer(Modifier.height(if (compact) 8.dp else 22.dp))
            Text(
                heading,
                color = Color(0xFFFFC800),
                fontSize = if (compact) 29.sp else 34.sp,
                lineHeight = if (compact) 33.sp else 40.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.weight(if (compact) 0.14f else 0.70f))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                CelebrationStatCard("TOTAL XP", "⚡", "$xp", Color(0xFFFFCC41), Modifier.weight(1f))
                CelebrationStatCard(timeLabel, "◷", showTime, Color(0xFF51B0ED), Modifier.weight(1f))
                CelebrationStatCard(
                    if (perfect) "PERFECT!" else "ACCURACY",
                    "🎯", "$accuracy%", Color(0xFF79CA37), Modifier.weight(1f)
                )
            }
            if (pesosEarned > 0 || streakMilestone > 0 || earnedQuests.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(13.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (pesosEarned > 0) Text("🪙 +$pesosEarned", color = Color(0xFFBA8400),
                        fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
                    if (streakMilestone > 0) Text("🔥 $streakMilestone days", color = Tangerine,
                        fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                    if (earnedQuests.isNotEmpty()) Text("🎁 ${earnedQuests.size}",
                        color = Purple, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                }
            }
            Spacer(Modifier.weight(if (compact) 0.17f else 1.1f))
            // Raised blue CTA, like the reference. XP is awarded before this screen.
            DalaButton(
                "CONTINUE",
                onContinue,
                Modifier.fillMaxWidth(),
                color = Color(0xFF1CB0F6),
                pressedColor = Color(0xFF168ACA),
                textColor = Color.White
            )
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun CelebrationStatCard(
    label: String,
    emoji: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier.height(112.dp)
            .clip(RoundedCornerShape(23.dp))
            .background(color)
            .padding(3.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(Modifier.height(27.dp), contentAlignment = Alignment.Center) {
            Text(
                label, color = Color.White, fontWeight = FontWeight.ExtraBold,
                fontSize = 11.sp, maxLines = 1
            )
        }
        Row(
            Modifier.fillMaxSize().clip(RoundedCornerShape(20.dp))
                .background(Color.White),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(emoji, fontSize = 18.sp)
            Spacer(Modifier.width(3.dp))
            Text(value, color = color, fontSize = 21.sp,
                fontWeight = FontWeight.ExtraBold, maxLines = 1)
        }
    }
}
