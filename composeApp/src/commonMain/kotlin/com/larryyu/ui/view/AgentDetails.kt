package com.larryyu.ui.view

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.larryyu.domain.model.AgentDetailsData
import com.larryyu.presentation.uistates.AgentDetailsIntent
import com.larryyu.presentation.viewmodel.AgentDetailsViewModel
import com.larryyu.presentation.viewmodel.AgentsViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.Font
import org.koin.compose.koinInject
import valorantui.composeapp.generated.resources.Res
import valorantui.composeapp.generated.resources.dryme


@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun AgentDetailsScreen(
    agentId: String,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onBack: () -> Unit,
) {

    val viewModel: AgentDetailsViewModel = koinInject()
    LaunchedEffect(Unit) {
        viewModel.dispatch(AgentDetailsIntent.FetchAgentDetails(agentId))
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
                // 🔙 زر الرجوع في بداية الـ TopBar
//                Icon(
//                    imageVector = Icons,
//                    contentDescription = "Back",
//                    tint = Color.White,
//                    modifier = Modifier
//                        .align(Alignment.CenterStart)
//                        .padding(start = 16.dp)
//                        .clickable { onBack() } // ← هنا بنستدعي الـ callback
//                )

                Text(
                    text = status.agentDetails.displayName ?: "",
                    color = Color.White,
                    fontFamily = FontFamily(Font(Res.font.dryme)),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.Center).clickable {
                        onBack()
                    }
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
            AgentDetailsCard(
                agent = status.agentDetails,
                animatedVisibilityScope,
                sharedTransitionScope,
                sheetState
            ) {
                dominantColor = it
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun AgentDetailsCard(
    agent: AgentDetailsData,
    animatedContentScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    sheetState: SheetState,
    viewModel: AgentsViewModel = koinInject(),
    onDominant: (Color) -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    var dominantColor by remember { mutableStateOf(Color.Gray) }
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

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        SubcomposeAsyncImage(
            model = agent.background,
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
        with(sharedTransitionScope) {
            SubcomposeAsyncImage(
                model = agent.fullPortrait,
                contentDescription = null,
                onSuccess = { success ->
//                    viewModel.calcDomaintColor(
//                        drawable = success.result.image as BitmapImage
//                    ) { color ->
//                        dominantColor = color
//                        onDominant(dominantColor)
//                    }
                },
                loading = {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(30.dp),
                            color = Color.White
                        )
                    }
                },
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(width = 250.dp, height = 350.dp) // حجم مناسب للـ Details
                    .align(Alignment.TopCenter)          // يظهر من فوق بدل ما يختفي تحت
                   .sharedElement(
                        sharedContentState = rememberSharedContentState(key = agent.uuid ?: ""),
                        animatedVisibilityScope = animatedContentScope
                    )
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount.x
                            offsetY += dragAmount.y
                        }
                    }
                    .scale(scale)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
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
                        .height(300.dp)
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
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
                    .height(300.dp)
                    .align(Alignment.BottomCenter)
            )
        }

    }
}

@Composable
fun AgentInfoDetails(agent: AgentDetailsData, modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(5.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        Spacer(modifier = Modifier.height(10.dp))
//        Text(
//            text = agent.displayName ?: "",
//            color = Color.White,
//            fontFamily = FontFamily(Font(R.font.dryme)),
//            fontSize = 26.sp
//        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            SubcomposeAsyncImage(
                model = agent.role?.displayIcon,
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = agent.role?.displayName ?: "",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
        LazyRow {
            items(agent.abilities ?: emptyList()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(100.dp)
                ) {
                    SubcomposeAsyncImage(
                        model = it.displayIcon,
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )
                    Text(
                        text = it.displayName ?: "",
                        color = Color.White,
                        fontSize = 10.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = agent.description ?: "",
            color = Color.White,
            modifier = Modifier.padding(5.dp),
            fontSize = 12.sp
        )
    }
}
