package com.larryyu.presentation.mapper

import com.larryyu.domain.model.AgentsModel
import com.larryyu.presentation.model.AgentUiModel

fun AgentsModel.toUiModel(): AgentUiModel {
    return AgentUiModel(
        agentId = uuid,
        agentName = displayName ?: "Unknown Agent",
        agentImageUrl = fullPortrait ?: "",
        agentRole = role?.displayName ?: "Unknown Role"
    )
}


fun List<AgentsModel>.toUiModels(): List<AgentUiModel> {
    return map { it.toUiModel() }
}


