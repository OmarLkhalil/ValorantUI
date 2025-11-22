package com.larryyu.ui.theme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.dp
@Immutable
data class ValorantUIShapes(
    val rounded8: RoundedCornerShape = RoundedCornerShape(8.dp),
    val rounded10: RoundedCornerShape = RoundedCornerShape(10.dp),
    val rounded12: RoundedCornerShape = RoundedCornerShape(12.dp),
    val rounded16: RoundedCornerShape = RoundedCornerShape(16.dp),
    val rounded20: RoundedCornerShape = RoundedCornerShape(20.dp),
    val rounded25: RoundedCornerShape = RoundedCornerShape(25.dp),
)
