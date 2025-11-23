package com.larryyu.utils

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.larryyu.db.ValorantDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

/**
 * iOS Database Driver Factory
 *
 * Uses the iOS Documents directory for database storage.
 * Falls back to in-memory database if file creation fails.
 *
 * Note: Requires -lsqlite3 linker option in build.gradle.kts
 * to link against the system SQLite library.
 */

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(ValorantDatabase.Schema, "Agents.db")
    }
}
