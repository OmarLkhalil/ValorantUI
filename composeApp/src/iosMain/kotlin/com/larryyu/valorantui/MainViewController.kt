package com.larryyu.valorantui

import androidx.compose.ui.window.ComposeUIViewController
import com.larryyu.App
import com.larryyu.di.commonModule
import com.larryyu.di.platformModule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    // Initialize Koin on first call (if not already started)
    try {
        startKoin {
            modules(
                platformModule(),
                commonModule(enableNetworkLogs = true)
            )
        }
    } catch (e: Exception) {
        // Koin already started, ignore
        println("Koin already initialized: ${e.message}")
    }

    return ComposeUIViewController {
        App()
    }
}

