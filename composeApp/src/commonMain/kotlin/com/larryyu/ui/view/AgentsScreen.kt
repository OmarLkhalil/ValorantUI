package com.larryyu.ui.view

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.larryyu.domain.model.AgentsModel
import com.larryyu.presentation.uistates.AgentsIntent
import com.larryyu.presentation.uistates.AgentsUIState
import com.larryyu.presentation.viewmodel.AgentsViewModel
import com.larryyu.ui.components.CoilImage
import com.larryyu.ui.components.ThemeWipeOverlay
import com.larryyu.ui.components.calculateDominantColor
import com.larryyu.ui.theme.Theme
import com.larryyu.ui.theme.ThemeViewModel
import kotlinx.coroutines.launch
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import valorantui.composeapp.generated.resources.Res
import valorantui.composeapp.generated.resources.arrow
import valorantui.composeapp.generated.resources.dryme
import valorantui.composeapp.generated.resources.valorant


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AgentsScreen(
    viewModel: AgentsViewModel = koinInject(),
    // onDetailsClick: (String) -> Unit // Removed as internal navigation is handled by selectedAgent state
) {
    val agentsState by viewModel.agentsState.collectAsStateWithLifecycle()
    val selectedAgent = remember { mutableStateOf<AgentsModel?>(null) }

    LaunchedEffect(Unit) {
        viewModel.sendIntent(AgentsIntent.FetchAgents)
    }

    SharedTransitionLayout {
        AnimatedContent(
            targetState = selectedAgent.value,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "agentsTransition"
        ) { targetAgent ->
            if (targetAgent == null) {
                // State-hoisting component for the grid
                AgentsGridRoute(
                    agentsState = agentsState,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this,
                    onAgentClick = { selectedAgent.value = it }
                )
            } else {
                AgentDetailsScreen(
                    agentId = targetAgent.uuid,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this,
                    onBack = { selectedAgent.value = null }
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AgentsGridRoute(
    agentsState: AgentsUIState,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: ThemeViewModel = koinInject(),
    onAgentClick: (AgentsModel) -> Unit
) {
    val isDarkTheme by viewModel.isDarkTheme.collectAsState()
    var startAnimation by remember { mutableStateOf(false) }
    val overlayFraction = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    // Animation Logic
    if (startAnimation) {
        LaunchedEffect(isDarkTheme) {
            overlayFraction.snapTo(0f)
            scope.launch {
                overlayFraction.animateTo(
                    1f,
                    animationSpec = keyframes {
                        durationMillis = 850
                        0.9f at 520 with CubicBezierEasing(0.2f, 0.8f, 0.2f, 1.0f)
                        1.03f at 700
                        1f at 850 with FastOutSlowInEasing
                    }
                )
                viewModel.toggleTheme() // بعد الانميشن فقط
                startAnimation = false
            }
        }
    }

    // Call the presentation component
    AgentsGridContent(
        agentsState = agentsState,
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope,
        overlayFraction = overlayFraction.value,
        onAgentClick = onAgentClick,
        onAnimateClick = { startAnimation = true }
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AgentsGridContent(
    agentsState: AgentsUIState,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    overlayFraction: Float,
    onAgentClick: (AgentsModel) -> Unit,
    onAnimateClick: () -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = Theme.colors.background,
            topBar = {
                AgentsTopBar(
                    onAnimateClick = onAnimateClick
                )
            }
        ) { padding ->
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                when {
                    agentsState.isLoading -> CircularProgressIndicator(
                        Modifier.align(Alignment.Center),
                        color = Theme.colors.textPrimary
                    )

                    agentsState.error != null -> Text(
                        text = agentsState.error,
                        color = Theme.colors.error,
                        modifier = Modifier.align(Alignment.Center)
                    )

                    else -> LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(agentsState.agents, key = { it.uuid }) { agent ->
                            AgentCard(
                                agent = agent,
                                sharedTransitionScope = sharedTransitionScope,
                                animatedVisibilityScope = animatedVisibilityScope,
                                onItemClick = { onAgentClick(agent) }
                            )
                        }
                    }
                }
            }
        }
        ThemeWipeOverlay(fraction = overlayFraction)
    }
}


@Composable
fun AgentsTopBar(
    onAnimateClick: () -> Unit,
) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(top = 20.dp, bottom = 3.dp)
    ) {

        CoilImage(
            url = Res.drawable.valorant,
            contentDescription = "Valorant Logo",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            contentScale = ContentScale.Fit
        )

        Icon(
            painter = painterResource(Res.drawable.arrow),
            contentDescription = null,
            tint = Theme.colors.textPrimary,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp)
                .size(26.dp)
                .clickable { onAnimateClick() }
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AgentCard(
    agent: AgentsModel,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    onItemClick: (String) -> Unit
) {

    var dominantColor by remember { mutableStateOf(Color.Gray) }
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    LaunchedEffect(agent.fullPortrait){
        calculateDominantColor(source = agent.fullPortrait.orEmpty()) { color ->
            dominantColor = color
        }
    }
    Box(
        modifier = Modifier
            .padding(9.dp)
            .height(230.dp)
            .width(130.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .clip(
                    RoundedCornerShape(
                        topEnd = 140.dp,
                        topStart = 10.dp,
                        bottomStart = 40.dp
                    )
                )
                .border(
                    BorderStroke(2.dp, dominantColor),
                    RoundedCornerShape(topEnd = 140.dp, topStart = 10.dp, bottomStart = 40.dp)
                )
                .background(
                    Brush.verticalGradient(
                        listOf(
                            dominantColor,
                            dominantColor.copy(0.1f)
                        )
                    )
                )
                .clickable { onItemClick(agent.uuid) }
                .height(140.dp)
                .width(130.dp),
        )

        with(sharedTransitionScope) {
            SubcomposeAsyncImage(
                model = agent.fullPortrait,
                contentDescription = null,
                loading = {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(30.dp),
                            color = Theme.colors.textPrimary
                        )
                    }
                },
                onSuccess = {
                },
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(key = agent.uuid),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                    .height(190.dp)
                    .scale(scale)
                    .align(Alignment.BottomCenter)
            )
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp, bottomStart = 50.dp))
                .height(55.dp)
                .padding(horizontal = 5.dp)
                .width(130.dp)
                .align(Alignment.BottomCenter)
                .background(Theme.colors.surface.copy(alpha = 0.6f))
        )

        AgentInfo(
            agent = agent,
            modifier = Modifier
                .width(130.dp)
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp)
        )
    }
}

@Composable
fun AgentInfo(agent: AgentsModel, modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = agent.displayName ?: "",
            color = Theme.colors.headingText,
            fontFamily = FontFamily(Font(Res.font.dryme)),
            fontSize = 18.sp
        )
        Text(
            text = agent.role?.displayName ?: "",
            color = Theme.colors.textSecondary
        )
    }
}