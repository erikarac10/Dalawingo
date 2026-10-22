package com.dalawingo.app.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Icon
import com.dalawingo.app.R
import kotlinx.coroutines.delay
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.core.content.ContextCompat
import com.dalawingo.app.model.AnswerAttempt
import com.dalawingo.app.model.Phrase
import com.dalawingo.app.model.vocabularyKey
import java.text.Normalizer
import java.util.Locale
import kotlin.math.abs

private fun spokenText(s: String): String = Normalizer.normalize(
    s.lowercase(Locale.ROOT), Normalizer.Form.NFD
).replace(Regex("\\p{Mn}+"), "")
    .replace(Regex("[^\\p{L}\\p{N}\\s]"), "")
    .replace(Regex("\\s+"), " ").trim()

/** A little tolerance for recognizer spelling without accepting unrelated words. */
private fun acceptedSpeech(heard: String, expected: String): Boolean {
    val a = spokenText(heard)
    val b = spokenText(expected)
    if (a == b) return true
    if (a.isEmpty() || b.isEmpty() || abs(a.length - b.length) > 2) return false
    val row = IntArray(b.length + 1) { it }
    for (i in a.indices) {
        var before = row[0]
        row[0] = i + 1
        for (j in b.indices) {
            val old = row[j + 1]
            row[j + 1] = minOf(row[j + 1] + 1, row[j] + 1,
                before + if (a[i] == b[j]) 0 else 1)
            before = old
        }
    }
    return row[b.length] <= if (b.length >= 15) 2 else 0
}

/** SpeechRecognizer runs inside our Compose screen: no external recognizer dialog. */
@Composable
fun InAppMicrophone(
    enabled: Boolean,
    onSpeech: (String) -> Unit,
    modifier: Modifier = Modifier,
    continuous: Boolean = false
) {
    val context = LocalContext.current
    var recording by remember { mutableStateOf(false) }
    var level by remember { mutableFloatStateOf(0f) }
    var hint by remember { mutableStateOf("") }
    var keepGoing by remember { mutableStateOf(false) }
    var restartToken by remember { mutableIntStateOf(0) }
    val currentlyEnabled by rememberUpdatedState(enabled)
    val currentCallback by rememberUpdatedState(onSpeech)
    val available = remember(context) { SpeechRecognizer.isRecognitionAvailable(context) }
    val recognizer = remember(context) {
        if (available) SpeechRecognizer.createSpeechRecognizer(context) else null
    }
    DisposableEffect(recognizer) {
        recognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) { recording = true; hint = "Listening…" }
            override fun onBeginningOfSpeech() { recording = true }
            override fun onRmsChanged(rmsdB: Float) { level = rmsdB.coerceIn(0f, 10f) }
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() { recording = false; hint = "Checking…" }
            override fun onError(error: Int) {
                recording = false
                hint = if (error == SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS)
                    "Microphone permission needed" else ""
                if (continuous && keepGoing && currentlyEnabled &&
                    (error == SpeechRecognizer.ERROR_NO_MATCH ||
                     error == SpeechRecognizer.ERROR_SPEECH_TIMEOUT)) restartToken++
            }
            override fun onResults(results: Bundle?) {
                recording = false
                val heard = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.firstOrNull()
                if (!heard.isNullOrBlank()) { hint = ""; currentCallback(heard) }
                if (continuous && keepGoing && currentlyEnabled) restartToken++
            }
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
        onDispose { recognizer?.cancel(); recognizer?.destroy() }
    }
    fun start() {
        if (!available || recognizer == null) {
            hint = "Speech recognition isn't available on this device."
            return
        }
        hint = "Listening…"
        recording = true
        level = 0f
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fil-PH")
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "fil-PH")
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        }
        runCatching { recognizer.startListening(intent) }.onFailure {
            recording = false; hint = "Microphone unavailable. Try again."
        }
    }
    LaunchedEffect(restartToken) {
        if (restartToken > 0 && continuous && keepGoing) {
            delay(720)
            if (currentlyEnabled && keepGoing && !recording) start()
        }
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { allowed ->
        if (allowed) start() else hint = "Microphone permission is required."
    }
    val microphoneColor = Color(0xFF1CB0F6)
    val strength by animateFloatAsState(
        targetValue = if (recording) 1f else .3f,
        label = "Speech recording pulse"
    )
    Column(modifier.widthIn(min = 190.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            Modifier
                .width(if (recording) 190.dp else 106.dp)
                .height(85.dp)
                .clip(RoundedCornerShape(23.dp))
                .background(Color(0xFF0788CA))
                .clickable(enabled = enabled) {
                    if (recording) {
                        keepGoing = false
                        recognizer?.stopListening()
                        recording = false
                    } else {
                        keepGoing = continuous
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
                            == PackageManager.PERMISSION_GRANTED) start()
                        else permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    }
                }.padding(bottom = 5.dp), contentAlignment = Alignment.Center
        ) {
            Row(
                Modifier.fillMaxSize().clip(RoundedCornerShape(22.dp))
                    .background(microphoneColor),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (recording) {
                    repeat(11) { n ->
                        val waveHeight = (9 + (8 + level * 3) *
                            (1f - abs(n - 5) / 6f) * strength).dp
                        Box(Modifier.padding(horizontal = 2.dp).width(4.dp)
                            .height(waveHeight)
                            .clip(RoundedCornerShape(3.dp)).background(Color.White))
                    }
                } else DalaIcon(R.drawable.ic_microphone, "Record pronunciation", 43.dp,
                    Color.White)
            }
        }
        Box(Modifier.fillMaxWidth().height(39.dp), contentAlignment = Alignment.TopCenter) {
            if (hint.isNotBlank()) Text(hint, color = Muted, fontSize = 12.sp,
                textAlign = TextAlign.Center, maxLines = 2)
        }
    }
}

