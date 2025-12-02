package com.larryyu.valorantui

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.larryyu.di.initKoin
import com.larryyu.utils.initDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class ValorantApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = true

        initDataStore(applicationContext)

        initKoin {
            androidLogger(level = Level.NONE)
            androidContext(this@ValorantApplication)
        }
    }
}

