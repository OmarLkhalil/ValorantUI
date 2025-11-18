package com.larryyu.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import com.larryyu.ui.theme.Theme

@Composable
fun HeaderText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
    contentDescription: String,
    textStyle: TextStyle = Theme.typography.headline20
) {
    Text(
        text = text,
        overflow = TextOverflow.Ellipsis,
        style = textStyle,
        maxLines = maxLines,
        modifier = modifier.semantics {
            this.contentDescription = contentDescription
        }
    )
}
