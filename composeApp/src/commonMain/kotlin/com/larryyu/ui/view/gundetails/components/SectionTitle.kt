package com.larryyu.ui.view.gundetails.components
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import com.larryyu.ui.theme.Theme
@Composable
fun SectionTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = Theme.colors.textPrimary,
        style = Theme.typography.headline20,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .padding(vertical = 8.dp)
            .semantics { heading() }
    )
}
