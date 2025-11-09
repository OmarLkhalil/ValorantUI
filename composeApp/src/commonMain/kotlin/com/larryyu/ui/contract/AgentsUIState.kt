package com.larryyu.ui.contract

import com.larryyu.domain.model.AgentsModel

data class AgentsUIState(
    val agents: List<AgentsModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
