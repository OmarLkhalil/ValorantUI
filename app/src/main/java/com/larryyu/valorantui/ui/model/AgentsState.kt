package com.larryyu.valorantui.ui.model

import com.larryyu.valorantui.domain.model.AgentItem

interface State

data class AgentsState(
    val agents: List<AgentItem> = emptyList(),
    val error: String = "",
    val isLoading: Boolean = false
) : State