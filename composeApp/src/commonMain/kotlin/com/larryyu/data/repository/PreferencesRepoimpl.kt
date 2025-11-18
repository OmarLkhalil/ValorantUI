package com.larryyu.data.repository

import com.larryyu.data.datasource.local.ThemeDataStore
import com.larryyu.domain.repository.PreferencesRepo


class PreferencesRepoimpl(
    private val dataStore: ThemeDataStore
) : PreferencesRepo {

    override suspend fun setDarkTheme(enabled: Boolean) {
        dataStore.setDarkTheme(enabled)
    }

    override suspend fun isDarkTheme(): Boolean {
        return dataStore.isDarkTheme()
    }
}