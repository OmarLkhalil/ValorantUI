package com.larryyu.ui.components

import androidx.compose.runtime.Composable

/**
 * Multiplatform BackHandler - handles back press events across platforms
 *
 * @param enabled Whether the back handler is enabled
 * @param onBack Callback to execute when back is pressed
 */
@Composable
expect fun BackHandler(
    enabled: Boolean = true,
    onBack: () -> Unit
)

