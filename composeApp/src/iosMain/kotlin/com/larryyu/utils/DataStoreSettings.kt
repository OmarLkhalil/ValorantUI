package com.larryyu.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path.Companion.toPath
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

private const val DATASTORE_NAME = "valorant_preferences.preferences_pb"

@OptIn(ExperimentalForeignApi::class)
private fun getDataStoreFile(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null
    )
    return requireNotNull(documentDirectory?.path) + "/$DATASTORE_NAME"
}

actual fun createDataStore(): DataStore<Preferences> {
    return createDataStoreWithPath(
        producePath = { getDataStoreFile() }
    )
}

private fun createDataStoreWithPath(
    producePath: () -> String
): DataStore<Preferences> {
    return androidx.datastore.preferences.core.PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath().toPath() }
    )
}

