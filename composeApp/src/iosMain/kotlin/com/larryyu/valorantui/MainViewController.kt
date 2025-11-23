package com.larryyu.valorantui

import androidx.compose.ui.window.ComposeUIViewController
import com.larryyu.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController {
    App()
}

