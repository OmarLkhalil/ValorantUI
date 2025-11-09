package com.larryyu.ui.model

import com.larryyu.domain.model.AgentDetailsData
import com.larryyu.domain.model.AgentsModel
import com.larryyu.ui.viewmodel.UiState


data class AgentDetailsState(
    val agentDetails : AgentDetailsData = AgentDetailsData(),
    override val isLoading: Boolean = false,
    override val error: String? = null
) : UiState
