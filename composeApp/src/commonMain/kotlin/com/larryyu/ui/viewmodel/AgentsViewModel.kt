package com.larryyu.ui.viewmodel

import androidx.compose.ui.graphics.Color
import com.larryyu.domain.repository.AgentsRepo
import com.larryyu.domain.utils.DataState
import com.larryyu.ui.contract.AgentsIntent
import com.larryyu.ui.contract.AgentsUIState
import com.larryyu.ui.extensions.EventFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent

class AgentsViewModel(
    private val agentsRepo: AgentsRepo
) : KoinComponent {

    private val _agentsState = MutableStateFlow(AgentsUIState())
    val agentsState: StateFlow<AgentsUIState> = _agentsState

    private val viewModelScope = CoroutineScope(Dispatchers.IO)

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

                    is AgentsIntent.OpenDetails -> {
                        navigateToDetails(agentIntent.agentId)
                    }
                }
            }
        }
    }

    private val _navigationEvent = EventFlow<String>()
    val navigationEvent = _navigationEvent.events
    fun navigateToDetails(agentId: String) {
        viewModelScope.launch {
            _navigationEvent.emit(agentId)
        }
    }

    private fun loadAgents() = launchAgentsViewModelScope {
        agentsRepo.getAgents().collect { result ->
            when (result) {
                is DataState.Success -> {
                    updateAgentsUiState(
                        AgentsUIState().copy(
                            agents = result.data.data ?: emptyList(),
                            isLoading = false
                        )
                    )
                }
                is DataState.Loading -> {
                    updateAgentsUiState(AgentsUIState().copy(isLoading = true))
                }
                is DataState.Error -> {
                    updateAgentsUiState(
                        AgentsUIState().copy(
                            isLoading = false,
                            error = result.exception.message.orEmpty()
                        )
                    )
                }
                is DataState.Idle -> {
                    updateAgentsUiState(AgentsUIState().copy(isLoading = false))
                }
            }
        }
    }

    private fun launchAgentsViewModelScope(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
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
        withContext(Dispatchers.Main) {
            _agentsState.emit(uiState)
        }
    }


//

}
