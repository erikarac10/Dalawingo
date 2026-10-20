package com.dalawingo.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.dalawingo.app.data.LearnerProgress
import com.dalawingo.app.data.currentValue
import com.dalawingo.app.data.tagalogScore
import com.dalawingo.app.model.AchievementDefinition
import com.dalawingo.app.model.VocabularyLevel
import com.dalawingo.app.model.achievements
import com.dalawingo.app.model.allPhrases
import com.dalawingo.app.model.vocabularyKey
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.delay

@Composable
fun ProfileScreen(
    progress: LearnerProgress,
    onClaimAchievement: (String) -> Boolean,
    onActivateBoost: () -> Boolean
) {
    var selected by remember { mutableStateOf<AchievementDefinition?>(null) }
    var showVocabulary by remember { mutableStateOf(false) }
    val achievementsScroll = rememberScrollState()
    var nowMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(progress.xpBoostUntilMillis) {
        nowMillis = System.currentTimeMillis()
        while (nowMillis < progress.xpBoostUntilMillis) {
            delay(1000L)
            nowMillis = System.currentTimeMillis()
        }
    }
    val boostSeconds = ((progress.xpBoostUntilMillis - nowMillis).coerceAtLeast(0L) + 999L) / 1000L
    val orderedAchievements = achievements.sortedWith(compareBy<AchievementDefinition> {
        when {
            it.id in progress.achievementUnlockedAt && it.id !in progress.claimedAchievements -> 0
            it.id in progress.claimedAchievements -> 1
            else -> 2
        }
    })

    Column(
        Modifier.fillMaxSize().background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 19.dp)
    ) {
        Heading("Your Progress", 27.sp)
        Spacer(Modifier.height(20.dp))
        Heading("Statistics", 21.sp)
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ProgressStat("🔥", "${progress.streak}", "Day streak", Modifier.weight(1f))
            ProgressStat("⚡", "${progress.xp}", "Total XP", Modifier.weight(1f))
        }
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ProgressStat("📖", "${progress.completed.size}", "Lessons completed", Modifier.weight(1f))
            ProgressStat("⭐", "${progress.golden.size}", "Golden lessons", Modifier.weight(1f))
        }

        Spacer(Modifier.height(18.dp))
        SurfaceCard(Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("TAGALOG SCORE", color = LeafDark,
                        fontWeight = FontWeight.ExtraBold, fontSize = 12.sp,
                        letterSpacing = 1.sp)
                    Text("Learning progress", color = Muted, fontSize = 12.sp)
                }
                Text("${progress.tagalogScore()}/100", color = LeafDark,
                    fontSize = 27.sp, fontWeight = FontWeight.ExtraBold)
            }
            Spacer(Modifier.height(10.dp))
            ProgressLine(progress.tagalogScore() / 100f, Leaf,
                Modifier.fillMaxWidth(), 11.dp)
        }

        Spacer(Modifier.height(24.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Heading("Vocabulary mastery", 21.sp, Modifier.weight(1f))
            RaisedIconButton(
                GameGlyph.CHEVRON, "View vocabulary", { showVocabulary = true },
                size = 36.dp, face = Pale, shade = Border, iconColor = LeafDark
            )
        }
        Spacer(Modifier.height(12.dp))
        SurfaceCard(Modifier.fillMaxWidth()) {
            val total = progress.vocabulary.size
            val mastered = progress.vocabulary.values.count { it.level == VocabularyLevel.MASTERED }
            val developing = progress.vocabulary.values.count { it.level == VocabularyLevel.DEVELOPING }
            val weak = progress.vocabulary.values.count { it.level == VocabularyLevel.WEAK }
            val fresh = total - mastered - developing - weak
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("$total phrases", fontSize = 24.sp, color = Ink, fontWeight = FontWeight.ExtraBold)
                    Text("Vocabulary encountered", color = Muted, fontSize = 12.sp)
                }
                Text("📚", fontSize = 34.sp)
            }
            Spacer(Modifier.height(13.dp))
            Row(Modifier.fillMaxWidth().height(11.dp).clip(RoundedCornerShape(9.dp))
                .background(Color(0xFFEAEAEA))) {
                if (total > 0) {
                    if (mastered > 0) Box(Modifier.weight(mastered.toFloat()).fillMaxHeight().background(Leaf))
                    if (developing > 0) Box(Modifier.weight(developing.toFloat()).fillMaxHeight().background(Color(0xFFE4BD49)))
                    if (weak > 0) Box(Modifier.weight(weak.toFloat()).fillMaxHeight().background(Tangerine))
                    if (fresh > 0) Box(Modifier.weight(fresh.toFloat()).fillMaxHeight().background(Sky))
                }
            }
            Spacer(Modifier.height(10.dp))
            Text("$mastered mastered  ·  $developing developing", color = Ink, fontSize = 12.sp)
            Text("$weak weak  ·  $fresh new", color = Muted, fontSize = 12.sp)
            val overdue = progress.vocabulary.values.count { it.isDue(LocalDate.now().toEpochDay()) }
            if (overdue > 0) {
                Spacer(Modifier.height(7.dp))
                Text("🔁 $overdue due for review", color = LeafDark, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }

        Spacer(Modifier.height(24.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Heading("Achievements", 21.sp, Modifier.weight(1f))

        }
        Spacer(Modifier.height(12.dp))
        Row(
            Modifier.fillMaxWidth().horizontalScroll(achievementsScroll),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            orderedAchievements.forEach { achievement ->
                val unlocked = achievement.id in progress.achievementUnlockedAt
                val claimed = achievement.id in progress.claimedAchievements
                val ready = unlocked && !claimed
                Column(
                    Modifier.width(142.dp).height(172.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(if (ready) Color(0xFFFFF6DD) else Color.White)
                        .border(2.dp, if (ready) Color(0xFFEFC257) else Border, RoundedCornerShape(18.dp))
                        .clickable { selected = achievement }
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(if (unlocked) achievement.emoji else "🔒", fontSize = 45.sp)
                    Text(achievement.title, color = Ink, fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center, maxLines = 2)
                    Text(
                        when {
                            ready -> "🎁 CLAIM REWARD"
                            claimed -> "✓ CLAIMED"
                            else -> "${achievement.currentValue(progress).coerceAtMost(achievement.target)}/${achievement.target}"
                        },
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = if (ready) Color(0xFF976B0A) else if (claimed) LeafDark else Muted
                    )
                }
            }
        }
        Spacer(Modifier.height(11.dp))
        // A visible, scroll-position-aware track replaces the instructional "SWIPE" text.
        BoxWithConstraints(Modifier.fillMaxWidth()) {
            val viewportPx = constraints.maxWidth.toFloat().coerceAtLeast(1f)
            val allPx = viewportPx + achievementsScroll.maxValue
            val visibleFraction = (viewportPx / allPx).coerceIn(.16f, 1f)
            val thumbWidth = maxWidth * visibleFraction
            val travel = maxWidth - thumbWidth
            val fraction = achievementsScroll.value.toFloat() /
                achievementsScroll.maxValue.coerceAtLeast(1).toFloat()
            Box(Modifier.fillMaxWidth().height(6.dp)
                .clip(RoundedCornerShape(10.dp)).background(Border)) {
                Box(Modifier.offset(x = travel * fraction).width(thumbWidth)
                    .fillMaxHeight().clip(RoundedCornerShape(10.dp)).background(Leaf))
            }
        }

        if (progress.xpBoostTokens > 0 || boostSeconds > 0) {
            Spacer(Modifier.height(24.dp))
            Heading("XP Boosts", 21.sp)
            Spacer(Modifier.height(12.dp))
            SurfaceCard(Modifier.fillMaxWidth()) {
                Text("⚡  2× XP · 15 minutes", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Ink)
                Spacer(Modifier.height(6.dp))
                Text(
                    if (boostSeconds > 0) "Active · ${boostSeconds / 60}:${(boostSeconds % 60).toString().padStart(2, '0')} remaining"
                    else "${progress.xpBoostTokens} unused boost${if (progress.xpBoostTokens == 1) "" else "s"}",
                    color = Muted, fontSize = 13.sp
                )
                Spacer(Modifier.height(12.dp))
                if (boostSeconds > 0) {
                    Text("Your lesson and practice XP is doubled until the timer ends.", color = LeafDark, fontSize = 12.sp)
                } else {
                    DalaButton("ACTIVATE 2× XP", { onActivateBoost() }, Modifier.fillMaxWidth(),
                        enabled = progress.xpBoostTokens > 0)
                }
            }
        }
        Spacer(Modifier.height(28.dp))
    }

    selected?.let { achievement ->
        AchievementDetails(
            achievement = achievement,
            progress = progress,
            onDismiss = { selected = null },
            onClaim = { onClaimAchievement(achievement.id) }
        )
    }
    if (showVocabulary) {
        VocabularyDetails(progress) { showVocabulary = false }
    }
}

@Composable
private fun ProgressStat(emoji: String, amount: String, label: String, modifier: Modifier) {
    SurfaceCard(modifier) {
        Text(emoji, fontSize = 25.sp)
        Spacer(Modifier.height(5.dp))
        Text(amount, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Ink)
        Text(label, fontSize = 12.sp, color = Muted)
    }
}

@Composable
private fun AchievementDetails(
    achievement: AchievementDefinition,
    progress: LearnerProgress,
    onDismiss: () -> Unit,
    onClaim: () -> Boolean
) {
    val earned = progress.achievementUnlockedAt[achievement.id]
    val claimed = achievement.id in progress.claimedAchievements
    val current = achievement.currentValue(progress)
    Dialog(onDismissRequest = onDismiss) {
        Column(
            Modifier.fillMaxWidth().heightIn(max = 650.dp)
                .clip(RoundedCornerShape(24.dp)).background(Color.White)
                .verticalScroll(rememberScrollState()).padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                RaisedIconButton(GameGlyph.CLOSE, "Close achievement", onDismiss, size = 39.dp)
            }
            Text(if (earned != null) achievement.emoji else "🔒", fontSize = 75.sp)
            Spacer(Modifier.height(8.dp))
            Heading(achievement.title, 24.sp)
            Spacer(Modifier.height(8.dp))
            Text(achievement.description, color = Ink, fontSize = 14.sp, textAlign = TextAlign.Center)
            Spacer(Modifier.height(14.dp))
            Text(
                when (earned) {
                    null -> "Not unlocked yet"
                    "earlier" -> "Earned before achievement tracking was added"
                    else -> "Unlocked ${formatAchievementDay(earned)}"
                },
                fontSize = 12.sp, color = Muted, textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(15.dp))
            ProgressLine(current.toFloat() / achievement.target, Leaf, Modifier.fillMaxWidth(), 11.dp)
            Spacer(Modifier.height(5.dp))
            Text("${current.coerceAtMost(achievement.target)} / ${achievement.target}", fontSize = 13.sp,
                color = if (earned != null) LeafDark else Muted, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(18.dp))
            Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(15.dp))
                .background(Pale).padding(15.dp)) {
                Text("ACHIEVEMENT REWARD", color = Muted, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(6.dp))
                Text(achievement.rewardLabel, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Ink)
                if (achievement.id == "perfectionist") Text("Starlight Bow · Headwear", fontSize = 12.sp, color = Muted)
                if (achievement.id == "tagalog_champion") Text("Starry Room · Background", fontSize = 12.sp, color = Muted)
                if (achievement.rewardType == com.dalawingo.app.model.AchievementReward.XP_BOOST) {
                    Spacer(Modifier.height(5.dp))
                    Text("Added to your Boost Inventory. Activate whenever you're ready.", fontSize = 12.sp, color = Muted)
                }
            }
            Spacer(Modifier.height(18.dp))
            DalaButton(
                when {
                    claimed -> "✓ REWARD CLAIMED"
                    earned == null -> "🔒 LOCKED"
                    else -> "CLAIM REWARD  →"
                },
                onClick = { onClaim() },
                modifier = Modifier.fillMaxWidth(),
                enabled = earned != null && !claimed
            )
        }
    }
}

