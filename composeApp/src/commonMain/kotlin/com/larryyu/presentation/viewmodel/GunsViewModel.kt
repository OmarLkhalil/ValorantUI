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
            is GunsIntent.FetchGuns -> {
                fetchAllData()
            }
        }
    }

    private suspend fun fetchAllData() {
        // Start loading
        setState { copy(isLoading = true, error = null) }

        try {
            // Fetch guns
            collectDataState(getAllGunsUseCase()) { response ->
                setState { copy(guns = response.data ?: emptyList()) }
            }

            // Fetch bundles
            collectDataState(getAllBundlesUseCase()) { response ->
                setState { copy(bundles = response.data ?: emptyList()) }
            }

        } catch (e: Exception) {
            setState { copy(error = e.message ?: "Unknown error") }
        } finally {
            // Stop loading in all cases
            setState { copy(isLoading = false) }
        }
    }

    override fun onLoading() {
    }

    override fun onError(e: Throwable) {
        super.onError(e)
        setState { copy(isLoading = false, error = e.message) }
    }

    override fun onIdle() {
    }
}