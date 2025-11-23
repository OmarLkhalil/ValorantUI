package com.larryyu.presentation.model


data class AgentUiModel(
    val id: String,
    val name: String,
    val portraitUrl: String,
    val roleName: String,
    val portraitV2Url: String
)

data class AgentRoleUi(
    val name: String
)

