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
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var recording by remember { mutableStateOf(false) }
    var level by remember { mutableFloatStateOf(0f) }
    var hint by remember { mutableStateOf("") }
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
            override fun onError(error: Int) { recording = false; hint = "Didn't catch that. Try again." }
            override fun onResults(results: Bundle?) {
                recording = false
                val heard = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.firstOrNull()
                if (heard.isNullOrBlank()) hint = "Didn't catch that. Try again."
                else { hint = ""; currentCallback(heard) }
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
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            Modifier
                .width(if (recording) 190.dp else 106.dp)
                .height(85.dp)
                .clip(RoundedCornerShape(23.dp))
                .background(Color(0xFF0788CA))
                .clickable(enabled = enabled) {
                    if (recording) { recognizer?.stopListening(); recording = false }
                    else if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED) start()
                    else permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
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
                } else Text("🎙", color = Color.White, fontSize = 41.sp)
            }
        }
        if (hint.isNotBlank()) {
            Spacer(Modifier.height(9.dp))
            Text(hint, color = Muted, fontSize = 12.sp, textAlign = TextAlign.Center)
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
    var hint by remember(phrase) { mutableStateOf("") }
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TarsierMascot(Modifier.size(105.dp))
            Spacer(Modifier.width(8.dp))
            Column(Modifier.weight(1f)
                .clip(RoundedCornerShape(21.dp))
                .border(2.dp, Border, RoundedCornerShape(21.dp))
                .clickable { onSpeakAudio(phrase.tagalog) }
                .padding(15.dp)) {
                Text("🔊  ${phrase.tagalog}", color = Ink,
                    fontSize = 17.sp, lineHeight = 23.sp)
                Text("Tap to listen", color = Sky, fontSize = 12.sp)
            }
        }
        Spacer(Modifier.height(46.dp))
        InAppMicrophone(enabled = true, onSpeech = { heard ->
            if (acceptedSpeech(heard, phrase.tagalog)) onSuccess()
            else { hint = "Try that again!"; onMiss() }
        })
        if (hint.isNotBlank()) {
            Spacer(Modifier.height(12.dp))
            Text(hint, color = Red, fontWeight = FontWeight.Bold)
        }
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
    onDone: (Boolean, List<AnswerAttempt>) -> Unit
) {
    var remaining by remember(phrases) { mutableStateOf(phrases) }
    var finished by remember(phrases) { mutableStateOf<Set<String>>(emptySet()) }
    var mistakes by remember(phrases) { mutableStateOf(false) }
    var attempts by remember(phrases) { mutableStateOf<List<AnswerAttempt>>(emptyList()) }
    var error by remember(phrases) { mutableStateOf(false) }
    val target = remaining.firstOrNull()
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        if (isCardStack) {
            Box(Modifier.fillMaxWidth().height(290.dp), contentAlignment = Alignment.Center) {
                for (i in 3 downTo 1) {
                    Box(Modifier.width((238 + i * 8).dp)
                        .height((245 + i * 5).dp)
                        .offset(y = (-i * 9).dp)
                        .clip(RoundedCornerShape(29.dp)).background(Color.White)
                        .border(2.dp, Border, RoundedCornerShape(29.dp)))
                }
                Box(Modifier.width(273.dp).height(260.dp)
                    .clip(RoundedCornerShape(28.dp)).background(Color.White)
                    .border(2.dp, Border, RoundedCornerShape(28.dp)),
                    contentAlignment = Alignment.Center) {
                    Text(if (error) target?.tagalog.orEmpty() else target?.english.orEmpty(),
                        color = if (error) Red else Ink,
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
            InAppMicrophone(enabled = target != null, onSpeech = { heard ->
                val phrase = remaining.firstOrNull()
                if (phrase != null) {
                    val correct = acceptedSpeech(heard, phrase.tagalog)
                    val attempt = AnswerAttempt(vocabularyKey(phrase), correct)
                    attempts = attempts + attempt
                    if (correct) {
                        error = false
                        finished = finished + vocabularyKey(phrase)
                        remaining = remaining.drop(1)
                        if (remaining.isEmpty()) onDone(!mistakes, attempts)
                    } else {
                        mistakes = true; error = true
                        onSpeakAudio(phrase.tagalog)
                    }
                }
            })
            if (isCardStack && remaining.size > 1) {
                Box(Modifier.size(60.dp).clip(RoundedCornerShape(15.dp))
                    .border(2.dp, Border, RoundedCornerShape(15.dp))
                    .clickable { remaining = remaining.shuffled() },
                    contentAlignment = Alignment.Center) {
                    Text("🔄", fontSize = 29.sp)
                }
            }
        }
        if (error && target != null) {
            Spacer(Modifier.height(12.dp))
            Text("Try again: ${target.tagalog}", color = Red, fontSize = 14.sp)
        }
        Spacer(Modifier.height(22.dp))
        Text("CAN'T SPEAK NOW", Modifier.clickable { onDone(false, attempts) }
            .padding(12.dp), color = Muted, fontWeight = FontWeight.ExtraBold,
            fontSize = 13.sp)
    }
}

/** Audible tile-to-translation pairs, with a per-phrase success/mistake history. */
@Composable
fun AudioPairMatching(
    phrases: List<Phrase>,
    onSpeakAudio: (String) -> Unit,
    onDone: (Boolean, List<AnswerAttempt>) -> Unit
) {
    val entries = remember(phrases) { phrases.distinctBy { vocabularyKey(it) } }
    val answers = remember(entries) { entries.shuffled() }
    var audioSelected by remember(entries) { mutableStateOf<Int?>(null) }
    var matched by remember(entries) { mutableStateOf<Set<String>>(emptySet()) }
    var attempts by remember(entries) { mutableStateOf<List<AnswerAttempt>>(emptyList()) }
    var hadMistake by remember(entries) { mutableStateOf(false) }
    var wrong by remember(entries) { mutableStateOf<String?>(null) }
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        entries.forEachIndexed { i, pair ->
            val leftDone = vocabularyKey(pair) in matched
            val right = answers.getOrNull(i)
            val rightDone = right != null && vocabularyKey(right) in matched
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Box(Modifier.weight(1f).height(66.dp)
                    .clip(RoundedCornerShape(17.dp))
                    .background(if (leftDone) Color(0xFFD9FFBD) else if (audioSelected == i)
                        Color(0xFFDDF4FF) else Color.White)
                    .border(2.dp, if (leftDone) Color(0xFF9CE96C)
                        else if (audioSelected == i) Color(0xFF6DD3FA) else Border,
                        RoundedCornerShape(17.dp))
                    .clickable(enabled = !leftDone) {
                        audioSelected = i; wrong = null; onSpeakAudio(pair.tagalog)
                    }, contentAlignment = Alignment.Center) {
                    Text(if (leftDone) "✓" else "🔊  ▂▅▇▅▂", fontSize = 20.sp,
                        color = if (leftDone) LeafDark else SkyDark)
                }
                Box(Modifier.weight(1f).height(66.dp)
                    .clip(RoundedCornerShape(17.dp))
                    .background(if (rightDone) Color(0xFFD9FFBD) else Color.White)
                    .border(2.dp, if (rightDone) Color(0xFF9CE96C)
                        else if (right != null && wrong == vocabularyKey(right)) Red else Border,
                        RoundedCornerShape(17.dp))
                    .clickable(enabled = right != null && !rightDone) {
                        val picked = audioSelected
                        if (picked != null && right != null) {
                            val selectedPair = entries[picked]
                            val success = vocabularyKey(selectedPair) == vocabularyKey(right)
                            val newAttempt = AnswerAttempt(vocabularyKey(selectedPair), success)
                            attempts = attempts + newAttempt
                            if (success) {
                                matched = matched + vocabularyKey(right)
                                audioSelected = null; wrong = null
                                if (matched.size == entries.size) onDone(!hadMistake, attempts)
                            } else { hadMistake = true; wrong = vocabularyKey(right) }
                        }
                    }, contentAlignment = Alignment.Center) {
                    Text(right?.english.orEmpty(), textAlign = TextAlign.Center,
                        fontSize = 17.sp, color = Ink)
                }
            }
        }
        Spacer(Modifier.height(14.dp))
        Text("CAN'T LISTEN NOW", Modifier.align(Alignment.CenterHorizontally)
            .clickable { onDone(false, attempts) }.padding(12.dp),
            color = Muted, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
    }
}
