package com.larryyu.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import com.larryyu.domain.model.AgentDetailsData


fun calculateDominantColor(
    source: String,
    onCalculated: (Color) -> Unit
) {
    val bytes = source.encodeToByteArray()
    val sums = IntArray(3)
    for (i in bytes.indices) {
        sums[i % 3] = (sums[i % 3] + (bytes[i].toInt() and 0xFF)) % 256
    }
    val r = sums[0] / 255f
    val g = sums[1] / 255f
    val b = sums[2] / 255f
    onCalculated(Color(r, g, b))
}

