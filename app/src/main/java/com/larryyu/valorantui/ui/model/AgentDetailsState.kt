package com.larryyu.valorantui.ui.model

import com.larryyu.valorantui.domain.model.Data

data class AgentDetailsState(
    val agentDetails : Data = Data(),
    val error: String = "",
    val isLoading: Boolean = false
): State