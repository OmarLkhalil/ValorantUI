package com.larryyu.presentation.mapper

import com.larryyu.domain.model.AgentsModel
import com.larryyu.domain.model.Role
import com.larryyu.presentation.model.AgentRoleUi
import com.larryyu.presentation.model.AgentUiModel

fun AgentsModel.toUiModel(): AgentUiModel {
    return AgentUiModel(
        id = uuid,
        name = displayName ?: "Unknown Agent",
        portraitUrl = fullPortrait ?: "",
        roleName = role?.displayName ?: "Unknown Role",
        portraitV2Url = fullPortraitV2 ?: ""
    )
}


fun List<AgentsModel>.toUiModels(): List<AgentUiModel> {
    return map { it.toUiModel() }
}


fun Role.toUiModel(): AgentRoleUi {
    return AgentRoleUi(
        name = displayName ?: "Unknown"
    )
}

