package com.larryyu.presentation.uistates

import com.larryyu.presentation.model.AgentDetailsUiModel
import com.larryyu.presentation.viewmodel.UiState


data class AgentDetailsState(
    val agentDetails: AgentDetailsUiModel? = null,
    override val isLoading: Boolean = false,
    override val error: String? = null
) : UiState
