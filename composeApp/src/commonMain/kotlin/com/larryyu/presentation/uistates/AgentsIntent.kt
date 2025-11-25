package com.larryyu.presentation.uistates

sealed interface AgentsIntent {
    data object FetchAgents : AgentsIntent
    data object RefreshAgents : AgentsIntent
}

sealed interface AgentDetailsIntent {
    data class FetchAgentDetails(val agentId: String) : AgentDetailsIntent
}
