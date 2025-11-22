package com.larryyu.ui.view
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.larryyu.domain.model.AbilitiesItemDetails
import com.larryyu.domain.model.AgentDetailsData
import com.larryyu.presentation.uistates.AgentDetailsIntent
import com.larryyu.presentation.viewmodel.AgentDetailsViewModel
import com.larryyu.ui.components.BackHandler
import com.larryyu.ui.components.CoilImage
import com.larryyu.ui.components.DescriptionText
import com.larryyu.ui.components.HeaderText
import com.larryyu.ui.components.calculateDominantColor
import com.larryyu.ui.theme.Theme
import org.koin.compose.koinInject
@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun AgentDetailsScreen(
    agentId: String,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onBack: () -> Unit,
) {
    val viewModel: AgentDetailsViewModel = koinInject()
    BackHandler(onBack = onBack)
    LaunchedEffect(Unit) {
        viewModel.sendIntent(AgentDetailsIntent.FetchAgentDetails(agentId))
    }
    val surfaceColor = Theme.colors.surface
    val status by viewModel.state.collectAsState()
    var dominantColor by remember { mutableStateOf(surfaceColor) }
    LaunchedEffect(status.agentDetails.fullPortrait) {
        calculateDominantColor(
            source = status.agentDetails.fullPortrait.orEmpty(),
        ) { color ->
            dominantColor = color
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize().background(dominantColor),
        topBar = {
            Box(
                modifier = Modifier.height(60.dp).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                HeaderText(
                    text = status.agentDetails.displayName ?: "",
                    modifier = Modifier.clickable { onBack() },
                    contentDescription = "It's the Agent Name ${status.agentDetails.displayName}"
                )
            }
        },
        containerColor = dominantColor.copy(alpha = 0.9f)
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(it)) {
            AgentDetailsCardComponent(
                agent = status.agentDetails,
                dominantColor = dominantColor,
                animatedVisibilityScope = animatedVisibilityScope,
                sharedTransitionScope = sharedTransitionScope
            )
        }
    }
}
@Composable
fun AgentInfoDetailsComponent(
    agent: AgentDetailsData,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(Theme.dimens.space16),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Theme.dimens.space16)
    ) {
        item {
            AgentRoleRow(agent = agent)
        }
        item {
            AgentAbilitiesRow(agent = agent)
        }
        item {
            DescriptionText(
                text = agent.description.orEmpty(),
                contentDescription = "Agent Description for ${agent.displayName}, it's Description is ${agent.description}"
            )
        }
    }
}
@Composable
fun AbilityItem(ability: AbilitiesItemDetails) {
    Column(
        horizontalAlignment = CenterHorizontally,
        modifier = Modifier.width(100.dp)
    ) {
        CoilImage(
            url = ability.displayIcon,
            contentDescription = "It's the ability Icon for ${ability.displayName}",
            modifier = Modifier.size(50.dp)
        )
        HeaderText(
            text = ability.displayName ?: "",
            contentDescription = "It's the ability Name for ${ability.displayName}",
            modifier = Modifier,
            textStyle = Theme.typography.body12
        )
    }
}
@Composable
fun AgentRoleRow(agent: AgentDetailsData) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        CoilImage(
            url = agent.role?.displayIcon,
            contentDescription = "It's the Agent Role Icon for ${agent.role?.displayName}",
            modifier = Modifier.size(30.dp),
        )
        Spacer(modifier = Modifier.width(10.dp))
        DescriptionText(
            text = agent.role?.displayName ?: "",
            contentDescription = "It's the Agent Role Name for ${agent.role?.displayName}",
            textStyle = Theme.typography.body16,
            color = Theme.colors.textPrimary
        )
    }
}
@Composable
fun AgentAbilitiesRow(agent: AgentDetailsData) {
    LazyRow {
        items(agent.abilities ?: emptyList()) {
            AbilityItem(it)
        }
    }
}
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AgentDetailsCardComponent(
    agent: AgentDetailsData,
    dominantColor: Color,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val scale by infiniteTransition.animateFloat(
        initialValue = 2f,
        targetValue = 2.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    Box(modifier = Modifier.fillMaxSize()) {
        CoilImage(
            url = agent.background,
            contentDescription = "It's the Agent Background for ${agent.displayName}",
            modifier = Modifier
                .height(400.dp)
                .padding(top = 50.dp)
                .align(Alignment.TopCenter)
        )
        with(sharedTransitionScope) {
            CoilImage(
                url = agent.fullPortrait,
                contentDescription = "It's the Agent Portrait for ${agent.displayName}",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(width = 250.dp, height = 350.dp)
                    .align(Alignment.TopCenter)
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(key = agent.uuid ?: ""),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                    .scale(scale)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            dominantColor.copy(alpha = 0.9f),
                            Color.White
                        )
                    )
                )
                .align(Alignment.BottomCenter)
        )
        AgentInfoDetailsComponent(
            agent = agent,
            modifier = Modifier.align(Alignment.BottomCenter).height(300.dp)
        )
    }
}
