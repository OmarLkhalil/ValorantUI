package com.larryyu.valorantui.ui.view

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.larryyu.valorantui.R
import com.larryyu.valorantui.domain.model.AgentItem
import com.larryyu.valorantui.ui.viewmodel.AgentsViewModel
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AgentScreen(
    agents: List<AgentItem>,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope, onItemClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            SubcomposeAsyncImage(
                model = R.drawable.valorant,
                contentDescription = "",
                modifier = Modifier
                    .padding(top = 20.sdp, bottom = 3.sdp)
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .height(60.dp),
                contentScale = ContentScale.Fit
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(35.sdp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .width(80.sdp)
                        .background(Color.White)
                        .height(2.sdp)
                )
                Text(
                    text = "Agents",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.sdp),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.dryme)),
                    fontSize = 18.ssp
                )
            }
        },
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black.copy(0.9f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                items(agents.reversed()) { agent ->
                        AgentCard(agent = agent, animatedContentScope, sharedTransitionScope, onItemClick = onItemClick)

                }
            }
        }
    }
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AgentCard(
    agent: AgentItem,
    animatedContentScope : AnimatedContentScope ,
    sharedTransitionScope: SharedTransitionScope ,
    viewModel: AgentsViewModel = hiltViewModel(),
    onItemClick: (String) -> Unit
) {

    var dominantColor by remember { mutableStateOf(Color.Gray) }
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
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
            .height(190.sdp)
            .width(130.sdp)
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
                .clickable {
                    onItemClick(agent.uuid ?: "")
                }
                .height(140.sdp)
                .width(130.sdp),
        )
        with(sharedTransitionScope){
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(agent.fullPortrait)
                    .size(Size.ORIGINAL)
                    .build(),
                contentDescription = null,
                onSuccess = { success ->
                    viewModel.calcDomaintColor(success.result.drawable) { color ->
                        dominantColor = color
                    }
                },
                loading = {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(30.dp),
                            color = Color.White
                        )
                    }
                },
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .sharedElement(
                        state = rememberSharedContentState(key = agent.uuid ?: ""),
                        animatedVisibilityScope  = animatedContentScope
                    )
                    .height(190.sdp)
                    .scale(scale)
                    .align(Alignment.BottomCenter)
            )
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp, bottomStart = 50.dp))
                .height(55.sdp)
                .padding(horizontal = 5.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(dominantColor.copy(alpha = 0.2f))
        )
        AgentInfo(
            agent = agent,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.sdp)
        )
    }

}

@Composable
fun AgentInfo(agent: AgentItem, modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = agent.displayName ?: "",
            color = Color.White,
            fontFamily = FontFamily(Font(R.font.dryme)),
            fontSize = 18.ssp
        )
        Text(text = agent.role?.displayName ?: "", color = Color.White)
    }
}



