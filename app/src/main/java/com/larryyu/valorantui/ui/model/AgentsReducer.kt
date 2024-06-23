package com.larryyu.valorantui.ui.model

import com.larryyu.valorantui.ui.intent.AgentDetailsIntent
import com.larryyu.valorantui.ui.intent.AgentsIntent
import javax.inject.Inject

interface Reducer<S : State, A : AgentsIntent> {
    fun reduce(currentState: S, action: A): S
}

class AgentsReducer @Inject constructor() : Reducer<AgentsState, AgentsIntent> {
    override fun reduce(currentState: AgentsState, action: AgentsIntent): AgentsState {
        return when (action) {
            is AgentsIntent.FetchAgents -> currentState.copy(isLoading = true, error = "")
            is AgentsIntent.Error -> currentState.copy(isLoading = false, error = action.error)
            is AgentsIntent.Loading -> currentState.copy(isLoading = true)
            is AgentsIntent.Idle -> currentState.copy(isLoading = false)
        }
    }
}

interface DetailsReducer<S : State, A : AgentDetailsIntent> {
    fun reduce(currentState: S, action: A): S
}

class AgentDetailsReducer @Inject constructor() : DetailsReducer<AgentDetailsState, AgentDetailsIntent>{
    override fun reduce(
        currentState: AgentDetailsState,
        action: AgentDetailsIntent
    ): AgentDetailsState {
        return when (action) {
            is AgentDetailsIntent.FetchAgentDetails -> currentState.copy(isLoading = true, error = "")
            is AgentDetailsIntent.Error -> currentState.copy(isLoading = false, error = action.error)
            is AgentDetailsIntent.Loading -> currentState.copy(isLoading = true)
            is AgentDetailsIntent.Idle -> currentState.copy(isLoading = false)
        }
    }
}
