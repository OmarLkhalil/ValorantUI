package com.larryyu.ui.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.clipRect


fun Modifier.clipToFraction(fraction: Float) = drawWithContent {
    val w = size.width * fraction.coerceIn(0f, 1f)

    clipRect(left = 0f, top = 0f, right = w, bottom = size.height) {
        this@drawWithContent.drawContent()
    }
}