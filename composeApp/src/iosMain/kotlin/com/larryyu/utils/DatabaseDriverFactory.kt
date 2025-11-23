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
    @OptIn(ExperimentalForeignApi::class)
    actual fun createDriver(): SqlDriver {
        return try {
            println("🗄️ [iOS] Creating SQLite database driver...")

            // Get the Documents directory path (safe for iOS)
            val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
                directory = NSDocumentDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = false,
                error = null
            )

            if (documentDirectory != null) {
                val dbPath = "${documentDirectory.path}/Agents.db"
                println("🗄️ [iOS] Database path: $dbPath")

                val driver = NativeSqliteDriver(
                    schema = ValorantDatabase.Schema,
                    name = dbPath
                )

                println("✅ [iOS] Database driver created successfully at: $dbPath")
                driver
            } else {
                println("⚠️  [iOS] Could not get Documents directory, using in-memory database")
                val driver = NativeSqliteDriver(
                    schema = ValorantDatabase.Schema,
                    name = "file::memory:?cache=shared"
                )
                println("✅ [iOS] In-memory database driver created")
                driver
            }
        } catch (e: Exception) {
            println("❌ [iOS] Database creation failed: ${e.message}")
            println("❌ [iOS] Exception type: ${e::class.simpleName}")
            println("❌ [iOS] Falling back to in-memory database...")
            e.printStackTrace()

            // Fallback: use in-memory database
            try {
                val fallbackDriver = NativeSqliteDriver(
                    schema = ValorantDatabase.Schema,
                    name = "file::memory:?cache=shared"
                )
                println("✅ [iOS] Fallback in-memory database created")
                fallbackDriver
            } catch (fallbackError: Exception) {
                println("❌ [iOS] Even fallback failed: ${fallbackError.message}")
                fallbackError.printStackTrace()
                throw RuntimeException("Failed to create database: ${fallbackError.message}", fallbackError)
            }
        }
    }
}

