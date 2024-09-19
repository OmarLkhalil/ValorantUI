package com.larryyu.valorantui.ui.intent



sealed interface GunsIntent {
    data object FetchGuns : GunsIntent
    data class Error(val error: String) : GunsIntent
    data object Loading : GunsIntent
    data object Idle : GunsIntent
}

sealed interface BundlesIntent {
    data object FetchBundles : BundlesIntent
    data class Error(val error: String) : BundlesIntent
    data object Loading : BundlesIntent
    data object Idle : BundlesIntent
}