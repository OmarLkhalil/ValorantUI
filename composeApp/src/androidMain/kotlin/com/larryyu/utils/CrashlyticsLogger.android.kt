package com.larryyu.utils

import com.google.firebase.crashlytics.FirebaseCrashlytics


actual object CrashlyticsLogger {

    private val crashlytics: FirebaseCrashlytics
        get() = FirebaseCrashlytics.getInstance()

    actual fun log(message: String) {
        try {
            crashlytics.log(message)
        } catch (e: Exception) {
            println("Crashlytics: $message")
        }
    }

    actual fun setCustomKey(key: String, value: String) {
        try {
            crashlytics.setCustomKey(key, value)
        } catch (e: Exception) {
            println("Crashlytics CustomKey: $key = $value")
        }
    }

    actual fun setCustomKey(key: String, value: Boolean) {
        try {
            crashlytics.setCustomKey(key, value)
        } catch (e: Exception) {
            println("Crashlytics CustomKey: $key = $value")
        }
    }

    actual fun setCustomKey(key: String, value: Int) {
        try {
            crashlytics.setCustomKey(key, value)
        } catch (e: Exception) {
            println("Crashlytics CustomKey: $key = $value")
        }
    }

    actual fun setUserId(userId: String) {
        try {
            crashlytics.setUserId(userId)
        } catch (e: Exception) {
            println("Crashlytics UserId: $userId")
        }
    }

    actual fun recordException(throwable: Throwable) {
        try {
            crashlytics.recordException(throwable)
        } catch (e: Exception) {
            println("Crashlytics Exception: ${throwable.message}")
            throwable.printStackTrace()
        }
    }
}

