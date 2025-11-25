package com.larryyu.ui.view.gundetails.components
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.larryyu.presentation.model.ChromaUiModel
import com.larryyu.ui.components.CoilImage
import com.larryyu.ui.theme.Theme

@Composable
fun SkinsRow(
    chromas: List<ChromaUiModel>,
    dominantColor: Color,
    onSkinClick: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = "Available weapon skins section, ${chromas.size} skin${if (chromas.size > 1) "s" else ""} available, swipe to browse"
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Theme.colors.surface.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .semantics {
                        heading()
                        contentDescription = "Skins section header, ${chromas.size} skin${if (chromas.size > 1) "s" else ""} available"
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🎨 ${chromas.size} Skin${if (chromas.size > 1) "s" else ""} Available",
                    color = Theme.colors.textPrimary,
                    style = Theme.typography.body12,
                    fontWeight = FontWeight.Bold
                )
            }
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(chromas) { chroma ->
                    SkinCard(
                        chroma = chroma,
                        dominantColor = dominantColor,
                        onClick = {
                            val imageUrl = chroma.chromaFullRender ?: chroma.chromaImageUrl
                            onSkinClick(imageUrl)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SkinCard(
    chroma: ChromaUiModel,
    dominantColor: Color,
    onClick: () -> Unit
) {
    val skinName = chroma.chromaName
    val hasPreview = (chroma.chromaFullRender ?: chroma.chromaImageUrl) != null &&
                     (chroma.chromaFullRender ?: chroma.chromaImageUrl) != "null"

    Box(
        modifier = Modifier
            .width(220.dp)
            .height(160.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Theme.colors.surface.copy(alpha = 0.6f))
            .border(
                width = 1.dp,
                color = Theme.colors.textSecondary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(
                onClickLabel = "Apply $skinName to weapon"
            ) { onClick() }
            .semantics(mergeDescendants = true) {
                contentDescription = if (hasPreview) {
                    "$skinName, tap to apply this skin"
                } else {
                    "$skinName, no preview available, tap to apply this skin"
                }
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            dominantColor.copy(alpha = 0.2f),
                            Color.Transparent
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(12.dp)
            ) {
                val imageUrl = chroma.chromaFullRender ?: chroma.chromaImageUrl
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(dominantColor.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (imageUrl != null && imageUrl != "null") {
                        CoilImage(
                            url = imageUrl,
                            contentDescription = chroma.chromaName,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                        )
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "🎨",
                                style = Theme.typography.body16
                            )
                            Text(
                                text = "No Preview",
                                color = Theme.colors.textSecondary,
                                style = Theme.typography.body8
                            )
                        }
                    }
                }
                Text(
                    text = chroma.chromaName,
                    color = Theme.colors.textPrimary,
                    style = Theme.typography.body12,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
