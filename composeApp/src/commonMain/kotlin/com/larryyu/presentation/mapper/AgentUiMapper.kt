package com.larryyu.presentation.mapper

import com.larryyu.domain.model.AgentsResponseModel
import com.larryyu.presentation.model.AgentUiModel

fun AgentsResponseModel.toUiModel(): AgentUiModel {
    return AgentUiModel(
        agentId = uuid ?: "",
        agentName = displayName ?: "Unknown Agent",
        agentImageUrl = fullPortrait ?: "",
        agentRole = role?.displayName ?: "Unknown Role"
    )
}

fun List<AgentsResponseModel>.toUiModels(): List<AgentUiModel> {
    return map { it.toUiModel() }
}


