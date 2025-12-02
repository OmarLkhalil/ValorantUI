package com.larryyu.utils


actual object CrashlyticsLogger {

    actual fun log(message: String) {
        println("Desktop Log: $message")
    }

    actual fun setCustomKey(key: String, value: String) {
        println("Desktop CustomKey: $key = $value")
    }

    actual fun setCustomKey(key: String, value: Boolean) {
        println("Desktop CustomKey: $key = $value")
    }

    actual fun setCustomKey(key: String, value: Int) {
        println("Desktop CustomKey: $key = $value")
    }

    actual fun setUserId(userId: String) {
        println("Desktop UserId: $userId")
    }

    actual fun recordException(throwable: Throwable) {
        println("Desktop Exception: ${throwable.message}")
        throwable.printStackTrace()
    }
}

