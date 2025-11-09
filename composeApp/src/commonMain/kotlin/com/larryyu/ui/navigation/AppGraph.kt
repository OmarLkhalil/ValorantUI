package com.larryyu.ui.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import org.koin.compose.koinInject
import com.larryyu.ui.view.AgentScreen
import com.larryyu.presentation.viewmodel.AgentsViewModel
import com.larryyu.presentation.uistates.AgentsIntent


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppGraph(
    navigator: Navigator
) {
    val agentsViewModel: AgentsViewModel = koinInject()

    LaunchedEffect(Unit) {
        agentsViewModel.sendIntent(AgentsIntent.FetchAgents)
    }

    SharedTransitionLayout {
        NavHost(
            navigator = navigator,
            initialRoute = Routes.Agents.route
        ) {
            scene(Routes.Agents.route) {
                AgentScreen(
                    onDetailsClick = { agentId ->
                        navigator.navigate(Routes.AgentDetails.createRoute(agentId))
                    }
                )
            }
        }
    }
}
