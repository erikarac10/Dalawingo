package com.dalawingo.app.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import com.dalawingo.app.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Small, consistent vector icons; emojis remain available for content artwork. */
enum class GameGlyph { EDIT, UP, HOME, LEARN, TROPHY, CLOSE, CHEVRON, CLOCK }

@Composable
fun GameIcon(glyph: GameGlyph, modifier: Modifier = Modifier, color: Color = Ink) {
    Canvas(modifier) {
        val w = size.width
        val h = size.height
        val s = minOf(w, h) / 24f
        fun pt(x: Float, y: Float) = Offset(w / 2f + (x - 12f) * s, h / 2f + (y - 12f) * s)
        fun line(a: Float, b: Float, c: Float, d: Float, thick: Float = 2.6f) =
            drawLine(color, pt(a, b), pt(c, d), strokeWidth = thick * s, cap = StrokeCap.Round)
        fun shape(points: List<Pair<Float, Float>>, close: Boolean = false, thick: Float = 2.5f) {
            val p = Path()
            points.forEachIndexed { i, pair ->
                val q = pt(pair.first, pair.second)
                if (i == 0) p.moveTo(q.x, q.y) else p.lineTo(q.x, q.y)
            }
            if (close) p.close()
            drawPath(p, color, style = Stroke(width = thick * s, cap = StrokeCap.Round, join = StrokeJoin.Round))
        }
        when (glyph) {
            GameGlyph.UP -> {
                line(12f, 20f, 12f, 4f, 3f)
                line(5f, 11f, 12f, 4f, 3f)
                line(19f, 11f, 12f, 4f, 3f)
            }
            GameGlyph.EDIT -> {
                shape(listOf(5f to 19f, 7f to 13f, 17f to 3f, 21f to 7f, 11f to 17f, 5f to 19f), true, 2f)
                line(14.5f, 5.5f, 18.5f, 9.5f, 2f)
            }
            GameGlyph.HOME -> {
                shape(listOf(3f to 11f, 12f to 3.5f, 21f to 11f), false, 2.6f)
                shape(listOf(5.5f to 10f, 5.5f to 20f, 18.5f to 20f, 18.5f to 10f), false, 2.6f)
                shape(listOf(10f to 20f, 10f to 14f, 14f to 14f, 14f to 20f), false, 2.3f)
            }
            GameGlyph.LEARN -> {
                shape(listOf(12f to 6f, 9f to 4.5f, 3.5f to 4.5f, 3.5f to 18f, 9f to 18f, 12f to 20f), false, 2.5f)
                shape(listOf(12f to 6f, 15f to 4.5f, 20.5f to 4.5f, 20.5f to 18f, 15f to 18f, 12f to 20f), false, 2.5f)
                line(12f, 6f, 12f, 19.8f, 2f)
            }
            GameGlyph.TROPHY -> {
                shape(listOf(7f to 4f, 17f to 4f, 17f to 11f, 15f to 15f, 12f to 16f, 9f to 15f, 7f to 11f), false, 2.5f)
                shape(listOf(7f to 6f, 3.5f to 6f, 3.5f to 9f, 7f to 12f), false, 2.4f)
                shape(listOf(17f to 6f, 20.5f to 6f, 20.5f to 9f, 17f to 12f), false, 2.4f)
                line(12f, 16f, 12f, 20f); line(8f, 20f, 16f, 20f)
            }
            GameGlyph.CLOSE -> {
                line(5f, 5f, 19f, 19f, 2.8f)
                line(19f, 5f, 5f, 19f, 2.8f)
            }
            GameGlyph.CHEVRON -> {
                line(9f, 5f, 16f, 12f, 2.8f)
                line(16f, 12f, 9f, 19f, 2.8f)
            }
            GameGlyph.CLOCK -> {
                drawCircle(color, radius = 9f * s, center = pt(12f, 12f), style = Stroke(2.4f * s))
                line(12f, 7f, 12f, 12f, 2.5f)
                line(12f, 12f, 16f, 14.5f, 2.5f)
            }
        }
    }
}

/** Raised, circular controls share a visual language across Player, Learn, Shop, and dialogs. */
@Composable
fun RaisedIconButton(
    glyph: GameGlyph,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    face: Color = Color.White,
    shade: Color = Border,
    iconColor: Color = Ink
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val controlShape = if (glyph == GameGlyph.EDIT) RoundedCornerShape(14.dp)
        else CircleShape
    Box(
        modifier.size(size).clip(controlShape).background(shade)
            .semantics { contentDescription = description; role = Role.Button }
            .clickable(interactionSource = interaction, indication = null, onClick = onClick)
    ) {
        Box(
            Modifier.offset(y = if (pressed) 4.dp else 0.dp)
                .fillMaxWidth().height(size - 4.dp)
                .clip(controlShape).background(face).align(Alignment.TopCenter),
            contentAlignment = Alignment.Center
        ) {
            val drawable = when (glyph) {
                GameGlyph.UP -> R.drawable.ic_arrow_up
                GameGlyph.EDIT -> R.drawable.ic_edit
                GameGlyph.HOME -> R.drawable.ic_home
                GameGlyph.LEARN -> R.drawable.ic_book
                GameGlyph.TROPHY -> R.drawable.ic_trophy
                else -> null
            }
            if (drawable != null) DalaIcon(drawable, description, size * .48f, iconColor)
            else GameIcon(glyph, Modifier.size(size * .48f), iconColor)
        }
    }
}
