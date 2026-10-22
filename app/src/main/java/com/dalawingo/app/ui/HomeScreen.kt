package com.dalawingo.app.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap
import com.dalawingo.app.R
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dalawingo.app.data.LearnerProgress
import com.dalawingo.app.data.questStats
import com.dalawingo.app.model.DailyQuest
import com.dalawingo.app.model.QuestStats
import com.dalawingo.app.model.QuestTier
import com.dalawingo.app.model.findDailyQuest
import com.dalawingo.app.model.selectDailyQuests
import com.dalawingo.app.model.allLessons
import com.dalawingo.app.model.nextLesson
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun StatsHeader(progress: LearnerProgress, onPesos: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier.fillMaxWidth().height(54.dp).padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            DalaIcon(R.drawable.ic_ph_flag, "Tagalog", 27.dp)
            Spacer(Modifier.width(5.dp))
            Text("TL", color = Ink, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
        }
        HeaderResourceChip(R.drawable.ic_fire, "Streak", "${progress.streak}", Tangerine)
        HeaderResourceChip(R.drawable.ic_peso_coin, "Pesos", "${progress.pesos}",
            Tangerine, onClick = onPesos)
    }
}

@Composable
private fun HeaderResourceChip(
    icon: Int, description: String, value: String, color: Color,
    onClick: (() -> Unit)? = null
) {
    Row(
        Modifier.then(if (onClick != null) Modifier.clickable(onClick = onClick)
            else Modifier).padding(horizontal = 5.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        DalaIcon(icon, description, 27.dp)
        Text(value, color = color, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
    }
}

private val dailyMessages = listOf(
    "It's giving bilingual.",
    "Your Tagalog era starts now.",
    "One round. Big moves.",
    "Plot twist: you're getting good.",
    "Main character energy.",
    "Small steps. Big salamat.",
    "Let's make it count.",
    "Future you says salamat."
)

/** Local-midnight countdown; it updates without restarting the app. */
@Composable
private fun QuestResetCountdown(now: LocalDateTime) {
    val remaining = Duration.between(
        now, now.toLocalDate().plusDays(1).atStartOfDay()
    ).coerceAtLeast(Duration.ZERO)
    val hours = remaining.toMinutes() / 60
    val minutes = remaining.toMinutes() % 60
    Row(verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        GameIcon(GameGlyph.CLOCK, Modifier.size(16.dp), Muted)
        Text("${hours}h ${minutes.toString().padStart(2, '0')}m",
            color = Muted, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun HomeScreen(
    progress: LearnerProgress,
    onContinue: () -> Unit,
    onPractice: () -> Unit,
    onLearn: () -> Unit,
    onPesos: () -> Unit,
    onCustomize: () -> Unit,
    celebratePractice: Boolean = false,
    onCelebrationShown: () -> Unit = {}
) {
    // Hoist the clock so the entire Player screen refreshes at local midnight.
    var clockNow by remember { mutableStateOf(LocalDateTime.now()) }
    LaunchedEffect(Unit) {
        while (true) {
            clockNow = LocalDateTime.now()
            delay(30_000L)
        }
    }
    val today = clockNow.toLocalDate()
    val isToday = progress.dailyDate == today.toString()
    val completedToday = isToday && progress.dailyPractices > 0
    val stats = if (isToday) progress.questStats() else QuestStats()
    val quests = if (isToday && progress.dailyQuestIds.size == 3) {
        progress.dailyQuestIds.mapNotNull(::findDailyQuest)
    } else selectDailyQuests(today.toEpochDay())
    val claimed = if (isToday) progress.dailyQuestClaimed else emptySet()
    val next = nextLesson(progress.completed)

    BoxWithConstraints(Modifier.fillMaxSize().background(Color.White)) {
    val compact = maxHeight < 820.dp
    Column(Modifier.fillMaxSize()) {
        // Single full-bleed background: header + pet + edit button, no white gutters.
        PetPreview(
            progress = progress,
            modifier = Modifier.fillMaxWidth().height(if (compact) 224.dp else 251.dp),
            onEdit = onCustomize,
            embedHeader = true,
            onPesos = onPesos
        )
        Spacer(Modifier.height(if (compact) 8.dp else 14.dp))
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Heading("Daily quests", 21.sp)
            QuestResetCountdown(clockNow)
        }
        Spacer(Modifier.height(if (compact) 5.dp else 9.dp))
        DailyPracticeHero(
            completedToday = completedToday,
            nextTitle = next?.title,
            completedCount = progress.completed.size,
            dailyMessage = dailyMessages[(today.toEpochDay() % dailyMessages.size).toInt()],
            onPractice = onPractice,
            onContinue = if (next == null) onLearn else onContinue,
            celebrate = celebratePractice,
            onCelebrationShown = onCelebrationShown
        )
        Spacer(Modifier.height(if (compact) 5.dp else 8.dp))
        Column(
            Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(if (compact) 3.dp else 6.dp)
        ) {
            quests.forEach { quest ->
                QuestItem(quest, quest.progress(stats), quest.id in claimed, compact)
            }
        }
    }
    }
}

@Composable
private fun DailyPracticeHero(
    completedToday: Boolean,
    nextTitle: String?,
    completedCount: Int,
    dailyMessage: String,
    onPractice: () -> Unit,
    onContinue: () -> Unit,
    celebrate: Boolean,
    onCelebrationShown: () -> Unit
) {
    val darkGold = Color(0xFF67420F)
    val glint = rememberInfiniteTransition(label = "Practice sparkle")
    val sparkle by glint.animateFloat(.35f, .88f,
        animationSpec = infiniteRepeatable(tween(1850), RepeatMode.Reverse),
        label = "Practice coin sparkle")
    val trophyScale = remember { Animatable(1f) }
    LaunchedEffect(celebrate) {
        if (celebrate && completedToday) {
            trophyScale.snapTo(0.55f)
            trophyScale.animateTo(1.20f, tween(430, easing = FastOutSlowInEasing))
            trophyScale.animateTo(1f, tween(240))
            onCelebrationShown()
        }
    }
    Column(
        Modifier.fillMaxWidth().padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(23.dp))
            .background(Brush.linearGradient(
                if (completedToday) listOf(Color(0xFFEEB43E), Color(0xFFFFD978))
                else listOf(Leaf, Color(0xFF7DE020))
            )).padding(horizontal = 16.dp, vertical = 11.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                if (completedToday) "PRACTICE COMPLETE" else "DAILY PRACTICE",
                color = if (completedToday) darkGold else Color.White,
                fontSize = 11.sp, fontWeight = FontWeight.ExtraBold,
                letterSpacing = .7.sp, modifier = Modifier.weight(1f)
            )
            if (completedToday) {
                DalaIcon(R.drawable.ic_trophy, "Practice complete", 28.dp,
                    darkGold, Modifier.graphicsLayer(scaleX = trophyScale.value, scaleY = trophyScale.value))
            } else {
                Box(Modifier.clip(RoundedCornerShape(13.dp))
                    .background(Color.White.copy(alpha = .96f))
                    .padding(horizontal = 10.dp, vertical = 5.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        DalaIcon(R.drawable.ic_sparkle, null, 13.dp, Color(0xFFFFCB4A),
                            Modifier.graphicsLayer(alpha = sparkle))
                        Spacer(Modifier.width(3.dp))
                        DalaIcon(R.drawable.ic_peso_coin, null, 21.dp)
                        Spacer(Modifier.width(3.dp))
                        Text("+25", color = LeafDark, fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
        }
        if (completedToday) {
            Spacer(Modifier.height(6.dp))
            Text(nextTitle ?: "All lessons finished!", color = darkGold,
                fontSize = 20.sp, lineHeight = 24.sp,
                fontWeight = FontWeight.ExtraBold, maxLines = 2)
            Spacer(Modifier.height(8.dp))
            ProgressLine(
                completedCount.toFloat() / allLessons.size.coerceAtLeast(1),
                darkGold.copy(alpha = .84f), Modifier.fillMaxWidth(), 9.dp
            )
        }
        Spacer(Modifier.height(8.dp))
        DailyPracticeAction(
            label = when {
                !completedToday -> dailyMessage
                nextTitle != null -> "CONTINUE LEARNING"
                else -> "REVIEW LESSONS"
            },
            onClick = if (completedToday) onContinue else onPractice,
            textColor = if (completedToday) darkGold else LeafDark,
            bottomColor = if (completedToday) Color(0xFFE2C27D) else Color(0xFFCAE9B6)
        )
    }
}

/** A full-width raised daily action with its arrow at the right edge, not embedded in text. */
@Composable
private fun DailyPracticeAction(
    label: String,
    onClick: () -> Unit,
    textColor: Color,
    bottomColor: Color
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    var activated by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val motion = rememberInfiniteTransition(label = "Practice call to action")
    val arrowNudge by motion.animateFloat(
        initialValue = 0f, targetValue = 7f,
        animationSpec = infiniteRepeatable(tween(850, easing = FastOutSlowInEasing),
            RepeatMode.Reverse), label = "Practice arrow bounce")
    Box(Modifier.fillMaxWidth().height(57.dp)
        .clip(RoundedCornerShape(17.dp)).background(bottomColor)
        .clickable(interactionSource = interaction, indication = null) {
            if (!activated) {
                activated = true
                scope.launch { delay(110); activated = false; onClick() }
            }
        }) {
        Row(Modifier.fillMaxWidth().height(53.dp)
            .offset(y = if (pressed || activated) 4.dp else 0.dp)
            .clip(RoundedCornerShape(16.dp)).background(Color.White)
            .padding(start = 18.dp, end = 15.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Text(label.uppercase(), modifier = Modifier.weight(1f), maxLines = 1,
                fontWeight = FontWeight.ExtraBold, color = textColor,
                fontSize = 14.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            DalaIcon(
                resource = R.drawable.ic_arrow_right,
                description = "Go",
                size = 36.dp,
                modifier = Modifier.offset(
                    x = if (pressed || activated) 0.dp else arrowNudge.dp
                )
            )
        }
    }
}

/** Compact daily quests: larger glossy bars with rewards on the right. */
@Composable
private fun QuestItem(quest: DailyQuest, count: Int, claimed: Boolean, compact: Boolean) {
    val fraction = (count.toFloat() / quest.target.coerceAtLeast(1)).coerceIn(0f, 1f)
    val done = claimed || fraction >= 1f
    val fill = if (done) Color(0xFFFFC928) else Leaf
    Column(Modifier.fillMaxWidth().padding(vertical = if (compact) 2.dp else 5.dp)) {
        Text(quest.title, color = Ink, fontSize = 15.sp,
            fontWeight = FontWeight.ExtraBold, maxLines = 1)
        Spacer(Modifier.height(5.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            // The bar takes all available width; the coin and its reward stack vertically.
            Box(Modifier.weight(1f).height(22.dp)
                .clip(RoundedCornerShape(12.dp)).background(Color(0xFFE6E6E6))) {
                Box(Modifier.fillMaxHeight().fillMaxWidth(fraction)
                    .clip(RoundedCornerShape(16.dp)).background(fill)) {
                    // Raised Duolingo-style glossy stripe, following the filled portion.
                    Box(Modifier.fillMaxWidth().padding(horizontal = 5.dp, vertical = 2.dp)
                        .height(5.dp).clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = .40f)))
                }
                Text("${count.coerceAtMost(quest.target)} / ${quest.target}",
                    modifier = Modifier.align(Alignment.Center),
                    fontWeight = FontWeight.ExtraBold,
                    color = if (fraction > .58f) Color.White else Ink,
                    fontSize = 12.sp, lineHeight = 14.sp)
            }
            Column(Modifier.width(46.dp).height(45.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                if (done) {
                    DalaIcon(R.drawable.ic_check, "Quest completed", 35.dp, Color(0xFFE0AF00))
                } else {
                    DalaIcon(R.drawable.ic_peso_coin, "Peso reward", 32.dp)
                    Text("+${quest.tier.pesos}", color = Ink,
                        fontSize = 13.sp, fontWeight = FontWeight.ExtraBold,
                        lineHeight = 14.sp)
                }
            }
        }
    }
}
