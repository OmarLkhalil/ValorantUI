package com.larryyu.di
import app.cash.sqldelight.db.SqlDriver
import org.koin.core.module.Module
import org.koin.dsl.module
import com.larryyu.utils.DatabaseDriverFactory
actual fun platformModule(): Module = module {
    single { DatabaseDriverFactory(context = get()) }
    single<SqlDriver> { get<DatabaseDriverFactory>().createDriver() }
}
