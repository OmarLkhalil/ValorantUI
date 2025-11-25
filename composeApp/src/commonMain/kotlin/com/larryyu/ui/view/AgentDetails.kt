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
import com.larryyu.presentation.model.AbilityUiModel
import com.larryyu.presentation.model.AgentDetailsUiModel
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

    status.agentDetails?.let { agentDetails ->
        LaunchedEffect(agentDetails.agentImageUrl) {
            calculateDominantColor(
                source = agentDetails.agentImageUrl,
            ) { color ->
                dominantColor = color
            }
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
                    text = status.agentDetails?.agentName ?: "",
                    modifier = Modifier.clickable { onBack() },
                    contentDescription = "It's the Agent Name ${status.agentDetails?.agentName}"
                )
            }
        },
        containerColor = dominantColor.copy(alpha = 0.9f)
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(it)) {
            status.agentDetails?.let { agentDetails ->
                AgentDetailsCardComponent(
                    agent = agentDetails,
                    dominantColor = dominantColor,
                    animatedVisibilityScope = animatedVisibilityScope,
                    sharedTransitionScope = sharedTransitionScope
                )
            }
        }
    }
}
@Composable
fun AgentInfoDetailsComponent(
    agent: AgentDetailsUiModel,
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
                text = agent.agentDescription,
                contentDescription = "Agent Description for ${agent.agentName}, it's Description is ${agent.agentDescription}"
            )
        }
    }
}
@Composable
fun AbilityItem(ability: AbilityUiModel) {
    Column(
        horizontalAlignment = CenterHorizontally,
        modifier = Modifier.width(100.dp)
    ) {
        CoilImage(
            url = ability.abilityIconUrl,
            contentDescription = "It's the ability Icon for ${ability.abilityName}",
            modifier = Modifier.size(50.dp)
        )
        HeaderText(
            text = ability.abilityName,
            contentDescription = "It's the ability Name for ${ability.abilityName}",
            modifier = Modifier,
            textStyle = Theme.typography.body12
        )
    }
}
@Composable
fun AgentRoleRow(agent: AgentDetailsUiModel) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        CoilImage(
            url = agent.agentRole.roleIconUrl,
            contentDescription = "It's the Agent Role Icon for ${agent.agentRole.roleName}",
            modifier = Modifier.size(30.dp),
        )
        Spacer(modifier = Modifier.width(10.dp))
        DescriptionText(
            text = agent.agentRole.roleName,
            contentDescription = "It's the Agent Role Name for ${agent.agentRole.roleName}",
            textStyle = Theme.typography.body16,
            color = Theme.colors.textPrimary
        )
    }
}
@Composable
fun AgentAbilitiesRow(agent: AgentDetailsUiModel) {
    LazyRow {
        items(agent.agentAbilities) { ability ->
            AbilityItem(ability)
        }
    }
}
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AgentDetailsCardComponent(
    agent: AgentDetailsUiModel,
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
        with(sharedTransitionScope) {
            CoilImage(
                url = agent.agentImageUrl,
                contentDescription = "It's the Agent Portrait for ${agent.agentName}",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(width = 250.dp, height = 350.dp)
                    .align(Alignment.TopCenter)
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(key = agent.agentId),
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
