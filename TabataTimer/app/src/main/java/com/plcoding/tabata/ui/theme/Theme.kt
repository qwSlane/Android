package com.plcoding.tabata.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.Flow

private val DarkColorPalette = darkColors(
    primary = Color.White,
    background = DarkGray,
    onBackground = Color.White,
    surface = LightBlue,
    onSurface = DarkGray
)

private val LightColorPalette = lightColors(
    primary = Color.Black,
    background = Color.White,
    onBackground = Color.Black
)

@Composable
fun AppTheme(darkTheme: Int = 1, fontSize: Int = 1, content: @Composable() () -> Unit) {
    MaterialTheme(
        colors = if (darkTheme == 1) DarkColorPalette else LightColorPalette,
        typography = when (fontSize) {
            0 -> TypographySmall
            1 -> Typography
            else -> TypographyLarge
        },
        shapes = Shapes,
        content = content
    )
}