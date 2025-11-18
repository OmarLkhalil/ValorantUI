package com.larryyu.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

// 🎮 Base Colors
val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)
val Red = Color(0xFFFF4C4C)
val Green = Color(0xFF00E676)
val Grey = Color(0xFF9A9A9A)
val Divider = Color(0xFFBDBDBD)

// 🎮 Core Brand Colors
val ValorantUIPurple = Color(0xFFC148FF)
val DeepBackground = Color(0xFF0E0F30)
val AccentBlue = Color(0xFF6B5BFF)
val SurfaceDark = Color(0xFF1A1B3A)
val SurfaceLight = Color(0xFFF4F2FF)

@Immutable
data class ValorantUIColors(
    // Buttons
    val buttonBackground: Color,
    val buttonText: Color,

    // Backgrounds
    val background: Color,
    val surface: Color,

    // Texts
    val textPrimary: Color,
    val textSecondary: Color,
    val headingText: Color,

    // Status / Actions
    val success: Color = Green,
    val error: Color = Red,

    // Borders / Dividers
    val divider: Color = Divider,
    val disabled: Color = Grey,

    // Extras
    val highlight: Color = ValorantUIPurple,
    val white: Color = White,
    val black: Color = Black,
)

// 🌕 Light Theme
val LightValorantUIColors = ValorantUIColors(
    buttonBackground = ValorantUIPurple,
    buttonText = White,
    background = SurfaceLight,
    surface = White,
    textPrimary = Color(0xFF1C1C1C),
    textSecondary = Color(0xFF666666),
    headingText = Black,
)

// 🌑 Dark Theme
val DarkValorantUIColors = ValorantUIColors(
    buttonBackground = ValorantUIPurple,
    buttonText = White,
    background = DeepBackground,
    surface = SurfaceDark,
    textPrimary = White,
    textSecondary = Grey,
    headingText = White,
)
