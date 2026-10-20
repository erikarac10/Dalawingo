package com.dalawingo.app.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dalawingo.app.data.LearnerProgress
import com.dalawingo.app.data.ROUNDS_PER_LESSON
import com.dalawingo.app.data.roundsFor
import com.dalawingo.app.model.CourseUnit
import com.dalawingo.app.model.Lesson
import com.dalawingo.app.model.allLessons
import com.dalawingo.app.model.course
import com.dalawingo.app.model.isUnlocked
import com.dalawingo.app.model.nextLesson
import com.dalawingo.app.model.PathActivity
import com.dalawingo.app.model.PathActivityType
import com.dalawingo.app.model.interludeFor
import com.dalawingo.app.model.storyFor
import com.dalawingo.app.model.isActivityUnlocked
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

val unitColors = listOf(Leaf, Purple, Sky, Tangerine, Color(0xFFED86AD))

/** A one-time visual reveal, created only after a lesson advances. */
data class PathReveal(
    val token: Long,
    val lessonId: String,
    val fromRounds: Int,
    val toRounds: Int,
    val newlyUnlockedLessonId: String? = null,
    val unitCompleted: Boolean = false
)

private fun pathIndexForLesson(lessonId: String?): Int {
    var index = 0
    var found = 0
    course.forEach { unit ->
        index++ // Unit header
        unit.lessons.forEachIndexed { lessonIndex, lesson ->
            if (lesson.id == lessonId) found = index
            index++ // Lesson node
            if (lessonIndex == 1) index++ // Interlude
        }
        index++ // Story
        index++ // Gap
    }
    return found
}

@Composable
fun UnitBanner(
    unit: CourseUnit,
    index: Int,
    onOpenUnit: () -> Unit,
    onGuide: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = unitColors[index % unitColors.size]
    val shadow = color.copy(red = color.red * .77f,
        green = color.green * .77f, blue = color.blue * .77f)
    Box(modifier.fillMaxWidth().height(99.dp)
        .clip(RoundedCornerShape(20.dp)).background(shadow)) {
        Row(Modifier.fillMaxWidth().height(94.dp)
            .clip(RoundedCornerShape(19.dp)).background(color),
            verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f).fillMaxHeight()
                .clickable(onClick = onOpenUnit)
                .padding(start = 18.dp, top = 15.dp, bottom = 11.dp),
                verticalArrangement = Arrangement.Center) {
                Text("SECTION 1, UNIT ${unit.number}",
                    color = Color.White.copy(alpha = .87f),
                    fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(5.dp))
                Text(unit.title, color = Color.White, fontSize = 21.sp,
                    fontWeight = FontWeight.ExtraBold, maxLines = 1)
            }
            Box(Modifier.width(2.dp).fillMaxHeight()
                .background(Color.White.copy(alpha = .55f)))
            Box(Modifier.width(70.dp).fillMaxHeight()
                .clickable(onClick = onGuide), contentAlignment = Alignment.Center) {
                Text("📖", fontSize = 31.sp)
            }
        }
    }
}

