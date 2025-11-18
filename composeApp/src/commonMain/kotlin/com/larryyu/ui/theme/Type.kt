package com.larryyu.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import valorantui.composeapp.generated.resources.Res
import valorantui.composeapp.generated.resources.blazed
import valorantui.composeapp.generated.resources.dryme

@Composable
fun welcomeTextStyle(colors: ValorantUIColors): TextStyle {
    return TextStyle(
        fontSize = 24.sp,
        fontFamily = FontFamily(Font(Res.font.blazed)),
        lineHeight = 1.5.em,
        color = colors.headingText,
        fontWeight = FontWeight.W500,
    )
}

@Composable
fun body16(colors: ValorantUIColors): TextStyle {
    return TextStyle(
        fontSize = 16.sp,
        fontFamily = FontFamily(Font(Res.font.dryme)),
        color = colors.textPrimary,
        fontWeight = FontWeight.W400,
    )
}

@Composable
fun headline20(colors: ValorantUIColors): TextStyle {
    return TextStyle(
        fontSize = 20.sp,
        fontFamily = FontFamily(Font(Res.font.blazed)),
        lineHeight = 1.3.em,
        letterSpacing = 0.2.em,
        color = colors.textPrimary,
        fontWeight = FontWeight.W400,
    )
}

@Composable
fun headline18(colors: ValorantUIColors): TextStyle {
    return TextStyle(
        fontSize = 18.sp,
        fontFamily = FontFamily(Font(Res.font.dryme)),
        lineHeight = 1.3.em,
        letterSpacing = 0.2.em,
        color = colors.textPrimary,
        fontWeight = FontWeight.W400,
    )
}

@Composable
fun body12(colors: ValorantUIColors): TextStyle {
    return TextStyle(
        fontSize = 12.sp,
        fontFamily = FontFamily(Font(Res.font.dryme)),
        color = colors.textSecondary,
        lineHeight = 1.3.em,
        letterSpacing = 0.2.em,
        fontWeight = FontWeight.W400,
    )
}

@Composable
fun body8(colors: ValorantUIColors): TextStyle {
    return TextStyle(
        fontSize = 7.sp,
        fontFamily = FontFamily(Font(Res.font.dryme)),
        color = colors.textSecondary,
        lineHeight = 1.3.em,
        letterSpacing = 0.2.em,
        fontWeight = FontWeight.W400,
    )
}

@Immutable
data class ValorantUITypography(
    val welcomeTextStyle: TextStyle,
    val body16: TextStyle,
    val headline18: TextStyle,
    val headline20: TextStyle,
    val body12: TextStyle,
    val body8: TextStyle,
)

@Composable
fun provideValorantUITypography(colors: ValorantUIColors): ValorantUITypography {
    return ValorantUITypography(
        welcomeTextStyle = welcomeTextStyle(colors),
        body16 = body16(colors),
        headline20 = headline20(colors),
        headline18 = headline18(colors),
        body12 = body12(colors),
        body8 = body8(colors),
    )
}