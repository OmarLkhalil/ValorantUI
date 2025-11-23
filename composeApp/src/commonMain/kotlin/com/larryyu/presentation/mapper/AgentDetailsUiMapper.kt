package com.larryyu.presentation.mapper

import com.larryyu.domain.model.AbilitiesItemDetails
import com.larryyu.domain.model.AgentDetailsData
import com.larryyu.domain.model.RoleDetails
import com.larryyu.presentation.model.AbilityUiModel
import com.larryyu.presentation.model.AgentDetailsUiModel
import com.larryyu.presentation.model.AgentRoleDetailsUi

fun AgentDetailsData.toUiModel(): AgentDetailsUiModel {
    return AgentDetailsUiModel(
        id = uuid ?: "",
        name = displayName ?: "Unknown Agent",
        description = description ?: "No description available",
        role = role?.toUiModel() ?: AgentRoleDetailsUi("Unknown", ""),
        portraitUrl = fullPortraitV2 ?: fullPortrait ?: "",
        backgroundUrl = background ?: "",
        abilities = abilities?.map { it.toUiModel() } ?: emptyList(),
        backgroundColors = backgroundGradientColors ?: emptyList()
    )
}


fun RoleDetails.toUiModel(): AgentRoleDetailsUi {
    return AgentRoleDetailsUi(
        name = displayName ?: "Unknown",
        iconUrl = displayIcon ?: ""
    )
}


fun AbilitiesItemDetails.toUiModel(): AbilityUiModel {
    return AbilityUiModel(
        name = displayName ?: "Unknown Ability",
        description = description ?: "No description",
        iconUrl = displayIcon ?: "",
        slot = slot ?: "unknown"
    )
}

