package com.larryyu.valorantui.ui.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.larryyu.valorantui.ui.intent.AgentsIntent
import com.larryyu.valorantui.ui.view.AgentDetailsScreen
import com.larryyu.valorantui.ui.view.AgentScreen
import com.larryyu.valorantui.ui.viewmodel.AgentsViewModel


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppGraph(navHostController: NavHostController) {

    val viewModel: AgentsViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.dispatch(AgentsIntent.FetchAgents)
    }
    SharedTransitionLayout {
        NavHost(
            navController = navHostController,
            startDestination = "Agents"
        ) {
            composable("Agents") {
                AgentScreen(agents = state.agents, this@SharedTransitionLayout, this) {
                    navHostController.navigate("AgentDetail/$it")
                }
            }

            composable("AgentDetail/{id}") {
                val agentId = it.arguments?.getString("id")
                AgentDetailsScreen(agentId ?: "", this@SharedTransitionLayout, this)
            }
        }
    }


}