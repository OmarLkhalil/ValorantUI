package com.larryyu.valorantui.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.larryyu.valorantui.domain.usecase.GetAllBundlesUseCase
import com.larryyu.valorantui.domain.usecase.GetAllGunsUseCase
import com.larryyu.valorantui.domain.utils.DataState
import com.larryyu.valorantui.ui.intent.BundlesIntent
import com.larryyu.valorantui.ui.intent.GunsIntent
import com.larryyu.valorantui.ui.model.BundlesReducer
import com.larryyu.valorantui.ui.model.BundlesState
import com.larryyu.valorantui.ui.model.GunsReducer
import com.larryyu.valorantui.ui.model.GunsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
//

@HiltViewModel
class GunsViewModel @Inject constructor(
    private val getAllGunsUseCase: GetAllGunsUseCase,
    private val getAllBundlesUseCase: GetAllBundlesUseCase,
    private val gunsReducer: GunsReducer,
    private val bundlesReducer: BundlesReducer
) : ViewModel() {

    private val _gunsState = MutableStateFlow(GunsState())
    val gunsState = _gunsState.asStateFlow()

    private val gunsIntentFlow = MutableSharedFlow<GunsIntent>()

    private suspend fun handleIntent(intent: GunsIntent) {
        when (intent) {
            is GunsIntent.FetchGuns -> fetchGuns()
            is GunsIntent.Loading -> _gunsState.value = gunsReducer.reduce(_gunsState.value, intent)
            is GunsIntent.Error -> _gunsState.value = gunsReducer.reduce(_gunsState.value, intent)
            else -> _gunsState.value = gunsReducer.reduce(_gunsState.value, intent)
        }
    }

    private suspend fun fetchGuns() {
        _gunsState.value = gunsReducer.reduce(_gunsState.value, GunsIntent.Loading)
        getAllGunsUseCase().collectLatest { dataState ->
            _gunsState.value = when (dataState) {
                is DataState.Success -> {
                    Log.d("GunsViewModel", "fetchGuns success ${dataState.data}")
                    GunsState(guns = dataState.data.data ?: emptyList())
                }

                is DataState.Error -> {
                    Log.e("GunsViewModel", "fetchGuns error: ${dataState.exception.message}")
                    GunsState(error = dataState.exception.message ?: "")
                }

                is DataState.Loading -> {
                    Log.d("GunsViewModel", "fetchGuns loading")
                    GunsState(isLoading = true)
                }

                else -> {
                    GunsState()
                }
            }
        }
    }


    private val _bundlesState = MutableStateFlow(BundlesState())
    val bundlesState = _bundlesState.asStateFlow()

    private val bundlesFlow = MutableSharedFlow<BundlesIntent>()

    private suspend fun handleBundleIntent(intent: BundlesIntent) {
        when (intent) {
            is BundlesIntent.FetchBundles -> fetchBundles()
            else -> {
                _bundlesState.value = bundlesReducer.reduce(
                    _bundlesState.value,
                    intent
                )
            }
        }
    }

    suspend fun fetchBundles() {
        _bundlesState.value = bundlesReducer.reduce(
            _bundlesState.value,
            BundlesIntent.Loading
        )
        getAllBundlesUseCase().collectLatest { dataState ->
            _bundlesState.value = when (dataState) {
                is DataState.Success -> {
                    BundlesState(bundles = dataState.data.data ?: emptyList())
                }

                is DataState.Error -> {
                    BundlesState(error = dataState.exception.message ?: "")
                }

                is DataState.Loading -> {
                    BundlesState(isLoading = true)
                }

                else -> {
                    BundlesState()
                }
            }
        }
    }


    fun dispatch(intent: GunsIntent, gunsIntent: BundlesIntent) {
        viewModelScope.launch {
            handleIntent(intent)
            handleBundleIntent(gunsIntent)
            gunsIntentFlow.emit(intent)
            bundlesFlow.emit(gunsIntent)
        }
    }
}