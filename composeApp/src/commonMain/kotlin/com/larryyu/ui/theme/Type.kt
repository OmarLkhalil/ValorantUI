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
import valorantui.composeapp.generated.resources.rajdhani_bold
import valorantui.composeapp.generated.resources.rajdhani_semibold
import valorantui.composeapp.generated.resources.rajdhani_medium
import valorantui.composeapp.generated.resources.rajdhani_regular
import valorantui.composeapp.generated.resources.inter_variable
import valorantui.composeapp.generated.resources.teko_variable

@Composable
fun welcomeTextStyle(colors: ValorantUIColors): TextStyle {
    return TextStyle(
        fontSize = 28.sp,
        fontFamily = FontFamily(Font(Res.font.rajdhani_bold)),
        lineHeight = 1.4.em,
        letterSpacing = 0.05.em,
        color = colors.headingText,
        fontWeight = FontWeight.Bold,
    )
}
@Composable
fun body16(colors: ValorantUIColors): TextStyle {
    return TextStyle(
        fontSize = 16.sp,
        fontFamily = FontFamily(Font(Res.font.inter_variable)),
        color = colors.textPrimary,
        fontWeight = FontWeight.Normal,
        lineHeight = 1.5.em,
    )
}
@Composable
fun headline20(colors: ValorantUIColors): TextStyle {
    return TextStyle(
        fontSize = 20.sp,
        fontFamily = FontFamily(Font(Res.font.rajdhani_bold)),
        lineHeight = 1.3.em,
        letterSpacing = 0.05.em,
        color = colors.textPrimary,
        fontWeight = FontWeight.Bold,
    )
}
@Composable
fun headline18(colors: ValorantUIColors): TextStyle {
    return TextStyle(
        fontSize = 18.sp,
        fontFamily = FontFamily(Font(Res.font.rajdhani_semibold)),
        lineHeight = 1.3.em,
        letterSpacing = 0.05.em,
        color = colors.textPrimary,
        fontWeight = FontWeight.SemiBold,
    )
}
@Composable
fun body12(colors: ValorantUIColors): TextStyle {
    return TextStyle(
        fontSize = 12.sp,
        fontFamily = FontFamily(Font(Res.font.inter_variable)),
        color = colors.textSecondary,
        lineHeight = 1.4.em,
        fontWeight = FontWeight.Medium,
    )
}
@Composable
fun body8(colors: ValorantUIColors): TextStyle {
    return TextStyle(
        fontSize = 9.sp,
        fontFamily = FontFamily(Font(Res.font.teko_variable)),
        color = colors.textSecondary,
        lineHeight = 1.3.em,
        letterSpacing = 0.08.em,
        fontWeight = FontWeight.Medium,
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
