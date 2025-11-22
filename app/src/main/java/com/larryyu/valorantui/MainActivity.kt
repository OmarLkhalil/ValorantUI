package com.larryyu.valorantui
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.larryyu.App
import com.larryyu.di.initKoin
import com.larryyu.ui.theme.ValorantUITheme
import com.larryyu.utils.ContextUtils
import com.larryyu.utils.initDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ContextUtils.setContext(context = this)
        initDataStore(applicationContext)
        initKoin {
            androidLogger(level = Level.NONE)
            androidContext(androidContext = this@MainActivity)
        }
        setContent {
            ValorantUITheme {
                App()
            }
        }
    }
}
