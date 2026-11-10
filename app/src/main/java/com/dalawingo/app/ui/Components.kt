package com.dalawingo.app.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DalaButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Leaf,
    pressedColor: Color = LeafDark,
    enabled: Boolean = true,
    textColor: Color = Color.White
) {
    Box(
        modifier = modifier
            .height(57.dp)
            .clip(RoundedCornerShape(17.dp))
            .background(if (enabled) pressedColor else Border)
            .clickable(enabled = enabled, onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(53.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(if (enabled) color else Color(0xFFE9E9E9)),
            contentAlignment = Alignment.Center
        ) {
            Text(text.uppercase(), fontWeight = FontWeight.ExtraBold, color = if (enabled) textColor else Muted,
                letterSpacing = 0.6.sp, fontSize = 15.sp, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun DalaOutlineButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, color: Color = Ink) {
    Box(modifier = modifier
        .height(55.dp)
        .clip(RoundedCornerShape(16.dp))
        .border(2.dp, Border, RoundedCornerShape(16.dp))
        .clickable(onClick = onClick), contentAlignment = Alignment.Center) {
        Text(text.uppercase(), fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = color, letterSpacing = .5.sp)
    }
}

@Composable
fun SurfaceCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(2.dp, Border, RoundedCornerShape(20.dp))
            .padding(18.dp),
        content = content
    )
}

@Composable
fun ProgressLine(progress: Float, color: Color = Leaf, modifier: Modifier = Modifier, height: androidx.compose.ui.unit.Dp = 13.dp) {
    Box(modifier = modifier.height(height).clip(CircleShape).background(Color(0xFFE6E6E6))) {
        Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(progress.coerceIn(0f, 1f))
            .clip(CircleShape).background(color))
    }
}

@Composable
fun Eyebrow(text: String, color: Color = Muted) {
    Text(text.uppercase(), fontSize = 12.sp, fontWeight = FontWeight.ExtraBold,
        color = color, letterSpacing = 1.2.sp)
}

@Composable
fun Heading(text: String, size: TextUnit = 26.sp, modifier: Modifier = Modifier) {
    Text(text, modifier = modifier, color = Ink, fontSize = size, lineHeight = (size.value + 5).sp,
        fontWeight = FontWeight.ExtraBold)
}

@Composable
fun StatChip(symbol: String, count: String, color: Color, onClick: (() -> Unit)? = null) {
    Row(modifier = Modifier
        .clip(RoundedCornerShape(14.dp))
        .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
        .padding(horizontal = 7.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(symbol, fontSize = 20.sp)
        Text(count, color = color, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
    }
}

/** Dalawingo's own mascot: a friendly Philippine tarsier, not Duolingo's owl. */
@Composable
fun TarsierMascot(modifier: Modifier = Modifier, happy: Boolean = true) {
    Canvas(modifier) {
        val x = size.width / 2f
        val y = size.height / 2f
        val r = size.minDimension / 2f
        val brown = Color(0xFFBD7949)
        val gold = Color(0xFFF4BF78)
        val shadow = Color(0xFF9B5B34)
        val eye = Color(0xFFFFFDF5)
        val pupil = Color(0xFF38281F)
        val pink = Color(0xFFED9293)
        val leftEar = Path().apply {
            moveTo(x - .49f*r, y - .45f*r)
            lineTo(x - .95f*r, y - 1.05f*r)
            lineTo(x - .95f*r, y - .12f*r)
            close()
        }
        val rightEar = Path().apply {
            moveTo(x + .49f*r, y - .45f*r)
            lineTo(x + .95f*r, y - 1.05f*r)
            lineTo(x + .95f*r, y - .12f*r)
            close()
        }
        drawPath(leftEar, brown)
        drawPath(rightEar, brown)
        drawCircle(brown, r * .88f, Offset(x, y))
        drawOval(gold, topLeft = Offset(x-r*.69f, y-r*.63f), size = Size(r*1.38f, r*1.33f))
        listOf(-.37f, .37f).forEach { shift ->
            drawCircle(shadow, r * .41f, Offset(x + shift*r, y - r*.11f))
            drawCircle(eye, r * .345f, Offset(x + shift*r, y - r*.15f))
            drawCircle(pupil, r * .195f, Offset(x + shift*r + r*.03f, y - r*.13f))
            drawCircle(Color.White, r * .071f, Offset(x + shift*r - r*.05f, y - r*.22f))
        }
        drawCircle(pink, r*.12f, Offset(x, y + r*.35f))
        if (happy) {
            drawArc(shadow, 10f, 160f, false,
                topLeft = Offset(x-r*.20f, y+r*.31f),
                size = Size(r*.40f, r*.25f), style = Stroke(r*.044f, cap = StrokeCap.Round))
        } else {
            drawCircle(shadow, r*.07f, Offset(x, y + r*.55f))
        }
    }
}

@Composable
fun CircleEmoji(emoji: String, background: Color, modifier: Modifier = Modifier, fontSize: TextUnit = 29.sp) {
    Box(modifier.clip(CircleShape).background(background), contentAlignment = Alignment.Center) {
        Text(emoji, fontSize = fontSize)
    }
}
