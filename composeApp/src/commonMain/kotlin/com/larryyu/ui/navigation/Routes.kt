package com.larryyu.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object Agents

@Serializable
object Guns

@Serializable
data class AgentDetails(val agentId: String)
