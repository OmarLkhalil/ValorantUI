package com.larryyu.presentation.viewmodel

import com.larryyu.domain.usecase.GetAllGunsUseCase
import com.larryyu.presentation.mapper.toUiModels
import com.larryyu.presentation.uistates.GunsIntent
import com.larryyu.presentation.uistates.GunsState

class GunsViewModel(
    private val getAllGunsUseCase: GetAllGunsUseCase,
) : BaseViewModel<GunsIntent, GunsState>(GunsState()) {

    val gState = state

    override suspend fun handleIntent(intent: GunsIntent) {
        super.handleIntent(intent)
        when (intent) {
            is GunsIntent.FetchGuns -> {
                fetchWeapons()
            }
        }
    }

    private suspend fun fetchWeapons() {
        setState { copy(isLoading = true, error = null) }
        try {
            collectDataState(getAllGunsUseCase()) { response ->
                val domainWeapons = response.data ?: emptyList()
                val uiWeapons = domainWeapons.toUiModels()

                setState { copy(weapons = uiWeapons) }
            }
        } catch (e: Exception) {
            setState { copy(error = e.message ?: "Unknown error") }
        } finally {
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
