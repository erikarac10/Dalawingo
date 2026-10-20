package com.dalawingo.app.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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

@Composable
fun StatsHeader(progress: LearnerProgress, onPesos: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("🇵🇭", fontSize = 27.sp)
            Spacer(Modifier.width(5.dp))
            Text("TL", color = Ink, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
        }
        StatChip("🔥", "${progress.streak}", Tangerine)
        StatChip("🪙", "${progress.pesos}", Tangerine, onClick = onPesos)
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

    Column(
        Modifier.fillMaxSize().background(Color.White).verticalScroll(rememberScrollState())
            .padding(bottom = 10.dp)
    ) {
        // Single full-bleed background: header + pet + edit button, no white gutters.
        PetPreview(
            progress = progress,
            modifier = Modifier.fillMaxWidth().height(252.dp),
            onEdit = onCustomize,
            embedHeader = true,
            onPesos = onPesos
        )
        Spacer(Modifier.height(17.dp))
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Heading("Daily quests", 21.sp)
            QuestResetCountdown(clockNow)
        }
        Spacer(Modifier.height(11.dp))
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
        Spacer(Modifier.height(11.dp))
        Column(
            Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            quests.forEach { quest ->
                QuestItem(quest, quest.progress(stats), quest.id in claimed)
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
            )).padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                if (completedToday) "✓  PRACTICE COMPLETE" else "DAILY PRACTICE",
                color = if (completedToday) darkGold else Color.White,
                fontSize = 11.sp, fontWeight = FontWeight.ExtraBold,
                letterSpacing = .7.sp, modifier = Modifier.weight(1f)
            )
            if (completedToday) {
                Text("🏆✨", modifier = Modifier.graphicsLayer(
                    scaleX = trophyScale.value, scaleY = trophyScale.value
                ), fontSize = 24.sp)
            } else {
                Box(Modifier.clip(RoundedCornerShape(13.dp))
                    .background(Color.White.copy(alpha = .96f))
                    .padding(horizontal = 10.dp, vertical = 5.dp)) {
                    Text("🪙 +25", color = LeafDark, fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold)
                }
            }
        }
        Spacer(Modifier.height(9.dp))
        Text(
            if (completedToday) nextTitle ?: "All lessons finished!" else dailyMessage,
            color = if (completedToday) darkGold else Color.White,
            fontSize = 23.sp, lineHeight = 27.sp, fontWeight = FontWeight.ExtraBold,
            maxLines = 2
        )
        if (completedToday) {
            Spacer(Modifier.height(8.dp))
            ProgressLine(
                completedCount.toFloat() / allLessons.size.coerceAtLeast(1),
                darkGold.copy(alpha = .84f), Modifier.fillMaxWidth(), 9.dp
            )
        }
        Spacer(Modifier.height(13.dp))
        DalaButton(
            when {
                !completedToday -> "START PRACTICE  →"
                nextTitle != null -> "CONTINUE LEARNING  →"
                else -> "REVIEW LESSONS  →"
            },
            if (completedToday) onContinue else onPractice,
            Modifier.fillMaxWidth(),
            color = Color.White,
            pressedColor = if (completedToday) Color(0xFFE2C27D) else Color(0xFFCAE9B6),
            textColor = if (completedToday) darkGold else LeafDark
        )
    }
}

/** Compact quest rows inspired by Duolingo's open, orange progress-bar layout. */
@Composable
private fun QuestItem(quest: DailyQuest, count: Int, claimed: Boolean) {
    val fraction = (count.toFloat() / quest.target.coerceAtLeast(1)).coerceIn(0f, 1f)
    val orange = Color(0xFFFF9600)
    val chest = when (quest.tier) {
        QuestTier.BRONZE -> "📦"
        QuestTier.SILVER -> "🧰"
        QuestTier.GOLD -> "🎁"
    }
    Row(Modifier.fillMaxWidth().padding(horizontal = 5.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        Column(Modifier.weight(1f)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(quest.title, color = Ink, modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 2)
                Text(if (claimed) "✓" else "🪙 +${quest.tier.pesos}",
                    fontWeight = FontWeight.ExtraBold, fontSize = 11.sp,
                    color = if (claimed) LeafDark else Muted)
            }
            Spacer(Modifier.height(8.dp))
            Box(Modifier.fillMaxWidth().height(18.dp)
                .clip(RoundedCornerShape(20.dp)).background(Color(0xFFE5E5E5))) {
                Box(Modifier.fillMaxHeight().fillMaxWidth(fraction)
                    .clip(RoundedCornerShape(20.dp)).background(orange))
                Text("${count.coerceAtMost(quest.target)} / ${quest.target}",
                    modifier = Modifier.align(Alignment.Center),
                    fontWeight = FontWeight.Bold,
                    color = if (fraction > .53f) Color.White else Muted,
                    fontSize = 12.sp)
            }
        }
        Box(Modifier.size(58.dp), contentAlignment = Alignment.Center) {
            Text(chest, fontSize = 38.sp)
        }
    }
}
