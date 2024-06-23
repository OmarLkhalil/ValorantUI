package com.larryyu.valorantui.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.larryyu.valorantui.domain.model.Data
import com.larryyu.valorantui.domain.usecase.AgentDetailsUseCase
import com.larryyu.valorantui.domain.utils.DataState
import com.larryyu.valorantui.ui.intent.AgentDetailsIntent
import com.larryyu.valorantui.ui.model.AgentDetailsReducer
import com.larryyu.valorantui.ui.model.AgentDetailsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class AgentDetailsViewModel @Inject constructor(
    private val agentDetailsUseCase: AgentDetailsUseCase,
    private val agentsReducer: AgentDetailsReducer
) : ViewModel() {

    private val _state = MutableStateFlow(AgentDetailsState())
    val state = _state.asStateFlow()

    private val intentFlow = MutableSharedFlow<AgentDetailsIntent>()


    private suspend fun handleIntent(intent: AgentDetailsIntent, agentId: String) {
        when (intent) {
            is AgentDetailsIntent.FetchAgentDetails -> fetchDetails(agentId)
            else -> _state.value = agentsReducer.reduce(_state.value, intent)
        }
    }

    private suspend fun fetchDetails(agentId: String) {
        _state.value = agentsReducer.reduce(_state.value, AgentDetailsIntent.Loading)
        agentDetailsUseCase(agentId).collectLatest { dataState ->
            _state.value = when (dataState) {
                is DataState.Success -> {
                    Log.d("AgentDetailsViewModel", "fetchDetails: ${dataState.data}")
                    AgentDetailsState(agentDetails = dataState.data.data ?: Data())
                }

                is DataState.Error -> {
                    Log.d("AgentDetailsViewModel", "fetchDetails: ${dataState.exception.message}")
                    AgentDetailsState(error = dataState.exception.message ?: "")
                }

                is DataState.Loading -> {
                    Log.d("AgentDetailsViewModel", "fetchDetails: Loading")
                    AgentDetailsState(isLoading = true)
                }

                else -> {
                    AgentDetailsState()
                }
            }
        }
    }

    fun dispatch(intent: AgentDetailsIntent, agentId: String) {
        viewModelScope.launch {
            handleIntent(intent, agentId)
            intentFlow.emit(intent)
        }
    }

}