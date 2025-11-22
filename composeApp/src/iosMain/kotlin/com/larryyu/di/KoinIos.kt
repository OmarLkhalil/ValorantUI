package com.larryyu.di

import org.koin.core.context.startKoin

/**
 * iOS-specific Koin initialization
 * This function is called from Swift code (iOSApp.swift)
 */
fun initKoinIos() {
    startKoin {
        modules(
            platformModule(),
            commonModule(enableNetworkLogs = true)
        )
    }
}