private fun formatAchievementDay(date: String): String = runCatching {
    LocalDate.parse(date).format(DateTimeFormatter.ofPattern("MMM d, yyyy"))
}.getOrDefault(date)

@Composable
private fun VocabularyDetails(progress: LearnerProgress, onClose: () -> Unit) {
    val phrases = allPhrases.distinctBy(::vocabularyKey)
        .filter { vocabularyKey(it) in progress.vocabulary }
    Dialog(onDismissRequest = onClose) {
        Column(
            Modifier.fillMaxWidth().heightIn(max = 600.dp)
                .clip(RoundedCornerShape(23.dp)).background(Color.White).padding(18.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Heading("Your vocabulary", 22.sp, Modifier.weight(1f))
                RaisedIconButton(GameGlyph.CLOSE, "Close vocabulary", onClose, size = 39.dp)
            }
            Spacer(Modifier.height(10.dp))
            if (phrases.isEmpty()) {
                Text("Complete a lesson to start collecting phrases!", color = Muted, fontSize = 14.sp)
            } else {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    phrases.forEach { phrase ->
                        val item = progress.vocabulary[vocabularyKey(phrase)] ?: return@forEach
                        Column(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                            Text(phrase.tagalog, fontWeight = FontWeight.ExtraBold, color = Ink, fontSize = 15.sp)
                            Text(phrase.english, color = Muted, fontSize = 13.sp)
                            Text(
                                "${item.level.name.lowercase().replaceFirstChar { it.uppercase() }} · ${item.correct}/${item.attempts} correct" +
                                    if (item.isDue(LocalDate.now().toEpochDay())) " · Due for review" else "",
                                color = LeafDark, fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
