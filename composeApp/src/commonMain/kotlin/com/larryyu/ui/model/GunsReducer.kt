package com.larryyu.ui.model


//interface WeaponsReducer<S : State, G : GunsIntent> {
//    fun reduce(state: S, intent: G): S
//}
//
//class GunsReducer @Inject constructor() : WeaponsReducer<GunsState, GunsIntent> {
//    override fun reduce(state: GunsState, intent: GunsIntent): GunsState {
//        return when (intent) {
//            is GunsIntent.FetchGuns -> state.copy(isLoading = true, error = "")
//            is GunsIntent.Error -> state.copy(isLoading = false, error = intent.error)
//            is GunsIntent.Loading -> state.copy(isLoading = true)
//            is GunsIntent.Idle -> state.copy(isLoading = false)
//        }
//    }
//}
//
//interface BundlessReducer<S : State, G : BundlesIntent> {
//    fun reduce(state: S, intent: G): S
//}
//
//class BundlesReducer @Inject constructor() : BundlessReducer<BundlesState, BundlesIntent> {
//    override fun reduce(state: BundlesState, intent: BundlesIntent): BundlesState {
//        return when (intent) {
//            is BundlesIntent.FetchBundles -> state.copy(isLoading = true, error = "")
//            is BundlesIntent.Error -> state.copy(isLoading = false, error = intent.error)
//            is BundlesIntent.Loading -> state.copy(isLoading = true)
//            is BundlesIntent.Idle -> state.copy(isLoading = false)
//        }
//    }
//}