@Composable
fun SingleSpeakingExercise(
    phrase: Phrase,
    onSpeakAudio: (String) -> Unit,
    onSuccess: () -> Unit,
    onMiss: () -> Unit,
    onSkip: () -> Unit
) {
    var wrongFlash by remember(phrase) { mutableIntStateOf(0) }
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TarsierMascot(Modifier.size(105.dp))
            Spacer(Modifier.width(8.dp))
            Column(Modifier.weight(1f)
                .clip(RoundedCornerShape(21.dp))
                .border(2.dp, Border, RoundedCornerShape(21.dp))
                .clickable { onSpeakAudio(phrase.tagalog) }
                .padding(15.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    DalaIcon(R.drawable.ic_speaker, "Listen", 26.dp, Sky)
                    Spacer(Modifier.width(7.dp))
                    Text(phrase.tagalog, color = Ink, fontSize = 17.sp, lineHeight = 23.sp)
                }
            }
        }
        Spacer(Modifier.height(46.dp))
        InAppMicrophone(enabled = true, onSpeech = { heard ->
            if (acceptedSpeech(heard, phrase.tagalog)) onSuccess()
            else { wrongFlash++; onMiss() }
        })
        Spacer(Modifier.height(32.dp))
        Text("CAN'T SPEAK NOW", Modifier.clickable(onClick = onSkip)
            .padding(12.dp), fontWeight = FontWeight.ExtraBold,
            color = Muted, fontSize = 13.sp)
    }
}