@Composable
fun PathScreen(
    progress: LearnerProgress,
    listState: LazyListState,
    onLesson: (Lesson) -> Unit,
    onActivity: (PathActivity) -> Unit,
    onUnit: (CourseUnit) -> Unit,
    onGuide: (CourseUnit) -> Unit,
    onLocked: () -> Unit,
    onPesos: () -> Unit,
    reveal: PathReveal? = null,
    onRevealFinished: () -> Unit = {}
) {
    val currentLessonId = nextLesson(progress.completed)?.id ?: allLessons.lastOrNull()?.id
    val targetIndex = remember(currentLessonId) { pathIndexForLesson(currentLessonId) }
    var revealStage by remember(reveal?.token) { mutableIntStateOf(0) }
    var selectedNode by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(reveal?.token) {
        val event = reveal ?: return@LaunchedEffect
        // The previous lesson remains visible when we start the reveal.
        val lessonIndex = pathIndexForLesson(event.lessonId)
        listState.animateScrollToItem((lessonIndex - 1).coerceAtLeast(0))
        delay(320)
        revealStage = 1  // Fill the next ring segment.
        delay(900)
        if (event.newlyUnlockedLessonId != null) {
            val nextIndex = pathIndexForLesson(event.newlyUnlockedLessonId)
            listState.animateScrollToItem((nextIndex - 1).coerceAtLeast(0))
            delay(160)
            revealStage = 2 // Turn the next lock into a bouncing available node.
            delay(1450)
        } else {
            delay(600)
        }
        onRevealFinished()
    }
    val scope = rememberCoroutineScope()
    val showJump by remember(listState, targetIndex) {
        derivedStateOf { listState.firstVisibleItemIndex > targetIndex + 1 }
    }
    Box(Modifier.fillMaxSize().background(Color.White)) {
        Column(Modifier.fillMaxSize()) {
            StatsHeader(progress, onPesos)
            LazyColumn(
                modifier = Modifier.weight(1f), state = listState,
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 14.dp, bottom = 40.dp)
            ) {
                course.forEachIndexed { unitIndex, unit ->
                    val accessible = unitIndex == 0 ||
                        course[unitIndex - 1].lessons.all { it.id in progress.completed }
                    item(key = "unit_${unit.number}") {
                        UnitBanner(unit, unitIndex,
                            onOpenUnit = { onUnit(unit) },
                            onGuide = { if (accessible) onGuide(unit) else onLocked() }
                        )
                    }
                    unit.lessons.forEachIndexed { lessonIndex, lesson ->
                        item(key = "lesson_${lesson.id}") {
                            val unlocked = isUnlocked(lesson, progress.completed)
                            val rounds = if (reveal?.lessonId == lesson.id && revealStage == 0)
                                reveal?.fromRounds ?: progress.roundsFor(lesson.id)
                            else progress.roundsFor(lesson.id)
                            val waitingForUnlock = reveal?.newlyUnlockedLessonId == lesson.id &&
                                revealStage < 2
                            val direction = when (lessonIndex % 3) {
                                0 -> Arrangement.Center
                                1 -> Arrangement.Start
                                else -> Arrangement.End
                            }
                            Column(Modifier.fillMaxWidth()) {
                                Row(
                                    Modifier.fillMaxWidth().height(137.dp),
                                    horizontalArrangement = direction,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (lessonIndex % 3 == 1) Spacer(Modifier.width(42.dp))
                                    LessonNode(
                                        lesson = lesson,
                                        unitColor = unitColors[unitIndex % unitColors.size],
                                        roundsCompleted = rounds,
                                        golden = lesson.id in progress.golden,
                                        unlocked = unlocked && !waitingForUnlock,
                                        unlockCelebration = reveal?.newlyUnlockedLessonId == lesson.id &&
                                            revealStage >= 2,
                                        onClick = {
                                            if (unlocked && !waitingForUnlock) {
                                                selectedNode = if (selectedNode == lesson.id) null
                                                    else lesson.id
                                            } else onLocked()
                                        }
                                    )
                                    if (lessonIndex % 3 == 2) Spacer(Modifier.width(42.dp))
                                }
                                if (selectedNode == lesson.id && unlocked && !waitingForUnlock) {
                                    val done = progress.roundsFor(lesson.id) >= ROUNDS_PER_LESSON
                                    Column(Modifier.fillMaxWidth()
                                        .clip(RoundedCornerShape(21.dp))
                                        .background(unitColors[unitIndex % unitColors.size])
                                        .padding(17.dp)) {
                                        Text(lesson.title, color = Color.White,
                                            fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                                        Spacer(Modifier.height(5.dp))
                                        Text(if (done) "Level complete" else
                                            "Lesson ${rounds.coerceAtMost(3) + 1} of 4",
                                            color = Color.White.copy(alpha = .94f), fontSize = 15.sp)
                                        Spacer(Modifier.height(12.dp))
                                        DalaButton(if (done) "REVIEW" else "START",
                                            onClick = { selectedNode = null; onLesson(lesson) },
                                            modifier = Modifier.fillMaxWidth(),
                                            color = Color.White,
                                            pressedColor = Color(0xFFD0EBC9),
                                            textColor = LeafDark)
                                    }
                                }
                            }
                        }
                        if (lessonIndex == 1) {
                            val activity = interludeFor(unit)
                            item(key = activity.id) {
                                BonusNode(
                                    activity = activity,
                                    enabled = isActivityUnlocked(activity, progress.completed),
                                    completed = activity.id in progress.completedActivities,
                                    onClick = { if (isActivityUnlocked(activity, progress.completed))
                                        onActivity(activity) else onLocked() }
                                )
                            }
                        }
                    }
                    val story = storyFor(unit)
                    item(key = story.id) {
                        BonusNode(
                            activity = story,
                            enabled = isActivityUnlocked(story, progress.completed),
                            completed = story.id in progress.completedActivities,
                            onClick = { if (isActivityUnlocked(story, progress.completed))
                                onActivity(story) else onLocked() }
                        )
                    }
                    item(key = "gap_${unit.number}") { Spacer(Modifier.height(18.dp)) }
                }
                item(key = "end") {
                    Text("You've reached the end of the course! 🎉",
                        Modifier.fillMaxWidth(), fontSize = 13.sp, color = Muted,
                        textAlign = TextAlign.Center)
                }
            }
        }
        AnimatedVisibility(
            visible = reveal != null && revealStage == 2 &&
                reveal.newlyUnlockedLessonId != null,
            enter = fadeIn() + scaleIn(), exit = fadeOut(),
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 73.dp)
        ) {
            Box(
                Modifier.clip(RoundedCornerShape(23.dp))
                    .background(Color(0xFFFFC800))
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text(if (reveal?.unitCompleted == true) "🎉 UNIT COMPLETE!"
                    else "✨ LESSON UNLOCKED!", color = Color(0xFF785600),
                    fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
            }
        }
        if (showJump) {
            RaisedIconButton(
                glyph = GameGlyph.UP,
                description = "Return to current lesson",
                onClick = {
                    scope.launch {
                        // Show the preceding node or unit header, not a lesson cut off at the top.
                        listState.animateScrollToItem(
                            index = (targetIndex - 2).coerceAtLeast(0),
                            scrollOffset = 0
                        )
                    }
                },
                modifier = Modifier.align(Alignment.BottomEnd).padding(19.dp),
                size = 58.dp, face = Leaf, shade = LeafDark, iconColor = Color.White
            )
        }
    }
}

