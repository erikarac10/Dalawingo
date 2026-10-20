package com.dalawingo.app.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Leaf = Color(0xFF58CC02)
val LeafDark = Color(0xFF46A302)
val Sky = Color(0xFF1CB0F6)
val SkyDark = Color(0xFF168CC5)
val Purple = Color(0xFFCE82FF)
val Tangerine = Color(0xFFFF9600)
val Red = Color(0xFFFF4B4B)
val Ink = Color(0xFF3C3C3C)
val Muted = Color(0xFF777777)
val Pale = Color(0xFFF7F9FA)
val Border = Color(0xFFE5E5E5)
val Cream = Color(0xFFFFF9EE)

@Composable
fun DalawingoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Leaf, secondary = Sky, surface = Color.White,
            background = Color.White, onSurface = Ink, onBackground = Ink
        ),
        content = content
    )
}
