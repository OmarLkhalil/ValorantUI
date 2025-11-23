package com.larryyu.utils

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.larryyu.db.ValorantDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return try {
            println("🗄️ Creating SQLite database driver...")
            val driver = NativeSqliteDriver(ValorantDatabase.Schema, "Agents.db")
            println("✅ Database driver created successfully")
            driver
        } catch (e: Exception) {
            println("❌ Database creation failed: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }
}

