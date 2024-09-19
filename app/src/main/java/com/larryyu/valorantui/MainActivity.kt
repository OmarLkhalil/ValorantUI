package com.larryyu.valorantui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigation
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.rememberNavController
import com.larryyu.valorantui.ui.navigation.AppGraph
import com.larryyu.valorantui.ui.theme.ValorantUITheme
import dagger.hilt.android.AndroidEntryPoint
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ValorantUITheme {
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = {
                        val currentScreen by navController.currentScreenAsState()
                        AIBottomNavigation(
                            modifier = Modifier.fillMaxWidth().height(40.sdp),
                            navController = navController,
                            currentSelectedScreen = currentScreen
                        )
                    }
                ) {
                    Box(modifier = Modifier.padding(it)) {
                        AppGraph(navHostController = navController)
                    }
                }
            }
        }
    }
}


@Composable
private fun AIBottomNavigation(
    navController: NavController,
    currentSelectedScreen: String,
    modifier: Modifier = Modifier,
) {
    BottomNavigation(
        modifier = modifier,
        backgroundColor = Color.Black,
        elevation = 0.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            BottomNavigationItem(
                selected = currentSelectedScreen == "Agents",
                onClick = {
                    navController.navigateToRootScreen("Agents")
                },
                title = "Agents"
            )
            BottomNavigationItem(
                selected = currentSelectedScreen == "Guns",
                onClick = {
                    navController.navigateToRootScreen("Guns")
                },
                title = "Guns"
            )
        }

    }


}


@Composable
private fun BottomNavigationItem(
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    title: String,
) {
    Column(
        modifier = modifier
            .width(80.sdp)
            .clickable {
                onClick()
            }
            .height(35.sdp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .width(80.sdp)
                    .background(Color.White)
                    .height(2.sdp)
            )
        }
        Text(
            text = title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.sdp),
            color = Color.White,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.dryme)),
            fontSize = 18.ssp
        )
    }
}

@Stable
@Composable
private fun NavController.currentScreenAsState(): State<String> {
    val selectedItem = remember { mutableStateOf("Agents") }
    DisposableEffect(key1 = this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when {
                destination.hierarchy.any { it.route == "Agents" } -> {
                    selectedItem.value = "Agents"
                }

                destination.hierarchy.any { it.route == "Guns" } -> {
                    selectedItem.value = "Guns"
                }

            }

        }
        addOnDestinationChangedListener(listener)
        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }
    return selectedItem
}


private fun NavController.navigateToRootScreen(roots: String) {
    navigate(roots) {
        launchSingleTop = true
        restoreState = true
    }
}
