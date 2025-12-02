package com.larryyu.valorantui.utils

import com.google.firebase.crashlytics.FirebaseCrashlytics

object CrashlyticsHelper {

    private val crashlytics: FirebaseCrashlytics
        get() = FirebaseCrashlytics.getInstance()

    fun log(message: String) {
        crashlytics.log(message)
    }


    fun setCustomKey(key: String, value: String) {
        crashlytics.setCustomKey(key, value)
    }

    fun setCustomKey(key: String, value: Boolean) {
        crashlytics.setCustomKey(key, value)
    }

    fun setCustomKey(key: String, value: Int) {
        crashlytics.setCustomKey(key, value)
    }

    fun setUserId(userId: String) {
        crashlytics.setUserId(userId)
    }

    fun recordException(throwable: Throwable) {
        crashlytics.recordException(throwable)
    }

    fun sendTestCrash() {
        throw RuntimeException("Test Crash from Valorant UI")
    }
}

