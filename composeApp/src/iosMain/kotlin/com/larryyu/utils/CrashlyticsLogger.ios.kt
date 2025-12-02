package com.larryyu.utils


actual object CrashlyticsLogger {

    actual fun log(message: String) {
        println("iOS Crashlytics: $message")
    }

    actual fun setCustomKey(key: String, value: String) {
        println("iOS Crashlytics CustomKey: $key = $value")
    }

    actual fun setCustomKey(key: String, value: Boolean) {
        println("iOS Crashlytics CustomKey: $key = $value")
    }

    actual fun setCustomKey(key: String, value: Int) {
        println("iOS Crashlytics CustomKey: $key = $value")
    }

    actual fun setUserId(userId: String) {
        println("iOS Crashlytics UserId: $userId")
    }

    actual fun recordException(throwable: Throwable) {
        println("iOS Crashlytics Exception: ${throwable.message}")
        throwable.printStackTrace()
    }
}