/** Duolingo-style speech stack and all-green speak-a-set use one adaptive data pipeline. */
@Composable
fun SpeakingSequence(
    phrases: List<Phrase>,
    isCardStack: Boolean,
    onSpeakAudio: (String) -> Unit,
    onDone: (Boolean, List<AnswerAttempt>) -> Unit,
    onSkip: () -> Unit = {}
) {
    var remaining by remember(phrases) { mutableStateOf(phrases) }
    var finished by remember(phrases) { mutableStateOf<Set<String>>(emptySet()) }
    var mistakes by remember(phrases) { mutableStateOf(false) }
    var attempts by remember(phrases) { mutableStateOf<List<AnswerAttempt>>(emptyList()) }
    var error by remember(phrases) { mutableStateOf(false) }
    LaunchedEffect(error) {
        if (error) {
            delay(420)
            error = false
        }
    }
    var advancing by remember(phrases) { mutableStateOf(false) }
    var advanceToken by remember(phrases) { mutableIntStateOf(0) }
    val successScale = remember { Animatable(1f) }
    val successSlide = remember { Animatable(0f) }
    LaunchedEffect(advanceToken) {
        if (advanceToken > 0) {
            successScale.snapTo(1f)
            successSlide.snapTo(0f)
            successScale.animateTo(1.055f, tween(140))
            delay(120)
            successSlide.animateTo(1f, tween(235))
            remaining = remaining.drop(1)
            advancing = false
            if (remaining.isEmpty()) onDone(!mistakes, attempts)
            successSlide.snapTo(0f)
            successScale.snapTo(1f)
        }
    }
    val target = remaining.firstOrNull()
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        if (isCardStack) {
            // All layers use the same width, centre, and corner radius.
            Box(Modifier.fillMaxWidth().height(307.dp), contentAlignment = Alignment.Center) {
                for (i in 3 downTo 1) {
                    Box(Modifier.size(width = 270.dp, height = 253.dp)
                        .offset(y = (-i * 6).dp)
                        .clip(RoundedCornerShape(28.dp)).background(Color.White)
                        .border(2.dp, Border, RoundedCornerShape(28.dp)))
                }
                Box(Modifier.size(width = 270.dp, height = 253.dp)
                    .graphicsLayer(
                        translationX = successSlide.value * 370f,
                        rotationZ = successSlide.value * 10f,
                        scaleX = successScale.value,
                        scaleY = successScale.value
                    )
                    .clip(RoundedCornerShape(28.dp))
                    .background(if (advancing) Color(0xFFD9FFBD) else Color.White)
                    .border(2.dp, if (advancing) Leaf else if (error) Red else Border,
                        RoundedCornerShape(28.dp)),
                    contentAlignment = Alignment.Center) {
                    Text(target?.english.orEmpty(),
                        color = if (advancing) LeafDark else Ink,
                        fontWeight = FontWeight.Bold, fontSize = 31.sp,
                        textAlign = TextAlign.Center)
                }
            }
        } else {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                phrases.forEach { p ->
                    val done = vocabularyKey(p) in finished
                    Box(Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(17.dp))
                        .background(if (done) Color(0xFFD9FFBD) else Color.White)
                        .border(2.dp, if (done) Color(0xFF9DE866) else Border,
                            RoundedCornerShape(17.dp)).padding(vertical = 15.dp),
                        contentAlignment = Alignment.Center) {
                        Text(p.tagalog, color = if (done) LeafDark else Ink,
                            fontSize = 21.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        Spacer(Modifier.height(34.dp))
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            InAppMicrophone(enabled = target != null && !advancing, continuous = true,
                onSpeech = { heard ->
                val phrase = remaining.firstOrNull()
                if (phrase != null) {
                    val correct = acceptedSpeech(heard, phrase.tagalog)
                    val attempt = AnswerAttempt(vocabularyKey(phrase), correct)
                    attempts = attempts + attempt
                    if (correct) {
                        error = false
                        finished = finished + vocabularyKey(phrase)
                        advancing = true
                        advanceToken++
                    } else {
                        mistakes = true; error = true
                        // Brief red outline only: don't insert text or shift the layout.
                    }
                }
            })
            if (isCardStack && remaining.size > 1) {
                Box(Modifier.size(60.dp).clip(RoundedCornerShape(15.dp))
                    .border(2.dp, Border, RoundedCornerShape(15.dp))
                    .clickable { remaining = remaining.shuffled() },
                    contentAlignment = Alignment.Center) {
                    DalaIcon(R.drawable.ic_refresh, "Shuffle cards", 29.dp, SkyDark)
                }
            }
        }
        Spacer(Modifier.height(22.dp))
        Text("CAN'T SPEAK NOW", Modifier.clickable { onSkip() }
            .padding(12.dp), color = Muted, fontWeight = FontWeight.ExtraBold,
            fontSize = 13.sp)
    }
}

/** Both audio and written pair activities support choosing either column first. */
@Composable
fun AudioPairMatching(
    phrases: List<Phrase>,
    onSpeakAudio: (String) -> Unit,
    onDone: (Boolean, List<AnswerAttempt>) -> Unit,
    onSkip: () -> Unit = {}
) = PairMatching(phrases, true, onSpeakAudio, onDone, onSkip)

@Composable
fun WrittenPairMatching(
    phrases: List<Phrase>,
    onSpeakAudio: (String) -> Unit,
    onDone: (Boolean, List<AnswerAttempt>) -> Unit
) = PairMatching(phrases, false, onSpeakAudio, onDone)

@Composable
private fun PairMatching(
    phrases: List<Phrase>,
    audio: Boolean,
    onSpeakAudio: (String) -> Unit,
    onDone: (Boolean, List<AnswerAttempt>) -> Unit,
    onSkip: () -> Unit = {}
) {
    val pairs = remember(phrases) { phrases.distinctBy(::vocabularyKey) }
    val mixed = remember(pairs) { pairs.shuffled() }
    var selectedLeft by remember(pairs) { mutableStateOf<Int?>(null) }
    var selectedRight by remember(pairs) { mutableStateOf<Int?>(null) }
    var wrongLeft by remember(pairs) { mutableStateOf<Int?>(null) }
    var wrongRight by remember(pairs) { mutableStateOf<Int?>(null) }
    var matched by remember(pairs) { mutableStateOf<Set<String>>(emptySet()) }
    var attempts by remember(pairs) { mutableStateOf<List<AnswerAttempt>>(emptyList()) }
    var mistakes by remember(pairs) { mutableStateOf(false) }
    var wrongCounter by remember(pairs) { mutableIntStateOf(0) }
    val shake = remember { Animatable(0f) }
    LaunchedEffect(wrongCounter) {
        if (wrongCounter > 0) {
            repeat(3) {
                shake.animateTo(-7f, tween(55))
                shake.animateTo(7f, tween(65))
            }
            shake.animateTo(0f, tween(70))
            delay(180)
            wrongLeft = null; wrongRight = null
        }
    }
    fun compare(left: Int, right: Int) {
        val item = pairs[left]
        val success = vocabularyKey(item) == vocabularyKey(mixed[right])
        attempts = attempts + AnswerAttempt(vocabularyKey(item), success,
            if (audio) com.dalawingo.app.model.ExerciseType.MATCH_AUDIO
            else com.dalawingo.app.model.ExerciseType.MATCH_TEXT)
        if (success) {
            matched = matched + vocabularyKey(item)
            wrongLeft = null; wrongRight = null
            if (matched.size == pairs.size) onDone(!mistakes, attempts)
        } else {
            mistakes = true; wrongLeft = left; wrongRight = right
            wrongCounter += 1
        }
        selectedLeft = null; selectedRight = null
    }
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        pairs.forEachIndexed { row, phrase ->
            val right = mixed.getOrNull(row)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(13.dp)) {
                val leftDone = vocabularyKey(phrase) in matched
                MatchTile(
                    selected = selectedLeft == row, wrong = wrongLeft == row,
                    matched = leftDone,
                    modifier = Modifier.weight(1f),
                    shakeX = if (wrongLeft == row) shake.value else 0f,
                    onClick = {
                        onSpeakAudio(phrase.tagalog)
                        val other = selectedRight
                        if (other != null) compare(row, other)
                        else selectedLeft = row
                    }
                ) {
                    if (audio) Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center) {
                        DalaIcon(R.drawable.ic_speaker, "Play audio", 23.dp,
                            if (selectedLeft == row) SkyDark else Sky)
                        Spacer(Modifier.width(7.dp))
                        DalaIcon(R.drawable.ic_soundwave, null, 59.dp,
                            if (selectedLeft == row) SkyDark else Sky)
                    }
                    else Text(phrase.tagalog, fontWeight = FontWeight.Bold, fontSize = 15.sp,
                        color = if (selectedLeft == row) SkyDark else Ink,
                        textAlign = TextAlign.Center)
                }
                if (right != null) {
                    val rightDone = vocabularyKey(right) in matched
                    MatchTile(selected = selectedRight == row, wrong = wrongRight == row,
                        matched = rightDone, modifier = Modifier.weight(1f),
                        shakeX = if (wrongRight == row) shake.value else 0f,
                        onClick = {
                            val other = selectedLeft
                            if (other != null) compare(other, row)
                            else selectedRight = row
                        }) {
                        Text(right.english, color = when {
                            rightDone -> LeafDark
                            selectedRight == row -> SkyDark
                            wrongRight == row -> Red
                            else -> Ink
                        }, fontSize = 16.sp, textAlign = TextAlign.Center)
                    }
                }
            }
        }
        if (audio) Text("CAN'T LISTEN NOW", modifier = Modifier.align(Alignment.CenterHorizontally)
            .clickable(onClick = onSkip).padding(14.dp),
            color = Muted, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
    }
}

@Composable
private fun MatchTile(
    selected: Boolean,
    wrong: Boolean,
    matched: Boolean,
    modifier: Modifier = Modifier,
    shakeX: Float,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val outline = when {
        wrong -> Red
        matched -> Leaf
        selected -> Sky
        else -> Border
    }
    val fill = when {
        wrong -> Color(0xFFFFDFDE)
        matched -> Color(0xFFE1FFCC)
        selected -> Color(0xFFDDF4FF)
        else -> Color.White
    }
    val bounce by animateFloatAsState(if (matched) 1.045f else 1f,
        animationSpec = tween(200), label = "Matched tile bounce")
    Box(modifier.height(66.dp)
        .graphicsLayer(translationX = shakeX, scaleX = bounce, scaleY = bounce)
        .clip(RoundedCornerShape(17.dp)).background(fill)
        .border(2.dp, outline, RoundedCornerShape(17.dp))
        .clickable(enabled = !matched, onClick = onClick).padding(7.dp),
        contentAlignment = Alignment.Center) {
        content()
    }
}
