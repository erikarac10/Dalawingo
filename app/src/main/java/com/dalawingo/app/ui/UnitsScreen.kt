package com.dalawingo.app.ui

import com.dalawingo.app.R

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.dalawingo.app.data.LearnerProgress
import com.dalawingo.app.data.ROUNDS_PER_LESSON
import com.dalawingo.app.data.roundsFor
import com.dalawingo.app.model.CourseUnit
import com.dalawingo.app.model.Lesson
import com.dalawingo.app.model.course
import com.dalawingo.app.model.isUnlocked
import java.util.Locale

/** Duolingo-style course map: illustration cards, not expanding lesson lists. */
@Composable
fun UnitsScreen(
    progress: LearnerProgress,
    selectedUnit: CourseUnit,
    onClose: () -> Unit,
    onLesson: (Lesson) -> Unit,
    onGuide: (CourseUnit) -> Unit,
    onLocked: () -> Unit,
    onSelectUnit: (CourseUnit) -> Unit = {}
) {
    val state = rememberLazyListState()
    LaunchedEffect(selectedUnit.number) {
        val index = course.indexOfFirst { it.number == selectedUnit.number }
        if (index >= 0) state.scrollToItem(index)
    }
    Column(Modifier.fillMaxSize().background(Color.White)) {
        Row(Modifier.fillMaxWidth().height(67.dp)
            .padding(horizontal = 18.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(41.dp).clickable(onClick = onClose),
                contentAlignment = Alignment.Center) {
                GameIcon(GameGlyph.CLOSE, Modifier.size(29.dp), Color(0xFF999999))
            }
            Spacer(Modifier.weight(1f))
            Text("Tagalog Course", color = Muted, fontSize = 17.sp,
                fontWeight = FontWeight.Bold)
            Spacer(Modifier.weight(1f))
            Spacer(Modifier.width(41.dp))
        }
        Box(Modifier.fillMaxWidth().height(2.dp).background(Border))
        LazyColumn(state = state, modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 22.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)) {
            course.forEachIndexed { index, unit ->
                item(key = "section_${unit.number}") {
                    val completedRounds = unit.lessons.sumOf { progress.roundsFor(it.id) }
                    val total = unit.lessons.size * ROUNDS_PER_LESSON
                    val accessible = index == 0 ||
                        course[index - 1].lessons.all { it.id in progress.completed }
                    Column(Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp))
                        .background(Color.White)
                        .border(2.dp, Border, RoundedCornerShape(22.dp))
                        .clickable { if (accessible) onSelectUnit(unit) else onLocked() }) {
                        Box(Modifier.fillMaxWidth().height(165.dp)
                            .background(Color(0xFFDEF3FF))) {
                            Box(Modifier.align(Alignment.TopStart)
                                .padding(start = 16.dp, top = 18.dp)
                                .fillMaxWidth(.78f).heightIn(min = 57.dp)
                                .clip(RoundedCornerShape(19.dp))
                                .background(Color.White).padding(16.dp)) {
                                Text(unit.lessons.firstOrNull()?.phrases?.firstOrNull()?.tagalog
                                    ?: "Kumusta!", color = Ink, fontSize = 19.sp,
                                    maxLines = 2)
                            }
                            TarsierMascot(Modifier.align(Alignment.BottomEnd)
                                .offset(x = 11.dp, y = 19.dp).size(120.dp))
                        }
                        Column(Modifier.padding(17.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column(Modifier.weight(1f)) {
                                    Text("Section ${unit.number}", color = Ink,
                                        fontSize = 23.sp, fontWeight = FontWeight.ExtraBold)
                                    Spacer(Modifier.height(3.dp))
                                    Text(unit.title, color = Muted, fontSize = 13.sp)
                                }
                                if (accessible) DalaIcon(R.drawable.ic_ph_flag, "Tagalog", 26.dp)
                                else Text("🔒", fontSize = 20.sp)
                            }
                            Spacer(Modifier.height(12.dp))
                            ProgressLine(completedRounds.toFloat() / total.coerceAtLeast(1),
                                Leaf, Modifier.fillMaxWidth(), 12.dp)
                            if (accessible) {
                                Spacer(Modifier.height(11.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("JUMP HERE", color = SkyDark,
                                        fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                                    Spacer(Modifier.width(4.dp))
                                    DalaIcon(R.drawable.ic_arrow_right, null, 17.dp, SkyDark)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/** A real full-page unit guidebook, opened from the book portion of the path banner. */
@Composable
fun GuidebookScreen(unit: CourseUnit, onClose: () -> Unit) {
    val context = LocalContext.current
    var ready by remember { mutableStateOf(false) }
    val tts = remember {
        TextToSpeech(context.applicationContext) { status ->
            ready = status == TextToSpeech.SUCCESS
        }
    }
    DisposableEffect(tts) { onDispose { tts.stop(); tts.shutdown() } }
    LaunchedEffect(ready) {
        if (ready) tts.language = Locale.forLanguageTag("fil-PH")
    }
    Column(Modifier.fillMaxSize().background(Color.White)) {
        Row(Modifier.fillMaxWidth().height(56.dp)
            .padding(horizontal = 14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(41.dp).clickable(onClick = onClose),
                contentAlignment = Alignment.Center) {
                GameIcon(GameGlyph.CLOSE, Modifier.size(29.dp), Color(0xFF999999))
            }
        }
        LazyColumn(modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(bottom = 25.dp)) {
            item {
                Column(Modifier.fillMaxWidth().padding(top = 15.dp, bottom = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    TarsierMascot(Modifier.size(138.dp))
                    Spacer(Modifier.height(22.dp))
                    Text("SECTION 1, UNIT ${unit.number}", color = Ink,
                        fontWeight = FontWeight.ExtraBold, fontSize = 15.sp,
                        letterSpacing = 1.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(unit.title, color = Ink, fontSize = 27.sp,
                        fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center)
                }
                Box(Modifier.fillMaxWidth().height(2.dp).background(Border))
                Spacer(Modifier.height(20.dp))
                Text("KEY PHRASES", Modifier.padding(horizontal = 21.dp),
                    color = SkyDark, fontWeight = FontWeight.ExtraBold,
                    fontSize = 17.sp)
                Spacer(Modifier.height(15.dp))
            }
            unit.lessons.forEach { lesson ->
                item(key = "topic_${lesson.id}") {
                    Text(lesson.title, Modifier.padding(start = 21.dp,
                        top = 12.dp, bottom = 10.dp), color = Ink,
                        fontWeight = FontWeight.Bold, fontSize = 17.sp)
                }
                lesson.phrases.forEachIndexed { index, phrase ->
                    item(key = "phrase_${lesson.id}_$index") {
                        Column(Modifier.fillMaxWidth().padding(
                            start = 24.dp, end = 46.dp, bottom = 12.dp)
                            .clip(RoundedCornerShape(20.dp)).background(Color.White)
                            .border(2.dp, Border, RoundedCornerShape(20.dp))
                            .clickable {
                                if (ready) tts.speak(phrase.tagalog,
                                    TextToSpeech.QUEUE_FLUSH, null, "guide_${lesson.id}_$index")
                            }.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                DalaIcon(R.drawable.ic_speaker, "Play phrase", 25.dp, SkyDark)
                                Spacer(Modifier.width(8.dp))
                                Text(phrase.tagalog, color = Ink, fontSize = 17.sp,
                                    modifier = Modifier.weight(1f), lineHeight = 23.sp)
                            }
                            Spacer(Modifier.height(7.dp))
                            Text(phrase.english, color = Muted, fontSize = 16.sp,
                                modifier = Modifier.padding(start = 33.dp))
                        }
                    }
                }
            }
        }
    }
}

// Kept for existing callers/previews. Guidebooks now render as a page, not a dialog.
@Composable
fun GuidebookDialog(unit: CourseUnit, onDismiss: () -> Unit) =
    GuidebookScreen(unit, onDismiss)
