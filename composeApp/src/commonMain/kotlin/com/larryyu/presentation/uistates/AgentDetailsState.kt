package com.larryyu.presentation.uistates

import com.larryyu.domain.model.AgentDetailsData
import com.larryyu.presentation.viewmodel.UiState


data class AgentDetailsState(
    val agentDetails : AgentDetailsData = AgentDetailsData(),
    override val isLoading: Boolean = false,
    override val error: String? = null
) : UiState
