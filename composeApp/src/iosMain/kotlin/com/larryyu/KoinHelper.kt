package com.larryyu

import com.larryyu.di.commonModule
import com.larryyu.di.platformModule
import org.koin.core.context.startKoin

fun doInitKoin() {
    startKoin {
        modules(
            platformModule(),
            commonModule(enableNetworkLogs = true)
        )
    }
}

