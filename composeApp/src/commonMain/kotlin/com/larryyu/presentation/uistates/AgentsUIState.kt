package com.larryyu.presentation.uistates

import com.larryyu.domain.model.AgentsModel

data class AgentsUIState(
    val agents: List<AgentsModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
