package com.example.quazz.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Light theme
val theme_light_primary = Color(0xFF6492E3)
val theme_light_secondary = Color(0xFF0F439D)
val theme_light_tertiary = Color(0xFFE66157)
val theme_light_text = Color.Black
val theme_light_background = Color.White

// Dark theme
val theme_dark_primary = Color(0xFF395091)
val theme_dark_secondary = Color(0xFF0B2B5E)
val theme_dark_tertiary = Color(0xFFA3382F)
val theme_dark_text = Color.White
val theme_dark_background = Color.Black

data class QuazzPalette(
    val primary: Color = Color.Unspecified,
    val secondary: Color = Color.Unspecified,
    val error: Color = Color.Unspecified,
    val text: Color = Color.Unspecified,
    val background: Color = Color.Unspecified,
)

val LightQuazzPalette = QuazzPalette(
    primary = theme_light_primary,
    secondary = theme_light_secondary,
    error = theme_light_tertiary,
    text = theme_light_text,
    background = theme_light_background,
)

val DarkQuazzPalette = QuazzPalette(
    primary = theme_dark_primary,
    secondary = theme_dark_secondary,
    error = theme_dark_tertiary,
    text = theme_dark_text,
    background = theme_dark_background,
)

val LocalQuazzPalette = staticCompositionLocalOf { QuazzPalette() }