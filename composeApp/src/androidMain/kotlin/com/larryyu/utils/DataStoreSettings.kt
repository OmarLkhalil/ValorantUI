package com.larryyu.utils
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.core.DataStore
import android.content.Context
private const val DATASTORE_NAME = "valorant_preferences"
private val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)
lateinit var appContext: Context
fun initDataStore(context: Context) {
    appContext = context.applicationContext
}
actual fun createDataStore(): DataStore<Preferences> {
    check(::appContext.isInitialized) { "You must call initDataStore(context) before using DataStore!" }
    return appContext.dataStore
}
