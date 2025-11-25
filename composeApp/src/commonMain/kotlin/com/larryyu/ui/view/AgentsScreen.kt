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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.larryyu.presentation.model.AgentUiModel
import com.larryyu.presentation.uistates.AgentsIntent
import com.larryyu.presentation.uistates.AgentsUIState
import com.larryyu.presentation.viewmodel.AgentsViewModel
import com.larryyu.ui.components.DescriptionText
import com.larryyu.ui.components.calculateDominantColor
import com.larryyu.ui.components.clipToFraction
import com.larryyu.ui.theme.Theme
import com.larryyu.ui.theme.ThemeViewModel
import com.larryyu.ui.theme.ValorantUITheme
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import valorantui.composeapp.generated.resources.Res
import valorantui.composeapp.generated.resources.arrow
import valorantui.composeapp.generated.resources.valorant

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AgentsScreen(
    viewModel: AgentsViewModel = koinInject(),
) {
    val agentsState by viewModel.agentsState.collectAsState()
    var selectedAgent by remember { mutableStateOf<AgentUiModel?>(null) }

    LaunchedEffect(Unit) {
        viewModel.sendIntent(AgentsIntent.FetchAgents)
    }

    SharedTransitionLayout {
        AnimatedContent(
            targetState = selectedAgent,
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            },
            label = "agentsTransition"
        ) { targetAgent ->
            if (targetAgent == null) {
                AgentsGridRoute(
                    agentsState = agentsState,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this,
                    onAgentClick = { selectedAgent = it }
                )
            } else {
                AgentDetailsScreen(
                    agentId = targetAgent.agentId,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this,
                    onBack = { selectedAgent = null }
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
    onAgentClick: (AgentUiModel) -> Unit
) {
    val isDarkTheme by viewModel.isDarkTheme.collectAsState()
    val fraction = remember { Animatable(if (isDarkTheme) 1f else 0f) }
    val scope = rememberCoroutineScope()
    var isAnimating by remember { mutableStateOf(false) }

    LaunchedEffect(isDarkTheme) {
        if (!isAnimating) {
            fraction.snapTo(if (isDarkTheme) 1f else 0f)
        }
    }

    AgentsGridContent(
        agentsState = agentsState,
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope,
        isDarkTheme = isDarkTheme,
        animationFraction = fraction.value,
        isAnimating = isAnimating,
        onAgentClick = onAgentClick,
        onAnimateClick = {
            if (!isAnimating) {
                scope.launch {
                    isAnimating = true
                    val start = fraction.value
                    val target = if (isDarkTheme) 0f else 1f
                    fraction.animateTo(
                        targetValue = target,
                        animationSpec = keyframes {
                            durationMillis = 2000
                            (start + (target - start) * 0.30f) at 400 using CubicBezierEasing(
                                0.42f,
                                0.0f,
                                0.58f,
                                1.0f
                            )
                            (start + (target - start) * 0.70f) at 1200 using CubicBezierEasing(
                                0.42f,
                                0.0f,
                                0.58f,
                                1.0f
                            )
                            (start + (target - start) * 0.95f) at 1700 using CubicBezierEasing(
                                0.42f,
                                0.0f,
                                0.58f,
                                1.0f
                            )
                            target at 2000 using FastOutSlowInEasing
                        }
                    )
                    viewModel.toggleTheme()
                    isAnimating = false
                }
            }
        }
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AgentsGridContent(
    agentsState: AgentsUIState,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    isDarkTheme: Boolean,
    animationFraction: Float,
    isAnimating: Boolean,
    onAgentClick: (AgentUiModel) -> Unit,
    onAnimateClick: () -> Unit
) {
    if (isAnimating) {
        Box(Modifier.fillMaxSize()) {
            ValorantUITheme(isDark = false) {
                AgentsGridScaffold(
                    agentsState = agentsState,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    onAgentClick = onAgentClick,
                    onAnimateClick = onAnimateClick
                )
            }
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clipToFraction(animationFraction)
            ) {
                ValorantUITheme(isDark = true) {
                    AgentsGridScaffold(
                        agentsState = agentsState,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope,
                        onAgentClick = onAgentClick,
                        onAnimateClick = onAnimateClick
                    )
                }
            }
        }
    } else {
        ValorantUITheme(isDark = isDarkTheme) {
            AgentsGridScaffold(
                agentsState = agentsState,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
                onAgentClick = onAgentClick,
                onAnimateClick = onAnimateClick
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun AgentsGridScaffold(
    agentsState: AgentsUIState,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onAgentClick: (AgentUiModel) -> Unit,
    onAnimateClick: () -> Unit
) {
    Scaffold(
        containerColor = Theme.colors.background,
        topBar = {
            AgentsTopBar(onAnimateClick = onAnimateClick)
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
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(agentsState.agents, key = { it.agentId }) { agent ->
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
        Image(
            painter = painterResource(Res.drawable.valorant),
            contentDescription = "Valorant Logo",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            contentScale = ContentScale.Fit
        )
        Icon(
            painter = painterResource(Res.drawable.arrow),
            contentDescription = "Change Theme Icon",
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
    agent: AgentUiModel,
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
    LaunchedEffect(agent.agentImageUrl) {
        calculateDominantColor(source = agent.agentImageUrl) { color ->
            dominantColor = color
        }
    }
    Box(
        modifier = Modifier
            .padding(4.dp)
            .height(160.dp)
            .width(130.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .clip(
                    RoundedCornerShape(
                        topEnd = 120.dp,
                        topStart = 10.dp,
                        bottomStart = 40.dp
                    )
                )
                .border(
                    BorderStroke(3.dp, dominantColor),
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
                .clickable { onItemClick(agent.agentId) }
                .height(120.dp)
                .width(130.dp),
        )
        with(sharedTransitionScope) {
            SubcomposeAsyncImage(
                model = agent.agentImageUrl,
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
                        sharedContentState = rememberSharedContentState(key = agent.agentId),
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
fun AgentInfo(agent: AgentUiModel, modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DescriptionText(
            text = agent.agentName,
            contentDescription = "It's an Agent Name ${agent.agentName}",
            color = Theme.colors.textPrimary
        )
        DescriptionText(
            text = agent.agentRole,
            contentDescription = "It's an Agent Description ${agent.agentRole}"
        )
    }
}
