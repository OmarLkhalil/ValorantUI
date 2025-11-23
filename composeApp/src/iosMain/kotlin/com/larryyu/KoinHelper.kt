package com.larryyu

import com.larryyu.di.initKoin
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

object KoinHelper {
    fun doInitKoin() {
        initKoin()
    }

    @OptIn(ExperimentalObjCName::class)
    @ObjCName("doInitKoinFromSwift")
    fun doInitKoinFromSwift() {
        doInitKoin()
    }
}