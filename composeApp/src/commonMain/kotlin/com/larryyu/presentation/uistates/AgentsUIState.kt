package com.larryyu.presentation.uistates

import com.larryyu.presentation.model.AgentUiModel


data class AgentsUIState(
    val agents: List<AgentUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
