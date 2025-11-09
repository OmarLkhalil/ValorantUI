package com.larryyu.presentation.uistates

sealed interface AgentsIntent {
    data object FetchAgents : AgentsIntent
    data object RefreshAgents : AgentsIntent
    data class OpenDetails(val agentId: String) : AgentsIntent
}

sealed interface AgentDetailsIntent {
    data class FetchAgentDetails(val agentId: String) : AgentDetailsIntent
}