@Composable
fun LessonNode(
    lesson: Lesson,
    unitColor: Color,
    roundsCompleted: Int,
    golden: Boolean,
    unlocked: Boolean,
    unlockCelebration: Boolean = false,
    onClick: () -> Unit
) {
    val completed = roundsCompleted >= ROUNDS_PER_LESSON
    val ringProgress by animateFloatAsState(
        targetValue = roundsCompleted.toFloat(),
        animationSpec = tween(durationMillis = 600),
        label = "Lesson rounds"
    )
    val unlockScale by animateFloatAsState(
        targetValue = if (unlockCelebration) 1.13f else 1f,
        animationSpec = tween(340), label = "Unlocked node bounce"
    )
    val gold = Color(0xFFFFC800)
    val face = when {
        !unlocked -> Color(0xFFE5E5E5)
        completed && golden -> gold
        completed -> Leaf
        else -> unitColor
    }
    val shade = when {
        !unlocked -> Color(0xFFC6C6C6)
        completed && golden -> Color(0xFFCB9900)
        completed -> LeafDark
        else -> face.copy(red = face.red * .78f, green = face.green * .78f,
            blue = face.blue * .78f)
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(125.dp)) {
        if (unlocked && !completed) {
            Box(Modifier.clip(RoundedCornerShape(9.dp)).background(face)
                .padding(horizontal = 10.dp, vertical = 4.dp)) {
                Text(if (roundsCompleted == 0) "START" else "${roundsCompleted}/4",
                    color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 11.sp)
            }
            Spacer(Modifier.height(3.dp))
        }
        Box(Modifier.size(94.dp)
            .graphicsLayer(scaleX = unlockScale, scaleY = unlockScale)
            .clickable(onClick = onClick), contentAlignment = Alignment.Center) {
            // Only the currently active node has a four-round progress ring.
            if (unlocked && !completed) Canvas(Modifier.fillMaxSize()) {
                val pad = 8.dp.toPx()
                val area = Size(size.width - 2 * pad, size.height - 2 * pad)
                val width = 6.dp.toPx()
                for (segment in 0 until ROUNDS_PER_LESSON) {
                    val start = -90f + 90f * segment + 6f
                    drawArc(
                        color = Color(0xFFDDDDDD), startAngle = start,
                        sweepAngle = 78f, useCenter = false,
                        topLeft = Offset(pad, pad), size = area,
                        style = Stroke(width = width, cap = StrokeCap.Round)
                    )
                    val fill = (ringProgress - segment).coerceIn(0f, 1f)
                    if (fill > 0f) drawArc(
                        color = if (completed && golden) gold else Leaf,
                        startAngle = start, sweepAngle = 78f * fill,
                        useCenter = false, topLeft = Offset(pad, pad),
                        size = area, style = Stroke(width = width, cap = StrokeCap.Round)
                    )
                }
            }
            Box(Modifier.size(68.dp).clip(CircleShape).background(shade)
                .padding(bottom = 5.dp)) {
                Box(Modifier.fillMaxSize().clip(CircleShape).background(face),
                    contentAlignment = Alignment.Center) {
                    Text(when {
                        !unlocked -> "🔒"
                        completed && golden -> "👑"
                        completed -> "✓"
                        else -> lesson.emoji
                    }, fontSize = if (completed && !golden) 34.sp else 30.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White)
                }
            }
        }
        Spacer(Modifier.height(3.dp))
        Text(lesson.title, fontSize = 12.sp, color = if (unlocked) Ink else Muted,
            fontWeight = FontWeight.ExtraBold, maxLines = 1,
            textAlign = TextAlign.Center)
    }
}

