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
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.larryyu.valorantui.R
import com.larryyu.valorantui.domain.model.Data
import com.larryyu.valorantui.ui.intent.AgentDetailsIntent
import com.larryyu.valorantui.ui.viewmodel.AgentDetailsViewModel
import com.larryyu.valorantui.ui.viewmodel.AgentsViewModel
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun AgentDetailsScreen(
    agentId: String,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
) {

    val viewModel: AgentDetailsViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        viewModel.dispatch(AgentDetailsIntent.FetchAgentDetails(agentId), agentId)
    }
    var dominantColor by remember { mutableStateOf(Color.Gray) }
    val status by viewModel.state.collectAsState()
    val sheetState: SheetState = rememberModalBottomSheetState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Box(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = status.agentDetails.displayName ?: "",
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.dryme)),
                    modifier = Modifier.padding(top = 10.sdp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.ssp
                )
            }
        },
        containerColor = Color.Black.copy(0.9f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            AgentDetailsCard(agent = status.agentDetails, animatedContentScope, sharedTransitionScope, sheetState) {
                dominantColor = it
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun AgentDetailsCard(
    agent: Data,
    animatedContentScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
    sheetState: SheetState,
    viewModel: AgentsViewModel = hiltViewModel(),
    onDominant: (Color) -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    var dominantColor by remember { mutableStateOf(Color.Gray) }
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(agent.background)
                .size(Size.ORIGINAL)
                .build(),
            contentDescription = null,
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
                .height(400.dp)
                .padding(top = 50.dp)
                .align(Alignment.TopCenter)
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
                        onDominant(dominantColor)
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
                    .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                    .fillMaxSize()
                    .sharedElement(
                        state = rememberSharedContentState(key = agent.uuid ?: ""),
                        animatedVisibilityScope  = animatedContentScope
                    )
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount.x
                            offsetY += dragAmount.y
                        }
                    }
                    .padding(bottom = 400.dp)
                    .scale(scale)
                    .align(Alignment.BottomCenter)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.sdp)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Black.copy(alpha = 0.4f),
                            Color.Transparent
                        )
                    )
                )
        )

        val coroutineScope = rememberCoroutineScope()
        var visible by remember { mutableStateOf(true) }

        if (visible) {
            ModalBottomSheet(
                onDismissRequest = {
                    coroutineScope.launch {
                        sheetState.hide()
                        visible = false
                    }
                },
                containerColor = dominantColor.copy(alpha = 0.5f),
                sheetState = sheetState
            ) {
                AgentInfoDetails(
                    agent = agent, modifier = Modifier
                        .height(300.sdp)
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.sdp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 20.dp
                        )
                    )
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                dominantColor.copy(alpha = 0.9f),
                                dominantColor.copy(alpha = 0.2f)
                            )
                        )
                    )
                    .align(Alignment.BottomCenter)
            )

            AgentInfoDetails(
                agent = agent, modifier = Modifier
                    .height(300.sdp)
                    .align(Alignment.BottomCenter)
            )
        }

    }
}

@Composable
fun AgentInfoDetails(agent: Data, modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(5.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        Spacer(modifier = Modifier.height(10.sdp))
//        Text(
//            text = agent.displayName ?: "",
//            color = Color.White,
//            fontFamily = FontFamily(Font(R.font.dryme)),
//            fontSize = 26.ssp
//        )
        Spacer(modifier = Modifier.height(10.sdp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(agent.role?.displayIcon)
                    .build(),
                contentDescription = null,
                modifier = Modifier.size(30.sdp)
            )
            Spacer(modifier = Modifier.width(10.sdp))
            Text(
                text = agent.role?.displayName ?: "",
                color = Color.White,
                fontSize = 20.ssp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(30.sdp))
        LazyRow {
            items(agent.abilities ?: emptyList()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(100.dp)
                ) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(it.displayIcon)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )
                    Text(
                        text = it.displayName ?: "",
                        color = Color.White,
                        fontSize = 10.ssp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.sdp))
        Text(
            text = agent.description ?: "",
            color = Color.White,
            modifier = Modifier.padding(5.sdp),
            fontSize = 12.ssp
        )
    }
}
