package com.larryyu.ui.navigation

sealed class Routes(val route: String) {

    object Agents : Routes("Agents")
    object Guns : Routes("Guns")

    object AgentDetails : Routes("AgentDetail/{id}") {
        fun createRoute(agentId: String): String {
            return "AgentDetail/$agentId"
        }
    }
}