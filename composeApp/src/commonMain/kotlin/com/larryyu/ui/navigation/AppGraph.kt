package com.larryyu.ui.navigation
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.koin.compose.koinInject
import com.larryyu.presentation.viewmodel.AgentsViewModel
import com.larryyu.presentation.viewmodel.GunsViewModel
import com.larryyu.presentation.uistates.AgentsIntent
import com.larryyu.presentation.uistates.GunsIntent
import com.larryyu.ui.theme.ThemeViewModel
import com.larryyu.ui.view.AgentsScreen
import com.larryyu.ui.view.GunsScreen
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppGraph(
    navController: NavHostController
) {
    val themeViewModel: ThemeViewModel = koinInject()
    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = Agents
        ) {
            composable<Agents> {
                AgentsScreen()
            }
            composable<Guns> {
                GunsScreen(
                    onThemeToggle = { themeViewModel.toggleTheme() }
                )
            }
        }
    }
}
