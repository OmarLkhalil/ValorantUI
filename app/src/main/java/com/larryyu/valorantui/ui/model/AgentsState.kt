package com.larryyu.valorantui.ui.model

import com.larryyu.valorantui.domain.model.AgentsData

interface State

data class AgentsState(
    val agents: List<AgentsData> = emptyList(),
    val error: String = "",
    val isLoading: Boolean = false
) : State