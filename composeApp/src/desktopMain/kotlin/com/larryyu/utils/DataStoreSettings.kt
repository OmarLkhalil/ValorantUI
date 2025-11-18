package com.larryyu.utils


import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.core.DataStore
import okio.Path
import okio.FileSystem
import okio.Path.Companion.toPath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope

private const val DATASTORE_FILE_NAME = "valorant_preferences.preferences_pb"

actual fun createDataStore(): DataStore<Preferences> {
    val dataStoreFile: Path = ("${System.getProperty("user.home")}/.$DATASTORE_FILE_NAME").toPath()

    return PreferenceDataStoreFactory.createWithPath(
        corruptionHandler = null,
        migrations = emptyList(),
        scope = CoroutineScope(Dispatchers.IO),
        produceFile = { dataStoreFile }
    )
}