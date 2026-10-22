package com.dalawingo.app.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
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
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.dalawingo.app.R
import androidx.compose.material3.Icon
val unitColors = listOf(Leaf, Purple, Sky, Tangerine, Color(0xFFED86AD))

/** A one-time visual reveal, created only after a lesson advances. */
data class PathReveal(
    val token: Long,
    val lessonId: String,
    val fromRounds: Int,
    val toRounds: Int,
    val newlyUnlockedLessonId: String? = null,
    val unitCompleted: Boolean = false,
    val justPerfected: Boolean = false
)

private fun pathIndexForLesson(lessonId: String?): Int {
    var index = 0
    var found = 0
    course.forEach { unit ->
        index++ // Section header
        unit.lessons.forEach { lesson ->
            if (lesson.id == lessonId) found = index
            index++
        }
        index++ // Section separator
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
    val shade = Color(color.red * .77f, color.green * .77f, color.blue * .77f)
    val leftSource = remember { MutableInteractionSource() }
    val rightSource = remember { MutableInteractionSource() }
    val leftDown by leftSource.collectIsPressedAsState()
    val rightDown by rightSource.collectIsPressedAsState()
    val scope = rememberCoroutineScope()
    var leftTapped by remember { mutableStateOf(false) }
    var rightTapped by remember { mutableStateOf(false) }
    fun pressedOpen(left: Boolean) {
        if (leftTapped || rightTapped) return
        if (left) leftTapped = true else rightTapped = true
        scope.launch {
            delay(130)
            if (left) { leftTapped = false; onOpenUnit() }
            else { rightTapped = false; onGuide() }
        }
    }
    Box(modifier.fillMaxWidth().height(99.dp)
        .clip(RoundedCornerShape(19.dp)).background(shade)) {
        Row(Modifier.fillMaxWidth().height(95.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f).fillMaxHeight()
                .offset(y = if (leftDown || leftTapped) 4.dp else 0.dp)
                .clip(RoundedCornerShape(topStart = 19.dp, bottomStart = 19.dp))
                .background(color)
                .clickable(interactionSource = leftSource, indication = null) { pressedOpen(true) }
                .padding(start = 18.dp, top = 13.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.Center) {
                Text("SECTION 1, UNIT ${unit.number}",
                    color = Color.White.copy(alpha = .87f),
                    fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(4.dp))
                Text(unit.title, color = Color.White, fontSize = 21.sp,
                    fontWeight = FontWeight.ExtraBold, maxLines = 1)
            }
            Box(Modifier.width(2.dp).fillMaxHeight()
                .background(Color.White.copy(alpha = .45f)))
            Box(Modifier.width(72.dp).fillMaxHeight()
                .offset(y = if (rightDown || rightTapped) 4.dp else 0.dp)
                .clip(RoundedCornerShape(topEnd = 19.dp, bottomEnd = 19.dp))
                .background(color)
                .clickable(interactionSource = rightSource, indication = null) { pressedOpen(false) },
                contentAlignment = Alignment.Center) {
                DalaIcon(R.drawable.ic_book, "Guidebook", 31.dp, Color.White)
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
            delay(if (event.justPerfected) 1350 else 600)
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
                                        golden = lesson.id in progress.golden &&
                                            !(reveal?.lessonId == lesson.id &&
                                                reveal.justPerfected && revealStage == 0),
                                        justPerfected = reveal?.lessonId == lesson.id &&
                                            reveal.justPerfected && revealStage >= 1,
                                        isCurrent = currentLessonId == lesson.id,
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
                                    val perfectNode = done && lesson.id in progress.golden
                                    val imperfectReview = done && !perfectNode
                                    Column(Modifier.fillMaxWidth()
                                        .clip(RoundedCornerShape(21.dp))
                                        .background(if (perfectNode)
                                            Brush.linearGradient(listOf(Color(0xFFFFE365),
                                                Color(0xFFFFB900)))
                                            else Brush.linearGradient(listOf(
                                                unitColors[unitIndex % unitColors.size],
                                                unitColors[unitIndex % unitColors.size])))
                                        .then(if (perfectNode)
                                            Modifier.border(3.dp, Color(0xFFFFDF65), RoundedCornerShape(21.dp))
                                            else Modifier)
                                        .padding(17.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(lesson.title, color = if (perfectNode)
                                                Color(0xFF865D08) else Color.White,
                                                fontSize = 20.sp, fontWeight = FontWeight.ExtraBold,
                                                modifier = Modifier.weight(1f))
                                            if (perfectNode) DalaIcon(R.drawable.ic_sparkle,
                                                "Perfect!", 29.dp, Color.White)
                                        }
                                        Spacer(Modifier.height(5.dp))
                                        Text(when {
                                            perfectNode -> "You completed this lesson!"
                                            done -> "Get every answer right to make it perfect"
                                            else -> "Lesson ${rounds.coerceAtMost(3) + 1} of 4"
                                        }, color = if (perfectNode) Color(0xFF936C09)
                                            else Color.White.copy(alpha = .94f), fontSize = 14.sp)
                                        Spacer(Modifier.height(12.dp))
                                        if (imperfectReview) {
                                            DalaButton("PRACTICE · UP TO 27 XP",
                                                onClick = { selectedNode = null; onLesson(lesson) },
                                                modifier = Modifier.fillMaxWidth(),
                                                color = Color.White,
                                                pressedColor = Color(0xFFD0EBC9),
                                                textColor = LeafDark)
                                            Spacer(Modifier.height(9.dp))
                                            PerfectRewardButton {
                                                selectedNode = null
                                                onLesson(lesson)
                                            }
                                        } else {
                                            DalaButton(if (perfectNode) "PRACTICE · UP TO 27 XP"
                                                else "START · UP TO 27 XP",
                                                onClick = { selectedNode = null; onLesson(lesson) },
                                                modifier = Modifier.fillMaxWidth(),
                                                color = Color.White,
                                                pressedColor = if (perfectNode) Color(0xFFE6B641)
                                                    else Color(0xFFD0EBC9),
                                                textColor = if (perfectNode) Color(0xFF926700)
                                                    else LeafDark)
                                        }
                                    }
                                }
                            }
                        }
                    }
                    item(key = "gap_${unit.number}") { Spacer(Modifier.height(18.dp)) }
                }
                item(key = "end") {
                    Text("You've reached the end of the course!",
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
                Text(if (reveal?.unitCompleted == true) "UNIT COMPLETE!"
                    else "LESSON UNLOCKED!", color = Color(0xFF785600),
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
    justPerfected: Boolean = false,
    isCurrent: Boolean = false,
    unlockCelebration: Boolean = false,
    onClick: () -> Unit
) {
    val completed = roundsCompleted >= ROUNDS_PER_LESSON

    val ringProgress by animateFloatAsState(
        targetValue = roundsCompleted.toFloat(),
        animationSpec = tween(durationMillis = 600),
        label = "Lesson rounds"
    )

    val burst by animateFloatAsState(
        targetValue = if (unlockCelebration) 1f else 0f,
        animationSpec = tween(650), label = "Unlocked sparkle burst")

    val unlockScale by animateFloatAsState(
        targetValue = if (unlockCelebration) 1.13f else 1f,
        animationSpec = tween(340),
        label = "Unlocked node bounce"
    )

    val attention = rememberInfiniteTransition(label = "Current lesson attention")
    val breathe by attention.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1800, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "Active ring pulse"
    )
    val starJump = if (isCurrent && unlocked && !completed && breathe > .72f)
        ((breathe - .72f) / .28f) * 9f else 0f
    // Reveal the opened lock briefly, then switch to the unlocked star.
    // This state only runs during a newly-unlocked node's one-time reveal.
    var showOpenLock by remember(unlockCelebration) { mutableStateOf(unlockCelebration) }
    LaunchedEffect(unlockCelebration) {
        if (unlockCelebration) {
            showOpenLock = true
            delay(420)
            showOpenLock = false
        }
    }
    val gold = Color(0xFFFFC800)
    val goldShine by attention.animateFloat(
        initialValue = .3f, targetValue = .95f,
        animationSpec = infiniteRepeatable(tween(1250), RepeatMode.Reverse),
        label = "Perfect lesson shine")
    val goldReveal = remember { androidx.compose.animation.core.Animatable(0f) }
    LaunchedEffect(justPerfected) {
        if (justPerfected) {
            goldReveal.snapTo(0f)
            goldReveal.animateTo(1.2f, tween(500, easing = FastOutSlowInEasing))
            goldReveal.animateTo(1f, tween(280))
        } else goldReveal.snapTo(0f)
    }

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
        else -> face.copy(
            red = face.red * .78f,
            green = face.green * .78f,
            blue = face.blue * .78f
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(125.dp)
    ) {
        Box(
            Modifier
                .size(106.dp)
                .graphicsLayer(
                    scaleX = unlockScale * if (isCurrent && !completed) 1f + breathe * .05f else 1f,
                    scaleY = unlockScale * if (isCurrent && !completed) 1f + breathe * .05f else 1f
                )
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            if (unlocked && !completed && isCurrent) {
                Canvas(Modifier.fillMaxSize()) {
                    val pad = 8.dp.toPx()
                    val area = Size(
                        size.width - 2 * pad,
                        size.height - 2 * pad
                    )
                    val width = 6.dp.toPx()

                    for (segment in 0 until ROUNDS_PER_LESSON) {
                        val start = -90f + 90f * segment + 6f

                        drawArc(
                            color = Color(0xFFDDDDDD),
                            startAngle = start,
                            sweepAngle = 78f,
                            useCenter = false,
                            topLeft = Offset(pad, pad),
                            size = area,
                            style = Stroke(
                                width = width,
                                cap = StrokeCap.Round
                            )
                        )

                        val fill = (ringProgress - segment)
                            .coerceIn(0f, 1f)

                        if (fill > 0f) {
                            drawArc(
                                color = if (completed && golden) gold else Leaf,
                                startAngle = start,
                                sweepAngle = 78f * fill,
                                useCenter = false,
                                topLeft = Offset(pad, pad),
                                size = area,
                                style = Stroke(
                                    width = width,
                                    cap = StrokeCap.Round
                                )
                            )
                        }
                    }
                }
            }

            if (completed && golden) {
                // Perfect nodes never draw a progress ring. Their shimmer is clipped
                // INSIDE the golden face below, rather than wrapping around it.
                if (justPerfected) {
                    DalaIcon(R.drawable.ic_sparkle, null, 30.dp, Color(0xFFFFCC29),
                        Modifier.offset(x = 37.dp, y = (-39).dp)
                            .graphicsLayer(scaleX = goldReveal.value,
                                scaleY = goldReveal.value, rotationZ = goldReveal.value * 45f))
                }
            }
            if (unlockCelebration) {
                listOf(-1f to -1f, 1f to -1f, -1f to 1f, 1f to 1f).forEach { (dx, dy) ->
                    DalaIcon(R.drawable.ic_sparkle, null, 23.dp, Color(0xFFFFC800),
                        Modifier.offset(x = (dx * 36f * burst).dp,
                            y = (dy * 36f * burst).dp)
                            .graphicsLayer(alpha = burst, rotationZ = burst * 65f))
                }
            }
            Box(
                Modifier
                    .size(68.dp)
                    .clip(CircleShape)
                    .background(shade)
                    .padding(bottom = 5.dp)
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(face),
                    contentAlignment = Alignment.Center
                ) {
                    // Shimmer INSIDE the gold button, behind its checkmark.
                    if (completed && golden) {
                        Canvas(Modifier.fillMaxSize()) {
                            val travel = ((goldShine - .3f) / .65f).coerceIn(0f, 1f)
                            val x = (travel * 1.6f - .3f) * size.width
                            drawLine(
                                color = Color.White.copy(alpha = .24f),
                                start = Offset(x - 24.dp.toPx(), 0f),
                                end = Offset(x + 12.dp.toPx(), size.height),
                                strokeWidth = 14.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                        }
                    }
                    when {
                        !unlocked -> {
                            DalaIcon(
                                resource = R.drawable.ic_lock_colored,
                                description = "Locked lesson",
                                size = 36.dp
                            )
                        }

                        unlockCelebration && showOpenLock -> {
                            DalaIcon(R.drawable.ic_unlock_colored, "Lesson unlocking", 37.dp,
                                Color.White)
                        }

                        completed && golden -> {
                            DalaIcon(R.drawable.ic_check, "Perfect lesson", 47.dp, Color.White,
                                Modifier.graphicsLayer(scaleX = 1f + goldReveal.value * .13f,
                                    scaleY = 1f + goldReveal.value * .13f))
                        }

                        completed -> {
                            Icon(
                                painter = painterResource(R.drawable.ic_check),
                                contentDescription = "Completed",
                                modifier = Modifier.size(47.dp),
                                tint = Color.White
                            )
                        }

                        else -> {
                            DalaIcon(R.drawable.ic_star, "Current lesson", 36.dp, Color.White,
                                Modifier.offset(y = (-starJump).dp)
                                    .graphicsLayer(rotationZ = if (starJump > 0) starJump * 1.7f else 0f))
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(3.dp))

        Text(
            lesson.title,
            fontSize = 12.sp,
            color = if (unlocked) Ink else Muted,
            fontWeight = FontWeight.ExtraBold,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
    }
}

/** Different-shaped bonus nodes add visual variety without blocking the lesson path. */
@Composable
private fun BonusNode(
    activity: PathActivity,
    enabled: Boolean,
    completed: Boolean,
    unitColor: Color,
    onClick: () -> Unit
) {
    val face = when {
        !enabled -> Color(0xFFE5E5E5)
        completed -> Leaf
        // Bonus nodes inherit their SECTION colour, including Section 1 stories.
        else -> unitColor
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
            Modifier.size(68.dp)
                .clip(CircleShape)
                .background(shade).clickable(onClick = onClick)
                .padding(bottom = 5.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(Modifier.fillMaxSize().clip(CircleShape)
                .background(face), contentAlignment = Alignment.Center) {
                // Same icon when locked, only desaturated. No generic lock substitution.
                val activityIcon = when (activity.type) {
                    PathActivityType.STORY -> R.drawable.ic_book
                    PathActivityType.PICTURE -> R.drawable.ic_star
                    PathActivityType.LISTENING -> R.drawable.ic_headphone
                    else -> R.drawable.ic_refresh
                }
                DalaIcon(if (completed) R.drawable.ic_check else activityIcon,
                    activity.title, 34.dp,
                    if (enabled) Color.White else Color(0xFFAAAAAA))
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(activity.title, fontWeight = FontWeight.ExtraBold,
            color = if (enabled) Ink else Muted, fontSize = 12.sp)
    }
}

/** Custom inline reward: the coin is a real drawable, never a text emoji. */
@Composable
private fun PerfectRewardButton(onClick: () -> Unit) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    var firing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    Box(Modifier.fillMaxWidth().height(57.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(Color(0xFFC78B00))
        .clickable(interactionSource = interaction, indication = null) {
            if (!firing) {
                firing = true
                scope.launch {
                    delay(120)
                    firing = false
                    onClick()
                }
            }
        }) {
        Row(Modifier.fillMaxWidth().height(53.dp)
            .offset(y = if (pressed || firing) 4.dp else 0.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color(0xFFFFD02C)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Text("MAKE PERFECT", color = Color(0xFF825A00),
                fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.width(8.dp))
            DalaIcon(R.drawable.ic_peso_coin, "Peso reward", 23.dp)
            Text("+10", color = Color(0xFF825A00),
                fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
        }
    }
}
