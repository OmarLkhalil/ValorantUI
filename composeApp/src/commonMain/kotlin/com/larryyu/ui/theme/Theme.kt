package com.larryyu.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.*
import org.koin.compose.koinInject


private val localColorScheme = staticCompositionLocalOf { LightValorantUIColors }
private val localDimens = staticCompositionLocalOf { ValorantUIDimens() }
private val localShapes = staticCompositionLocalOf { ValorantUIShapes() }
private val localTypography = staticCompositionLocalOf<ValorantUITypography> { error("No Typography provided") }

@Composable
fun ValorantUITheme(
    content: @Composable () -> Unit
) {
    val themeViewModel: ThemeViewModel = koinInject()

    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState(initial = isSystemInDarkTheme())

    val colorScheme = if (isDarkTheme) DarkValorantUIColors else LightValorantUIColors

    CompositionLocalProvider(
        localColorScheme provides colorScheme,
        localTypography provides provideValorantUITypography(colorScheme),
        localShapes provides ValorantUIShapes(),
        localDimens provides ValorantUIDimens(),
    ) {
        content()
    }
}

@Composable
fun ValorantUITheme(
    isDark: Boolean,
    content: @Composable () -> Unit
) {
    val colorScheme = if (isDark) DarkValorantUIColors else LightValorantUIColors

    CompositionLocalProvider(
        localColorScheme provides colorScheme,
        localTypography provides provideValorantUITypography(colorScheme),
        localShapes provides ValorantUIShapes(),
        localDimens provides ValorantUIDimens(),
    ) {
        content()
    }
}

@Composable
@ReadOnlyComposable
fun isDarkTheme() = DarkThemeValue.current.value

private val DarkThemeValue = compositionLocalOf { mutableStateOf(false) }

@Composable
@ReadOnlyComposable
infix fun <T> T.orInLightTheme(other: T): T =
    if (isDarkTheme()) this else other

object Theme {
    val colors: ValorantUIColors
        @Composable
        @ReadOnlyComposable
        get() = localColorScheme.current

    val typography: ValorantUITypography
        @Composable
        @ReadOnlyComposable
        get() = localTypography.current

    val shapes: ValorantUIShapes
        @Composable
        @ReadOnlyComposable
        get() = localShapes.current

    val dimens: ValorantUIDimens
        @Composable
        @ReadOnlyComposable
        get() = localDimens.current
}
