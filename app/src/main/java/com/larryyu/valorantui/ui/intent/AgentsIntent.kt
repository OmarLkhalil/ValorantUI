package com.larryyu.valorantui.ui.intent


sealed interface AgentsIntent{
    data object FetchAgents : AgentsIntent
    data class Error(val error: String) : AgentsIntent
    data object Loading : AgentsIntent
    data object Idle : AgentsIntent
}

sealed interface AgentDetailsIntent{
    data class FetchAgentDetails(val agentId: String) : AgentDetailsIntent
    data class Error(val error: String) : AgentDetailsIntent
    data object Loading : AgentDetailsIntent
    data object Idle : AgentDetailsIntent
}