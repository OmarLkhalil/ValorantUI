package com.larryyu.domain.repository
interface PreferencesRepo {
    suspend fun setDarkTheme(enabled: Boolean)
    suspend fun isDarkTheme(): Boolean
}
