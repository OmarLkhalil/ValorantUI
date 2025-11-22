package com.larryyu.ui.view.gundetails.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.larryyu.ui.components.CoilImage
import com.larryyu.ui.theme.Theme
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun RotatableGunImage(
    imageUrl: String?,
    gunUuid: String?,
    gunName: String?,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier
) {
    val rotation = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    var isAutoRotating by remember { mutableStateOf(true) }
    LaunchedEffect(isAutoRotating) {
        while (isAutoRotating) {
            rotation.animateTo(
                targetValue = rotation.value + 360f,
                animationSpec = tween(durationMillis = 10000, easing = LinearEasing)
            )
            rotation.snapTo(0f)
        }
    }
    LaunchedEffect(imageUrl) {
        rotation.snapTo(0f)
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(350.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Theme.colors.surface.copy(alpha = 0.6f))
            .border(
                width = 1.dp,
                color = Theme.colors.textSecondary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(20.dp)
            )
            .semantics {
                contentDescription = "${gunName ?: "Weapon"} image, ${if (isAutoRotating) "auto-rotating" else "manual rotation"}, drag to rotate manually"
                customActions = listOf(
                    CustomAccessibilityAction(
                        label = if (isAutoRotating) "Pause auto-rotation" else "Resume auto-rotation"
                    ) {
                        isAutoRotating = !isAutoRotating
                        true
                    },
                    CustomAccessibilityAction(
                        label = "Rotate left"
                    ) {
                        scope.launch {
                            rotation.snapTo((rotation.value - 45f) % 360f)
                        }
                        true
                    },
                    CustomAccessibilityAction(
                        label = "Rotate right"
                    ) {
                        scope.launch {
                            rotation.snapTo((rotation.value + 45f) % 360f)
                        }
                        true
                    }
                )
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Theme.colors.highlight.copy(alpha = 0.3f),
                            Color.Transparent
                        ),
                        radius = 500f
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            with(sharedTransitionScope) {
                CoilImage(
                    url = imageUrl,
                    contentDescription = gunName ?: "Gun",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .sharedElement(
                            rememberSharedContentState(key = "gun-image-${gunUuid}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                        .fillMaxWidth(0.95f)
                        .fillMaxHeight(0.75f)
                        .rotate(rotation.value)
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = { isAutoRotating = false },
                                onDragEnd = { isAutoRotating = true },
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    scope.launch {
                                        val rotationChange = dragAmount.x / 3f
                                        rotation.snapTo((rotation.value + rotationChange) % 360f)
                                    }
                                }
                            )
                        }
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Theme.colors.surface.copy(alpha = 0.85f))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "👆",
                        style = Theme.typography.body12
                    )
                    Text(
                        text = if (isAutoRotating) "Drag to control • Auto-rotating" else "Drag to rotate • Manual",
                        color = Theme.colors.textPrimary,
                        style = Theme.typography.body8,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
