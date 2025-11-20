package com.larryyu.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.larryyu.domain.model.AgentDetailsData
import com.larryyu.ui.theme.Theme

@Composable
fun DescriptionText(
    text: String,
    modifier: Modifier = Modifier,
    contentDescription: String,
    maxLines: Int = Int.MAX_VALUE,
    textStyle: TextStyle = Theme.typography.body12,
    color : Color = Theme.colors.textSecondary,
) {
    Text(
        text = text,
        style = textStyle,
        maxLines = maxLines,
        color =color,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier.padding(5.dp).semantics {
            this.contentDescription = contentDescription
        },
    )
}
