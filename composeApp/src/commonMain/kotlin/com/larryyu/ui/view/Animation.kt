//package com.larryyu.ui.view
//
//import android.graphics.RuntimeShader
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.compose.animation.core.Animatable
//import androidx.compose.animation.core.FastOutSlowInEasing
//import androidx.compose.animation.core.tween
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.BoxScope
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.asComposeRenderEffect
//import androidx.compose.ui.graphics.graphicsLayer
//import androidx.compose.ui.layout.onSizeChanged
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.IntSize
//import androidx.compose.ui.unit.dp
//import kotlinx.coroutines.delay
//import android.graphics.RenderEffect as FrameworkRenderEffect
//
//@Composable
//fun WhirlpoolMorphScreen(
//    modifier: Modifier = Modifier,
//    // Time (ms) for each direction of the ping–pong
//    durationMillis: Int = 1800,
//    // Optional delay before the first run
//    startDelayMillis: Int = 0,
//    // Pause when fully un-morphed (progress = 0)
//    restPauseMillis: Int = 1200,
//    // Pause at the peak whirlpool (progress = 1)
//    peakPauseMillis: Int = 0,
//    // Max twist in radians at the very center (2π is a full rotation)
//    maxTwistRadians: Float = (Math.PI * 2.0).toFloat(),
//    // How much the content sinks towards the center at peak (0..1)
//    sinkAmount: Float = 0.25f,
//) {
//    val progress = remember { Animatable(0f) }
//    LaunchedEffect(durationMillis, startDelayMillis, restPauseMillis, peakPauseMillis) {
//        progress.snapTo(0f)
//        if (startDelayMillis > 0) delay(startDelayMillis.toLong())
//        while (true) {
//            progress.animateTo(
//                targetValue = 1f,
//                animationSpec = tween(durationMillis, easing = FastOutSlowInEasing)
//            )
//            if (peakPauseMillis > 0) delay(peakPauseMillis.toLong())
//
//            progress.animateTo(
//                targetValue = 0f,
//                animationSpec = tween(durationMillis, easing = FastOutSlowInEasing)
//            )
//            if (restPauseMillis > 0) delay(restPauseMillis.toLong())
//        }
//    }
//
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//        WhirlpoolRuntimeShaderContainer(
//            modifier = modifier.fillMaxSize(),
//            progress = progress.value,
//            maxTwistRadians = maxTwistRadians,
//            sinkAmount = sinkAmount
//        ) {
//            DemoBackdrop()
//        }
//    } else {
//        Box(modifier.fillMaxSize()) { DemoBackdrop() }
//    }
//}
//
//@Composable
//private fun DemoBackdrop() {
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(
//                Brush.linearGradient(
//                    listOf(
//                        Color(0xFF647FBC),
//                        Color(0xFF91ADC8),
//                        Color(0xFFAED6CF),
//                        Color(0xFFFAFDD6)
//                    )
//                )
//            )
//            .padding(24.dp)
//    ) {
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            Text(
//                text = "I looooovvvvvvvveeeeeeeeee Yooooooooooouuuu",
//                style = MaterialTheme.typography.headlineLarge.copy(
//                    color = Color.White,
//                    fontWeight = FontWeight.ExtraBold
//                )
//            )
//            Spacer(Modifier.height(12.dp))
//            Text(
//                text = "Love You Mennety",
//                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White.copy(alpha = 0.9f))
//            )
//        }
//    }
//}
//
//@RequiresApi(Build.VERSION_CODES.TIRAMISU)
//@Composable
//private fun WhirlpoolRuntimeShaderContainer(
//    modifier: Modifier = Modifier,
//    progress: Float,
//    maxTwistRadians: Float,
//    sinkAmount: Float,
//    content: @Composable BoxScope.() -> Unit
//) {
//    var size by remember { mutableStateOf(IntSize.Zero) }
//    val shader = remember { RuntimeShader(WHIRLPOOL_AGSL) }
//
//    val renderEffect = remember(size, progress, maxTwistRadians, sinkAmount) {
//        if (size.width > 0 && size.height > 0) {
//            shader.setFloatUniform("resolution", size.width.toFloat(), size.height.toFloat())
//        } else {
//            shader.setFloatUniform("resolution", 1f, 1f)
//        }
//        shader.setFloatUniform("progress", progress)
//        shader.setFloatUniform("maxTwist", maxTwistRadians)
//        shader.setFloatUniform("sink", sinkAmount)
//        FrameworkRenderEffect.createRuntimeShaderEffect(shader, "inputImage")
//            .asComposeRenderEffect()
//    }
//
//    Box(
//        modifier = modifier
//            .onSizeChanged { size = it }
//            .graphicsLayer {
//                compositingStrategy = androidx.compose.ui.graphics.CompositingStrategy.Offscreen
//                this.renderEffect = renderEffect
//            },
//        content = content
//    )
//}
//
//private const val WHIRLPOOL_AGSL = """
//uniform shader inputImage;
//uniform float2 resolution;
//uniform float progress;     // 0..1
//uniform float maxTwist;     // radians
//uniform float sink;         // 0..1
//half4 main(float2 fragCoord) {
//    // Normalized coordinates
//    float2 uv = fragCoord / resolution;
//    float2 center = float2(0.5, 0.5);
//    float2 delta = uv - center;
//    float r = length(delta);
//    // Stronger twist near the center, smoothly fading to the edges.
//    float falloff = 1.0 - smoothstep(0.0, 1.0, r);
//    float angle = maxTwist * progress * falloff;
//    float s = sin(angle);
//    float c = cos(angle);
//    // Apply rotation (swirl)
//    float2 rotated = float2(
//        delta.x * c - delta.y * s,
//        delta.x * s + delta.y * c
//    );
//    // Apply inward sink based on distance and progress
//    float k = 1.0 - (sink * progress * falloff);
//    rotated *= k;
//    float2 uv2 = center + rotated;
//    // Clamp to avoid sampling outside content
//    uv2 = clamp(uv2, float2(0.0), float2(1.0));
//    return inputImage.eval(uv2 * resolution);
//}
//"""
//
//
//@Preview
//@Composable
//fun PreviewAnimation(){
//    WhirlpoolMorphScreen()
//}
//
//
