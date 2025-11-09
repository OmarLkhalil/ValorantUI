package com.larryyu.ui.viewmodel

import com.larryyu.domain.utils.DataState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

abstract class BaseViewModel<I, S : UiState>(
    initialState: S
) :KoinComponent{

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    private val intentFlow = MutableSharedFlow<I>()

    private val viewModelScope = CoroutineScope(Dispatchers.IO)

    init {
        viewModelScope.launch {
            intentFlow.collect { intent ->
                handleIntent(intent)
            }
        }
    }

    fun dispatch(intent: I) {
        viewModelScope.launch { intentFlow.emit(intent) }
    }

    protected fun setState(reducer: S.() -> S) {
        _state.value = _state.value.reducer()
    }

    protected suspend fun <T> collectDataState(
        flow: Flow<DataState<T>>,
        onSuccess: (T) -> Unit
    ) {
        flow.collect { dataState ->
            when (dataState) {
                is DataState.Loading -> onLoading()
                is DataState.Error -> onError(dataState.exception)
                is DataState.Success -> {
                    onSuccess(dataState.data)
                }
                DataState.Idle -> onIdle()
            }
        }
    }

    protected open suspend fun handleIntent(intent: I) {
    }

    protected open fun onLoading() {}
    protected open fun onError(e: Throwable) {}
    protected open fun onIdle() {}

}

interface UiState {
    val isLoading: Boolean
    val error: String?
}
