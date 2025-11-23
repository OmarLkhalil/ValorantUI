package com.larryyu

import com.larryyu.di.commonModule
import com.larryyu.di.platformModule
import org.koin.core.context.startKoin
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName("doInitKoin")
fun doInitKoin() {
    startKoin {
        modules(
            platformModule(),
            commonModule(enableNetworkLogs = true)
        )
    }
}

