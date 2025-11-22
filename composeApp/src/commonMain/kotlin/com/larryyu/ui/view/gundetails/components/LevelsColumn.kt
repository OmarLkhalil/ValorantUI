package com.larryyu.ui.view.gundetails.components
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import com.larryyu.domain.model.LevelsItem
import com.larryyu.ui.components.CoilImage
import com.larryyu.ui.theme.Theme

@Composable
fun LevelsColumn(
    levels: List<LevelsItem>,
    dominantColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = "Weapon upgrade levels section, ${levels.size} level${if (levels.size > 1) "s" else ""} available"
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Theme.colors.surface.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics {
                        heading()
                        contentDescription = "Upgrade levels section header, ${levels.size} upgrade level${if (levels.size > 1) "s" else ""}"
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "⬆️ ${levels.size} Upgrade Level${if (levels.size > 1) "s" else ""}",
                    color = Theme.colors.textPrimary,
                    style = Theme.typography.body12,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            levels.forEachIndexed { index, level ->
                LevelCard(
                    level = level,
                    dominantColor = dominantColor,
                    levelNumber = index + 1
                )
            }
        }
    }
}
@Composable
private fun LevelCard(
    level: LevelsItem,
    dominantColor: Color,
    levelNumber: Int = 1
) {
    val levelName = level.displayName ?: "Unknown Level"
    val levelItemText = level.levelItem?.toString()
        ?.replace("\"", "")
        ?.replace("EEquippableSkinLevelItem::", "")
        ?.replace("_", " ")
    val hasVideo = level.streamedVideo?.toString()?.replace("\"", "") != "null"

    val contentDesc = buildString {
        append("Level $levelNumber, $levelName")
        if (levelItemText != null && levelItemText != "null") {
            append(", Feature: $levelItemText")
        }
        if (hasVideo) {
            append(", Video preview available")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Theme.colors.surface.copy(alpha = 0.6f))
            .border(
                width = 1.dp,
                color = Theme.colors.textSecondary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.dp)
            )
            .semantics(mergeDescendants = true) {
                contentDescription = contentDesc
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            dominantColor.copy(alpha = 0.2f),
                            Color.Transparent
                        )
                    )
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(70.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                        .background(dominantColor.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    level.displayIcon?.let { iconUrl ->
                        CoilImage(
                            url = iconUrl,
                            contentDescription = level.displayName ?: "Level",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                        )
                    } ?: run {
                        Text(
                            text = "🔫",
                            style = Theme.typography.headline18
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(24.dp)
                        .clip(androidx.compose.foundation.shape.CircleShape)
                        .background(Theme.colors.textPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = levelNumber.toString(),
                        color = Theme.colors.background,
                        style = Theme.typography.body8,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = level.displayName ?: "Unknown Level",
                    color = Theme.colors.textPrimary,
                    style = Theme.typography.body16,
                    fontWeight = FontWeight.Bold
                )
                level.levelItem?.let { item ->
                    val itemText = item.toString()
                        .replace("\"", "")
                        .replace("EEquippableSkinLevelItem::", "")
                        .replace("_", " ")
                    if (itemText != "null") {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "✨",
                                style = Theme.typography.body12
                            )
                            Text(
                                text = itemText,
                                color = Theme.colors.textSecondary,
                                style = Theme.typography.body12,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
                level.streamedVideo?.let { video ->
                    val videoUrl = video.toString().replace("\"", "")
                    if (videoUrl != "null") {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🎬",
                                style = Theme.typography.body8
                            )
                            Text(
                                text = "Preview Available",
                                color = Theme.colors.textSecondary,
                                style = Theme.typography.body8,
                                fontWeight = FontWeight.Light
                            )
                        }
                    }
                }
            }
        }
    }
}
