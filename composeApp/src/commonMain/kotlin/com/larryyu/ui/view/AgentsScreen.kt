package com.larryyu.ui.view

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import com.larryyu.ui.components.ThemeWipeOverlay
import com.larryyu.ui.theme.Theme
import com.larryyu.ui.theme.ThemeViewModel
import com.larryyu.ui.theme.ValorantUIColors
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
fun AgentScreen(
    viewModel: AgentsViewModel = koinInject(),
    onDetailsClick: (String) -> Unit
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
                AgentsGridScreen(
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
        SubcomposeAsyncImage(
            model = Res.drawable.valorant,
            contentDescription = null,
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
fun AgentsGridScreen(
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

    Box(Modifier.fillMaxSize()) {



        Scaffold(
            containerColor = Theme.colors.background,
            topBar = {
                AgentsTopBar(
                    onAnimateClick = { startAnimation = true }
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
        // Overlay الانيميشن
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

            ThemeWipeOverlay(fraction = overlayFraction.value)
        }
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
                    val source = agent.fullPortrait.orEmpty()
                    val bytes = source.encodeToByteArray()
                    val sums = IntArray(3)
                    for (i in bytes.indices) {
                        sums[i % 3] = (sums[i % 3] + (bytes[i].toInt() and 0xFF)) % 256
                    }
                    val r = sums[0] / 255f
                    val g = sums[1] / 255f
                    val b = sums[2] / 255f
                    dominantColor = Color(r, g, b)
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