package com.larryyu.ui.contract



sealed interface GunsIntent {
    data object FetchGuns : GunsIntent
}

sealed interface BundlesIntent {
    data object FetchBundles : BundlesIntent
}