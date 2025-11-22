package com.larryyu.ui.theme
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)
val Red = Color(0xFFFF4C4C)
val Green = Color(0xFF00E676)
val Grey = Color(0xFF9A9A9A)
val Divider = Color(0xFFBDBDBD)
val ValorantUIPurple = Color(0xFFC148FF)
val DeepBackground = Color(0xFF0E0F30)
val AccentBlue = Color(0xFF6B5BFF)
val SurfaceDark = Color(0xFF1A1B3A)
val SurfaceLight = Color(0xFFF4F2FF)
@Immutable
data class ValorantUIColors(
    val buttonBackground: Color,
    val buttonText: Color,
    val background: Color,
    val surface: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val headingText: Color,
    val success: Color = Green,
    val error: Color = Red,
    val divider: Color = Divider,
    val disabled: Color = Grey,
    val highlight: Color = ValorantUIPurple,
    val white: Color = White,
    val black: Color = Black,
)
val LightValorantUIColors = ValorantUIColors(
    buttonBackground = ValorantUIPurple,
    buttonText = White,
    background = SurfaceLight,
    surface = White,
    textPrimary = Color(0xFF1C1C1C),
    textSecondary = Color(0xFF666666),
    headingText = Black,
)
val DarkValorantUIColors = ValorantUIColors(
    buttonBackground = ValorantUIPurple,
    buttonText = White,
    background = DeepBackground,
    surface = SurfaceDark,
    textPrimary = White,
    textSecondary = Grey,
    headingText = White,
)
