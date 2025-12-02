package com.larryyu.utils


expect object CrashlyticsLogger {
    fun log(message: String)
    fun setCustomKey(key: String, value: String)

    fun setCustomKey(key: String, value: Boolean)

    fun setCustomKey(key: String, value: Int)
    fun setUserId(userId: String)
    fun recordException(throwable: Throwable)
}

