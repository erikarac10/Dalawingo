package com.dalawingo.app.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.dalawingo.app.R
import com.dalawingo.app.data.LearnerProgress
import com.dalawingo.app.data.currentValue
import com.dalawingo.app.model.AchievementDefinition
import com.dalawingo.app.model.AchievementReward
import com.dalawingo.app.model.VocabularyLevel
import com.dalawingo.app.model.achievements
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ProfileScreen(
    progress: LearnerProgress,
    onClaimAchievement: (String) -> Boolean,
    onActivateBoost: () -> Boolean
) {
    var selected by remember { mutableStateOf<AchievementDefinition?>(null) }
    var claiming by remember { mutableStateOf<AchievementDefinition?>(null) }
    LaunchedEffect(claiming?.id) {
        val item = claiming ?: return@LaunchedEffect
        delay(1450)
        selected = item
        claiming = null
    }
    val achievementsScroll = rememberScrollState()
    var nowMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(progress.xpBoostUntilMillis) {
        while (progress.xpBoostUntilMillis > System.currentTimeMillis()) {
            nowMillis = System.currentTimeMillis()
            delay(1000)
        }
        nowMillis = System.currentTimeMillis()
    }
    val boostSeconds = ((progress.xpBoostUntilMillis - nowMillis).coerceAtLeast(0L) + 999) / 1000
    val ordered = achievements.sortedWith(compareBy<AchievementDefinition> {
        when {
            it.id in progress.achievementUnlockedAt && it.id !in progress.claimedAchievements -> 0
            it.id in progress.claimedAchievements -> 1
            else -> 2
        }
    })
    Column(Modifier.fillMaxSize().background(Color.White)
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp, vertical = 17.dp)) {
        Heading("Statistics", 21.sp)
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
            ProgressStat(R.drawable.ic_fire, "${progress.streak}", "Day streak", Modifier.weight(1f), Tangerine)
            ProgressStat(R.drawable.ic_xp, "${progress.xp}", "Total XP", Modifier.weight(1f), Color(0xFFE7B423))
        }
        Spacer(Modifier.height(9.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
            ProgressStat(R.drawable.ic_check_colored, "${progress.completed.size}", "Lessons finished", Modifier.weight(1f), Leaf)
            ProgressStat(R.drawable.ic_star_colored, "${progress.golden.size}", "Perfect lessons", Modifier.weight(1f), Color(0xFFCFAC16))
        }
        Spacer(Modifier.height(23.dp))
        Heading("Achievements", 21.sp)
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth().horizontalScroll(achievementsScroll),
            horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ordered.forEach { item ->
                val unlocked = item.id in progress.achievementUnlockedAt
                val claimed = item.id in progress.claimedAchievements
                val ready = unlocked && !claimed
                val pulse = rememberInfiniteTransition(label = "Claimable ${item.id}")
                val scale by pulse.animateFloat(1f, 1.055f,
                    animationSpec = infiniteRepeatable(tween(820), RepeatMode.Reverse),
                    label = "Ready to claim")
                Column(Modifier.width(140.dp).height(166.dp)
                    .clip(RoundedCornerShape(17.dp))
                    .background(if (ready) Color(0xFFFFF7E2) else Color.White)
                    .border(2.dp, if (ready) Color(0xFFECC75A) else Border, RoundedCornerShape(17.dp))
                    .clickable {
                        if (ready && onClaimAchievement(item.id)) claiming = item
                        else selected = item
                    }.padding(9.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween) {
                    Box(contentAlignment = Alignment.Center) {
                        AchievementBadge(item.id, unlocked, 82.dp,
                            Modifier.graphicsLayer {
                                scaleX = if (ready) scale else 1f
                                scaleY = if (ready) scale else 1f
                            })
                        if (ready) {
                            val alpha = ((scale - 1f) * 17f).coerceIn(.2f, .95f)
                            DalaIcon(R.drawable.ic_sparkle, null, 22.dp,
                                Color(0xFFFFCB36), Modifier.align(Alignment.TopEnd)
                                    .graphicsLayer(alpha = alpha).offset(x = 5.dp, y = (-6).dp))
                        }
                    }
                    Text(item.title, color = Ink, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center, maxLines = 2)
                    if (!claimed) {
                        Text("${item.currentValue(progress).coerceAtMost(item.target)}/${item.target}",
                            fontSize = 10.sp, fontWeight = FontWeight.ExtraBold,
                            color = if (ready) Color(0xFFB08515) else Muted)
                    } else Spacer(Modifier.height(12.dp))
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        BoxWithConstraints(Modifier.fillMaxWidth()) {
            val visible = maxWidth * (constraints.maxWidth.toFloat() /
                (constraints.maxWidth + achievementsScroll.maxValue).coerceAtLeast(1)).coerceIn(.13f, 1f)
            val travel = maxWidth - visible
            val at = achievementsScroll.value.toFloat() / achievementsScroll.maxValue.coerceAtLeast(1)
            Box(Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(8.dp)).background(Border)) {
                Box(Modifier.offset(x = travel * at).width(visible).fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp)).background(Leaf))
            }
        }
        Spacer(Modifier.height(23.dp))
        Heading("Vocabulary Mastery", 21.sp)
        Spacer(Modifier.height(11.dp))
        SurfaceCard(Modifier.fillMaxWidth()) {
            val total = progress.vocabulary.size
            val mastered = progress.vocabulary.values.count { it.level == VocabularyLevel.MASTERED }
            val developing = progress.vocabulary.values.count { it.level == VocabularyLevel.DEVELOPING }
            val weak = progress.vocabulary.values.count { it.level == VocabularyLevel.WEAK }
            val fresh = total - mastered - developing - weak
            Text("$total phrases", color = Ink, fontSize = 23.sp, fontWeight = FontWeight.ExtraBold)
            Text("Vocabulary encountered", color = Muted, fontSize = 12.sp)
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth().height(12.dp).clip(RoundedCornerShape(7.dp))
                .background(Color(0xFFE7E7E7))) {
                if (total > 0) {
                    if (mastered > 0) Box(Modifier.weight(mastered.toFloat()).fillMaxHeight().background(Leaf))
                    if (developing > 0) Box(Modifier.weight(developing.toFloat()).fillMaxHeight().background(Color(0xFFE9C23E)))
                    if (weak > 0) Box(Modifier.weight(weak.toFloat()).fillMaxHeight().background(Tangerine))
                    if (fresh > 0) Box(Modifier.weight(fresh.toFloat()).fillMaxHeight().background(Sky))
                }
            }
            Spacer(Modifier.height(9.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(13.dp)) {
                Text("$mastered mastered", color = LeafDark, fontSize = 12.sp,
                    fontWeight = FontWeight.Bold)
                Text("$developing developing", color = Color(0xFFB28A00), fontSize = 12.sp,
                    fontWeight = FontWeight.Bold)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(13.dp)) {
                Text("$weak weak", color = Tangerine, fontSize = 12.sp,
                    fontWeight = FontWeight.Bold)
                Text("$fresh new", color = SkyDark, fontSize = 12.sp,
                    fontWeight = FontWeight.Bold)
            }
        }
        if (progress.xpBoostTokens > 0 || boostSeconds > 0) {
            Spacer(Modifier.height(23.dp))
            Heading("XP Boosts", 21.sp)
            Spacer(Modifier.height(11.dp))
            SurfaceCard(Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    DalaIcon(R.drawable.ic_xp, null, 26.dp, Color(0xFFE7B423))
                    Spacer(Modifier.width(7.dp))
                    Text("2× XP · 15 minutes", color = Ink, fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold)
                }
                Spacer(Modifier.height(5.dp))
                Text(if (boostSeconds > 0) "Active · ${boostSeconds / 60}:${(boostSeconds % 60).toString().padStart(2, '0')} remaining"
                     else "${progress.xpBoostTokens} unused boosts", color = Muted, fontSize = 13.sp)
                if (boostSeconds == 0L) {
                    Spacer(Modifier.height(12.dp))
                    DalaButton("ACTIVATE 2× XP", { onActivateBoost() }, Modifier.fillMaxWidth())
                }
            }
        }
        Spacer(Modifier.height(24.dp))
    }
    if (claiming != null) {
        Dialog(onDismissRequest = {}) {
            val celebrate = remember { Animatable(.4f) }
            LaunchedEffect(claiming?.id) {
                celebrate.animateTo(1.38f, tween(350))
                celebrate.animateTo(1f, tween(400))
                delay(250)
                celebrate.animateTo(1.13f, tween(260))
                celebrate.animateTo(1f, tween(290))
            }
            Box(Modifier.fillMaxWidth().height(260.dp), contentAlignment = Alignment.Center) {
                AchievementBadge(claiming!!.id, true, 165.dp,
                    Modifier.graphicsLayer(scaleX = celebrate.value, scaleY = celebrate.value))
                listOf(-1 to -1, 1 to -1, -1 to 1, 1 to 1).forEach { (x, y) ->
                    DalaIcon(R.drawable.ic_sparkle, null, 31.dp, Color(0xFFFFCB36),
                        Modifier.offset(x = (x * 105).dp, y = (y * 95).dp)
                            .graphicsLayer(rotationZ = celebrate.value * 54f))
                }
            }
        }
    }
    selected?.let { item ->
        AchievementDetails(item, progress, { selected = null })
    }
}

