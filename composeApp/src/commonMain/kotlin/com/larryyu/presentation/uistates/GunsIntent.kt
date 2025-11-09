package com.larryyu.presentation.uistates



sealed interface GunsIntent {
    data object FetchGuns : GunsIntent
}

sealed interface BundlesIntent {
    data object FetchBundles : BundlesIntent
}