package com.larryyu
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
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.larryyu.ui.navigation.AppGraph
import com.larryyu.ui.navigation.Agents
import com.larryyu.ui.navigation.Guns
import com.larryyu.ui.theme.ValorantUITheme
import org.jetbrains.compose.resources.Font
import valorantui.composeapp.generated.resources.Res
import valorantui.composeapp.generated.resources.dryme
@Composable
fun App() {
    ValorantUITheme {
        val navController = rememberNavController()
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route
        val bottomItems = listOf(
            BottomNavItem(Agents, "Agents"),
            BottomNavItem(Guns, "Guns")
        )
        Scaffold(
            bottomBar = {
                AIBottomNavigation(
                    items = bottomItems,
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                AppGraph(navController = navController)
            }
        }
    }
}
data class BottomNavItem(val route: Any, val title: String)
@Composable
fun AIBottomNavigation(
    items: List<BottomNavItem>,
    currentRoute: String?,
    onNavigate: (Any) -> Unit
) {
    NavigationBar(
        containerColor = Color.Black,
        modifier = Modifier.height(50.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val routeClass = item.route::class.qualifiedName
                val isSelected = currentRoute?.contains(routeClass ?: "") == true
                BottomNavigationItem(
                    title = item.title,
                    selected = isSelected,
                    onClick = { onNavigate(item.route) }
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
