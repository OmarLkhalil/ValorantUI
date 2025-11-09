package com.larryyu

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.rememberNavigator
import com.larryyu.ui.navigation.AppGraph
import com.larryyu.ui.navigation.Routes
import com.larryyu.ui.theme.AppTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import valorantui.composeapp.generated.resources.dryme
import org.jetbrains.compose.resources.Font
import androidx.compose.ui.text.font.FontFamily
import valorantui.composeapp.generated.resources.Res

@Composable
fun App() {
    PreComposeApp {
        AppTheme {
            val navigator = rememberNavigator()

            val bottomItems = listOf(
                Routes.Agents.route,
                Routes.Guns.route
            )

            val currentRoute = navigator.currentEntry.collectAsState(null).value?.route?.route

            Scaffold(
                bottomBar = {
                    AIBottomNavigation(
                        items = bottomItems,
                        currentRoute = currentRoute,
                        onNavigate = { route ->
                            navigator.navigate(route)
                        }
                    )
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                ) {
                    AppGraph(navigator = navigator)
                }
            }
        }
    }
}


@Composable
fun AIBottomNavigation(
    items: List<String>,
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    androidx.compose.material.BottomNavigation(
        backgroundColor = Color.Black,
        elevation = 0.dp,
        modifier = Modifier.height(50.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { route ->
                val isSelected = currentRoute == route

                BottomNavigationItem(
                    title = route.replaceFirstChar { it.uppercase() },
                    selected = isSelected,
                    onClick = { onNavigate(route) }
                )
            }
        }
    }
}


@Composable
fun BottomNavigationItem(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(80.dp)
            .clickable { onClick() }
            .height(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(2.dp)
                    .background(Color.White)
            )
        }

        Text(
            text = title,
            modifier = Modifier.padding(top = 6.dp),
            color = Color.White,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(Res.font.dryme)),
            fontSize = 16.sp
        )
    }
}
