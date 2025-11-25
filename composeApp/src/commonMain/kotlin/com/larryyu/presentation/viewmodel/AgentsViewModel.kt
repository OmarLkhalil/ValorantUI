package com.larryyu.presentation.viewmodel

import com.larryyu.domain.repository.AgentsRepo
import com.larryyu.domain.utils.DataState
import com.larryyu.presentation.mapper.toUiModels
import com.larryyu.presentation.uistates.AgentsIntent
import com.larryyu.presentation.uistates.AgentsUIState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class AgentsViewModel(
    private val agentsRepo: AgentsRepo
) : KoinComponent {

    private val _agentsState = MutableStateFlow(AgentsUIState())
    val agentsState: StateFlow<AgentsUIState> = _agentsState
    private val viewModelScope = CoroutineScope(Dispatchers.Default)
    private val intentState = MutableSharedFlow<AgentsIntent>()

    init {
        processIntents()
    }

    fun sendIntent(intent: AgentsIntent) {
        viewModelScope.launch {
            intentState.emit(intent)
        }
    }

    private fun processIntents() {
        viewModelScope.launch {
            intentState.collect { agentIntent ->
                when (agentIntent) {
                    AgentsIntent.FetchAgents, AgentsIntent.RefreshAgents -> {
                        loadAgents()
                    }
//                    is AgentsIntent.OpenDetails -> {
//                        navigateToDetails(agentIntent.agentId)
//                    }
                }
            }
        }
    }
//    private val _navigationEvent = EventFlow<String>()
//    val navigationEvent = _navigationEvent.events
//    fun navigateToDetails(agentId: String) {
//        viewModelScope.launch {
//            _navigationEvent.emit(agentId)
//        }
//    }
    private fun loadAgents() = launchAgentsViewModelScope {
        agentsRepo.getAgents().collect { result ->
            when (result) {
                is DataState.Success -> {
                    val domainAgents = result.data.data ?: emptyList()
                    val uiAgents = domainAgents.toUiModels()

                    updateAgentsUiState(
                        _agentsState.value.copy(
                            agents = uiAgents,
                            isLoading = false,
                            error = null
                        )
                    )
                }
                is DataState.Loading -> {
                    updateAgentsUiState(_agentsState.value.copy(isLoading = true))
                }
                is DataState.Error -> {
                    updateAgentsUiState(
                        _agentsState.value.copy(
                            isLoading = false,
                            error = result.exception.message ?: "Unknown error"
                        )
                    )
                }
                is DataState.Idle -> {
                    updateAgentsUiState(_agentsState.value.copy(isLoading = false))
                }
            }
        }
    }
    private fun launchAgentsViewModelScope(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch {
            try {
                block()
            } catch (throwable: Throwable) {
                updateAgentsUiState(
                    AgentsUIState().copy(
                        isLoading = false,
                        error = throwable.message.orEmpty()
                    )
                )
            }
        }
    }

    private suspend fun updateAgentsUiState(uiState: AgentsUIState) {
        _agentsState.emit(uiState)
    }
}
