package com.larryyu.valorantui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.larryyu.App
import com.larryyu.ui.theme.ValorantUITheme
import com.larryyu.utils.ContextUtils
import com.larryyu.valorantui.utils.CrashlyticsHelper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CrashlyticsHelper.log("MainActivity onCreate - App launched")
        CrashlyticsHelper.setCustomKey("app_version", "1.0")

        ContextUtils.setContext(context = this)
        setContent {
            ValorantUITheme {
                App()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        CrashlyticsHelper.log("MainActivity onResume")
    }

    override fun onPause() {
        super.onPause()
        CrashlyticsHelper.log("MainActivity onPause")
    }
}
