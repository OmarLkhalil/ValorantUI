package com.larryyu.domain.model
import kotlinx.serialization.Serializable
@Serializable
data class AgentsModel(
    val displayName: String? = null,
    val uuid: String,
    val fullPortrait: String? = null,
    val role: Role? = null,
    val fullPortraitV2: String? = null
)
@Serializable
data class Role(
    val displayName: String? = null
)