/** Different-shaped bonus nodes add visual variety without blocking the lesson path. */
@Composable
private fun BonusNode(
    activity: PathActivity,
    enabled: Boolean,
    completed: Boolean,
    onClick: () -> Unit
) {
    val face = when {
        !enabled -> Color(0xFFE5E5E5)
        completed -> Leaf
        activity.type == PathActivityType.STORY -> Purple
        activity.type == PathActivityType.PICTURE -> Sky
        activity.type == PathActivityType.LISTENING -> Tangerine
        else -> Color(0xFFCE82FF)
    }
    val shade = if (!enabled) Color(0xFFC7C7C7) else Color(
        red = face.red * .78f, green = face.green * .78f,
        blue = face.blue * .78f
    )
    Column(
        Modifier.fillMaxWidth().padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier.size(width = 94.dp, height = 79.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(shade).clickable(onClick = onClick)
                .padding(bottom = 5.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(Modifier.fillMaxSize().clip(RoundedCornerShape(22.dp))
                .background(face), contentAlignment = Alignment.Center) {
                Text(if (completed) "✓" else if (enabled) activity.emoji else "🔒",
                    fontSize = 35.sp, color = Color.White,
                    fontWeight = FontWeight.ExtraBold)
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(activity.title, fontWeight = FontWeight.ExtraBold,
            color = if (enabled) Ink else Muted, fontSize = 12.sp)
        if (completed) Text("COMPLETED", color = LeafDark, fontSize = 10.sp,
            fontWeight = FontWeight.ExtraBold)
    }
}