@Composable
fun AchievementBadge(id: String, unlocked: Boolean, size: Dp, modifier: Modifier = Modifier) {
    val art = badgeFor(id)
    if (art != null) {
        val gray = remember { ColorMatrix().apply { setToSaturation(0f) } }
        Image(painterResource(art), contentDescription = null, modifier = modifier.size(size),
            colorFilter = if (unlocked) null else ColorFilter.colorMatrix(gray),
            alpha = if (unlocked) 1f else .55f)
    } else {
        Box(modifier.size(size).clip(RoundedCornerShape(23.dp))
            .background(if (unlocked) Color(0xFFDDF7CC) else Color(0xFFE5E5E5)),
            contentAlignment = Alignment.Center) {
            DalaIcon(iconForAchievement(id), null, size * .52f,
                if (unlocked) LeafDark else Color(0xFFAAAAAA))
        }
    }
}

@Composable
private fun ProgressStat(icon: Int, number: String, title: String, modifier: Modifier, color: Color) {
    SurfaceCard(modifier) {
        // Preserve the cheerful multi-colour illustration for flame, energy and star.
        val colouredAsset = icon in setOf(R.drawable.ic_fire, R.drawable.ic_xp,
            R.drawable.ic_check_colored, R.drawable.ic_star_colored)
        DalaIcon(icon, title, 29.dp, if (colouredAsset) null else color)
        Spacer(Modifier.height(5.dp))
        Text(number, color = Ink, fontWeight = FontWeight.ExtraBold, fontSize = 23.sp)
        Text(title, color = Muted, fontSize = 12.sp)
    }
}

