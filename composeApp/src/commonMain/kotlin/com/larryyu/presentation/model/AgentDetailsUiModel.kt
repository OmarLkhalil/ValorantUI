package com.larryyu.presentation.model

data class AgentDetailsUiModel(
    val id: String,
    val name: String,
    val description: String,
    val role: AgentRoleDetailsUi,
    val portraitUrl: String,
    val backgroundUrl: String,
    val abilities: List<AbilityUiModel>,
    val backgroundColors: List<String>
)

data class AgentRoleDetailsUi(
    val name: String,
    val iconUrl: String
)

data class AbilityUiModel(
    val name: String,
    val description: String,
    val iconUrl: String,
    val slot: String
)

