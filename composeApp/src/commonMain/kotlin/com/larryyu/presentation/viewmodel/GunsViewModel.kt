package com.larryyu.presentation.viewmodel

import com.larryyu.domain.usecase.GetAllBundlesUseCase
import com.larryyu.domain.usecase.GetAllGunsUseCase
import com.larryyu.presentation.mapper.toBundleUiModels
import com.larryyu.presentation.mapper.toUiModels
import com.larryyu.presentation.uistates.GunsIntent
import com.larryyu.presentation.uistates.GunsState
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class GunsViewModel(
    private val getAllGunsUseCase: GetAllGunsUseCase,
    private val getAllBundlesUseCase: GetAllBundlesUseCase
) : BaseViewModel<GunsIntent, GunsState>(GunsState()) {

    val gState = state

    override suspend fun handleIntent(intent: GunsIntent) {
        super.handleIntent(intent)
        when (intent) {
            is GunsIntent.FetchGuns -> {
                fetchWeaponsAndBundles()
            }
        }
    }

    private suspend fun fetchWeaponsAndBundles() = coroutineScope {
        setState { copy(isLoading = true, error = null) }
        try {
            val weaponsDeferred = async {
                var weapons = emptyList<com.larryyu.presentation.model.WeaponUiModel>()
                collectDataState(getAllGunsUseCase()) { response ->
                    val domainWeapons = response.data ?: emptyList()
                    weapons = domainWeapons.toUiModels()
                }
                weapons
            }

            val bundlesDeferred = async {
                var bundles = emptyList<com.larryyu.presentation.model.BundleUiModel>()
                collectDataState(getAllBundlesUseCase()) { response ->
                    val domainBundles = response.data ?: emptyList()
                    bundles = domainBundles.toBundleUiModels()
                }
                bundles
            }

            val weapons = weaponsDeferred.await()
            val bundles = bundlesDeferred.await()

            setState { copy(weapons = weapons, bundles = bundles) }
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
