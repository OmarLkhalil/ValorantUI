package com.larryyu.ui.components

import androidx.compose.runtime.Composable

/**
 * Desktop implementation of BackHandler
 * Desktop typically doesn't have a back button, but can handle ESC key
 */
@Composable
actual fun BackHandler(
    enabled: Boolean,
    onBack: () -> Unit
) {
    // Desktop implementation
    // You can add keyboard event handling here if needed (ESC key)
    // For now, it's a no-op as desktop doesn't typically have back navigation

}

