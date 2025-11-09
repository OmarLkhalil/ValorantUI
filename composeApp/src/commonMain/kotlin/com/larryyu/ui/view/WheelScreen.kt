//package com.larryyu.ui.view
//
//import com.larryyu.R
//import android.graphics.Paint
//import androidx.compose.animation.core.EaseOutCubic
//import androidx.compose.animation.core.animateFloatAsState
//import androidx.compose.animation.core.tween
//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.offset
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.wrapContentSize
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.LocationOn
//import androidx.compose.material3.AlertDialog
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableFloatStateOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.rotate
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.drawscope.Fill
//import androidx.compose.ui.graphics.drawscope.Stroke
//import androidx.compose.ui.graphics.nativeCanvas
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.window.Dialog
//import androidx.compose.ui.zIndex
//import com.airbnb.lottie.compose.LottieAnimation
//import com.airbnb.lottie.compose.LottieCompositionSpec
//import com.airbnb.lottie.compose.animateLottieCompositionAsState
//import com.airbnb.lottie.compose.rememberLottieComposition
//import kotlinx.coroutines.delay
//import kotlin.math.cos
//import kotlin.math.sin
//import kotlin.random.Random
//
//@Composable
//fun WheelScreen(modifier: Modifier = Modifier) {
//    var isSpinning by remember { mutableStateOf(false) }
//    var targetRotation by remember { mutableFloatStateOf(0f) }
//    var showWinDialog by remember { mutableStateOf(false) }
//    var selectedPrize by remember { mutableStateOf("") }
//
//    val prizes = listOf("M", "W", "M", "W", "M", "W", "M", "W")
//
//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .background(Color.White),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Box(
//            modifier = Modifier
//                .wrapContentSize()
//                .background(Color.White),
//            contentAlignment = Alignment.Center
//        ) {
//            SpinningWheel(
//                isSpinning = isSpinning,
//                targetRotation = targetRotation,
//                onSpinComplete = { selectedSegment ->
//                    isSpinning = false
//                    selectedPrize = prizes[selectedSegment]
//                    showWinDialog = true
//                }
//            )
//            WheelMarker()
//            WheelCenterLogo()
//        }
//        SpinWheelButton(
//            onClick = {
//                if (!isSpinning) {
//                    isSpinning = true
//                    val segmentCount = prizes.size
//                    val anglePerSegment = 360f / segmentCount
//                    val randomSegment = Random.nextInt(segmentCount)
//                    val startOffset = -anglePerSegment / 2f
//                    targetRotation += 360f * 5f + randomSegment * anglePerSegment + startOffset
//                }
//            },
//            isSpinning = isSpinning
//        )
//    }
//
//    if (showWinDialog) {
//        WinDialog(
//            prizeText = selectedPrize,
//            onDismiss = { showWinDialog = false }
//        )
//    }
//}
//
//@Composable
//fun WinDialog(
//    prizeText: String,
//    onDismiss: () -> Unit
//) {
//    Dialog(onDismissRequest = { onDismiss() }) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(24.dp)
//                .clip(RoundedCornerShape(20.dp))
//                .background(Color.White),
//            contentAlignment = Alignment.Center
//        ) {
//            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_paper_scraps))
//            val progress by animateLottieCompositionAsState(composition)
//            LottieAnimation(
//                composition = composition,
//                progress = { progress },
//                modifier = Modifier.size(200.dp)
//            )
//            Column(
//                modifier = Modifier
//                    .padding(16.dp)
//                    .fillMaxWidth(),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Text(
//                    text = prizeText,
//                    style = MaterialTheme.typography.headlineSmall.copy(
//                        fontWeight = FontWeight.Bold,
//                        color = Color(0xFFFB8500)
//                    ),
//                    textAlign = TextAlign.Center
//                )
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                Button(
//                    onClick = onDismiss,
//                    shape = RoundedCornerShape(12.dp),
//                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
//                ) {
//                    Text("ok", color = Color.White)
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun SpinningWheel(
//    isSpinning: Boolean,
//    targetRotation: Float,
//    onSpinComplete: (Int) -> Unit
//) {
//    val segmentCount = 8
//    val anglePerSegment = 360f / segmentCount
//    val colors = listOf(Color.Blue, Color(0xFFFF9800), Color.White)
//
//    val rotation by animateFloatAsState(
//        targetValue = targetRotation,
//        animationSpec = tween(
//            durationMillis = 3000,
//            easing = EaseOutCubic
//        ),
//        finishedListener = {
//            val finalAngle = targetRotation % 360f
//            val adjustedAngle = (finalAngle + anglePerSegment / 2f) % 360f
//            val selectedSegment = (adjustedAngle / anglePerSegment).toInt()
//
//            println("🎯 Wheel stopped at index: $selectedSegment")
//
//            onSpinComplete(selectedSegment)
//        }
//    )
//
//
//    Canvas(
//        modifier = Modifier
//            .size(300.dp)
//            .rotate(rotation) // Apply rotation to the wheel
//    ) {
//        repeat(segmentCount) { index ->
//            val startAngle = index * anglePerSegment
//            drawArc(
//                color = colors[index % colors.size],
//                startAngle = startAngle,
//                sweepAngle = anglePerSegment,
//                useCenter = true,
//                style = Fill
//            )
//
//            val angleRad = Math.toRadians((startAngle + anglePerSegment / 2).toDouble())
//            val radius = size.minDimension / 2.5f
//            val x = center.x + radius * cos(angleRad).toFloat()
//            val y = center.y + radius * sin(angleRad).toFloat()
//
//            drawContext.canvas.nativeCanvas.apply {
//                drawText(
//                    if (index % 2 == 0) "W" else "M",
//                    x,
//                    y,
//                    Paint().apply {
//                        color = android.graphics.Color.BLACK
//                        textSize = 40f
//                        textAlign = Paint.Align.CENTER
//                    }
//                )
//            }
//        }
//
//        drawCircle(
//            color = Color.Yellow,
//            radius = size.minDimension / 2,
//            style = Stroke(width = 8f)
//        )
//
//        repeat(segmentCount) { index ->
//            val angleRad = Math.toRadians((index * anglePerSegment).toDouble())
//            val dotRadius = size.minDimension / 2
//            val x = center.x + dotRadius * cos(angleRad).toFloat()
//            val y = center.y + dotRadius * sin(angleRad).toFloat()
//
//            drawCircle(
//                color = Color.Yellow,
//                radius = 4.dp.toPx(),
//                center = Offset(x, y)
//            )
//        }
//    }
//
//    // Trigger the spin completion logic after the animation duration
//    LaunchedEffect(isSpinning) {
//
//        if (isSpinning) {
//            delay(3000L) // Match animation duration
//            // The actual segment selection is handled in the animation's finishedListener
//        }
//    }
//}
//
//@Composable
//fun WheelMarker() {
//    Box(
//        modifier = Modifier
//            .size(60.dp)
//            .offset(y = (-155).dp, x = 6.dp),
//    ) {
//        Icon(
//            imageVector = Icons.Default.LocationOn,
//            contentDescription = "Wheel Marker",
//            modifier = Modifier.size(50.dp),
//            tint = Color.Red
//        )
//    }
//}
//
//@Composable
//fun WheelCenterLogo() {
//    Box(
//        modifier = Modifier
//            .size(80.dp)
//            .background(Color(0xFF0D1B4C), shape = CircleShape)
//            .border(2.dp, Color.Black, CircleShape),
//        contentAlignment = Alignment.Center
//    ) {
//        Image(
//            painter = painterResource(id = android.R.drawable.ic_dialog_email),
//            contentDescription = "Center Logo",
//            modifier = Modifier.size(50.dp)
//        )
//    }
//}
//
//@Composable
//fun SpinWheelButton(
//    onClick: () -> Unit,
//    isSpinning: Boolean,
//    modifier: Modifier = Modifier
//) {
//    Button(
//        onClick = onClick,
//        modifier = modifier.padding(16.dp),
//        enabled = !isSpinning
//    ) {
//        Text("Spin Wheel")
//    }
//}
//
//@Preview
//@Composable
//fun PreviewWheelScreen() {
//    WheelScreen()
//}