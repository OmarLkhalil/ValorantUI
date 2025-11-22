package com.larryyu.ui.components

import androidx.compose.runtime.Composable

@Composable
actual fun BackHandler(
    enabled: Boolean,
    onBack: () -> Unit
) {
    // iOS doesn't have a system back button like Android
    // Back navigation is handled by the NavigationController's swipe gesture
    // This is a no-op on iOS
}

