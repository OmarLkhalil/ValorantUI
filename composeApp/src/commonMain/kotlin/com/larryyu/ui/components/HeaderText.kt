package com.larryyu.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.larryyu.ui.theme.Theme
import org.jetbrains.compose.resources.Font
import valorantui.composeapp.generated.resources.Res
import valorantui.composeapp.generated.resources.dryme

@Composable
fun HeaderText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
    contentDescription: String
) {
    Text(
        text = text,
        color = Theme.colors.textPrimary,
        fontFamily = FontFamily(Font(Res.font.dryme)),
        fontWeight = FontWeight.Bold,
        overflow = TextOverflow.Ellipsis,
        fontSize = 24.sp,
        maxLines = maxLines,
        modifier = modifier.semantics {
            this.contentDescription = contentDescription
        }
    )
}
