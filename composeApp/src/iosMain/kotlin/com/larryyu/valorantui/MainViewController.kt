package com.larryyu.valorantui

import androidx.compose.ui.window.ComposeUIViewController
import com.larryyu.App
import com.larryyu.di.commonModule
import com.larryyu.di.platformModule
import org.koin.core.context.startKoin
import platform.UIKit.UIViewController

/**
 * Initialize Koin for iOS
 * Called from Swift code during app initialization
 */
fun initKoinIos() {
    startKoin {
        modules(
            platformModule(),
            commonModule(enableNetworkLogs = true)
        )
    }
}

fun MainViewController(): UIViewController {
    return ComposeUIViewController {
        App()
    }
}