@Composable
private fun AchievementDetails(
    achievement: AchievementDefinition,
    progress: LearnerProgress,
    onDismiss: () -> Unit
) {
    val earned = progress.achievementUnlockedAt[achievement.id]
    val claimed = achievement.id in progress.claimedAchievements
    val current = achievement.currentValue(progress)
    Dialog(onDismissRequest = onDismiss) {
        Column(Modifier.fillMaxWidth().heightIn(max = 670.dp)
            .clip(RoundedCornerShape(24.dp)).background(Color.White)
            .verticalScroll(rememberScrollState()).padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                Box(Modifier.size(38.dp).clickable(onClick = onDismiss),
                    contentAlignment = Alignment.Center) {
                    GameIcon(GameGlyph.CLOSE, Modifier.size(26.dp), Muted)
                }
            }
            AchievementBadge(achievement.id, earned != null, 128.dp)
            Spacer(Modifier.height(10.dp))
            Heading(achievement.title, 24.sp)
            Spacer(Modifier.height(8.dp))
            Text(achievement.description, color = Ink, fontSize = 14.sp, textAlign = TextAlign.Center)
            Spacer(Modifier.height(13.dp))
            if (earned != null && earned != "earlier") {
                Text("Unlocked ${formatAchievementDay(earned)}", color = Muted,
                    fontSize = 12.sp, textAlign = TextAlign.Center)
            }
            Spacer(Modifier.height(16.dp))
            ProgressLine(current.toFloat() / achievement.target.coerceAtLeast(1), Leaf,
                Modifier.fillMaxWidth(), 11.dp)
            Spacer(Modifier.height(5.dp))
            Text("${current.coerceAtMost(achievement.target)} / ${achievement.target}",
                fontSize = 13.sp, color = if (earned != null) LeafDark else Muted,
                fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(15.dp))
            // The fully-coloured badge is proof the reward has already been claimed.
            // Show what the player could earn only while it remains unclaimed.
            if (!claimed) {
                if (achievement.rewardType == AchievementReward.PESOS) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        DalaIcon(R.drawable.ic_peso_coin, null, 29.dp)
                        Spacer(Modifier.width(7.dp))
                        Text("+${achievement.rewardAmount} Pesos", color = Ink,
                            fontSize = 17.sp, fontWeight = FontWeight.ExtraBold)
                    }
                } else {
                    Text(achievement.rewardLabel, color = Ink, fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold)
                }
            }
        }
    }
}
private fun formatAchievementDay(date: String): String = runCatching {
    LocalDate.parse(date).format(DateTimeFormatter.ofPattern("MMM d, yyyy"))
}.getOrDefault(date)
