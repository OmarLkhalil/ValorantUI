package com.larryyu.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.io.IOException

class ThemeDataStore(private val dataStore: DataStore<Preferences>) {

    private val darkThemeKey = booleanPreferencesKey("dark_theme_enabled")

    suspend fun setDarkTheme(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[darkThemeKey] = enabled
        }
    }

    suspend fun isDarkTheme(): Boolean {
        return dataStore.data
            .catch { e ->
                if (e is IOException) emit(emptyPreferences())
                else throw e
            }
            .map { prefs -> prefs[darkThemeKey] ?: false }
            .first()
    }
}