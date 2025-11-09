package com.larryyu.presentation.viewmodel

import com.larryyu.domain.usecase.GetAllBundlesUseCase
import com.larryyu.domain.usecase.GetAllGunsUseCase
import com.larryyu.presentation.uistates.GunsIntent
import com.larryyu.presentation.uistates.GunsState


class GunsViewModel(
    private val getAllGunsUseCase: GetAllGunsUseCase,
    private val getAllBundlesUseCase: GetAllBundlesUseCase,
) : BaseViewModel<GunsIntent, GunsState>(GunsState()) {
    val gState = state
    override suspend fun handleIntent(intent: GunsIntent) {
        super.handleIntent(intent)
        when (intent) {
            is GunsIntent.FetchGuns -> fetchGuns()
        }
    }

    private suspend fun fetchGuns() {
        collectDataState(getAllGunsUseCase()) { response ->
            setState { copy(guns = response.data ?: emptyList()) }
        }
    }

    override fun onLoading() {
        super.onLoading()
        setState { copy(isLoading = true, error = null) }
    }

    override fun onError(e: Throwable) {
        super.onError(e)
        setState { copy(isLoading = false, error = e.message) }
    }

    override fun onIdle() {
        super.onIdle()
        setState { copy(isLoading = false, error = null) }
    }
}