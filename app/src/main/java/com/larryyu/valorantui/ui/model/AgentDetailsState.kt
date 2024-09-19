package com.larryyu.valorantui.ui.model

import com.larryyu.valorantui.domain.model.AgentDetailsData


data class AgentDetailsState(
    val agentDetails : AgentDetailsData = AgentDetailsData(),
    val error: String = "",
    val isLoading: Boolean = false
): State