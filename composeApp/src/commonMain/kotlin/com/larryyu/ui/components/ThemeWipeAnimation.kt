package com.larryyu.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.larryyu.ui.theme.Theme
import kotlinx.coroutines.launch

@Composable
fun ThemeWipeOverlay(fraction: Float) {
    val color =Theme.colors.background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawWithContent {
                drawContent()
                val w = size.width * fraction.coerceIn(0f, 1f)
                drawRect(
                    color = color,
                    topLeft = Offset.Zero,
                    size = Size(w, size.height)
                )
            }
    )
}