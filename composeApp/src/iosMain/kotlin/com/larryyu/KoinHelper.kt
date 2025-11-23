package com.larryyu

import com.larryyu.di.commonModule
import com.larryyu.di.platformModule
import org.koin.core.context.startKoin
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName("doInitKoin")
fun doInitKoin() {
    try {
        startKoin {
            printLogger() // Enable Koin logging
            modules(
                platformModule(),
                commonModule(enableNetworkLogs = true)
            )
        }
        println("✅ Koin modules loaded successfully")
    } catch (e: Exception) {
        println("❌ Koin initialization error: ${e.message}")
        e.printStackTrace()
        throw e
    }
}

