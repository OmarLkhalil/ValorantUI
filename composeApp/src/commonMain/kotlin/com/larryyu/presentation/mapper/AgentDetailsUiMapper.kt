package com.larryyu.presentation.mapper

import com.larryyu.domain.model.AbilitiesItemDetails
import com.larryyu.domain.model.AgentDetailsData
import com.larryyu.domain.model.RoleDetails
import com.larryyu.presentation.model.AbilityUiModel
import com.larryyu.presentation.model.AgentDetailsUiModel
import com.larryyu.presentation.model.AgentRoleDetailsUi


fun AgentDetailsData.toUiModel(): AgentDetailsUiModel {
    return AgentDetailsUiModel(
        agentId = uuid ?: "",
        agentName = displayName ?: "Unknown Agent",
        agentDescription = description ?: "No description available",
        agentRole = role?.toUiModel() ?: AgentRoleDetailsUi("Unknown", ""),
        agentImageUrl = fullPortrait ?: "",
        agentAbilities = abilities?.map { it.toUiModel() } ?: emptyList()
    )
}


fun RoleDetails.toUiModel(): AgentRoleDetailsUi {
    return AgentRoleDetailsUi(
        roleName = displayName ?: "Unknown",
        roleIconUrl = displayIcon ?: ""
    )
}

fun AbilitiesItemDetails.toUiModel(): AbilityUiModel {
    return AbilityUiModel(
        abilityName = displayName ?: "Unknown Ability",
        abilityDescription = description ?: "No description",
        abilityIconUrl = displayIcon ?: ""
    )
}

