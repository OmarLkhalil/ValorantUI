package com.larryyu.presentation.model



data class AgentDetailsUiModel(
    val agentId: String,
    val agentName: String,
    val agentDescription: String,
    val agentRole: AgentRoleDetailsUi,
    val agentImageUrl: String,
    val agentAbilities: List<AbilityUiModel>
)

data class AgentRoleDetailsUi(
    val roleName: String,
    val roleIconUrl: String
)

data class AbilityUiModel(
    val abilityName: String,
    val abilityDescription: String,
    val abilityIconUrl: String
)

