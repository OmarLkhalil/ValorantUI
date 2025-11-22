package com.larryyu.ui.view.gundetails.components
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.larryyu.domain.model.GunsData
import com.larryyu.ui.theme.Theme
import com.larryyu.ui.view.gundetails.utils.extractCategory
import com.larryyu.ui.view.gundetails.utils.getRarityName

@Composable
fun BasicInfoSection(
    gun: GunsData,
    dominantColor: Color,
    modifier: Modifier = Modifier
) {
    val category = extractCategory(gun.assetPath)
    val rarity = gun.contentTierUuid?.let { getRarityName(it) } ?: "Unknown"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = "Basic weapon information, Category: $category, Rarity: $rarity"
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Theme.colors.surface.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InfoRow(
                label = "Category",
                value = extractCategory(gun.assetPath),
                icon = "🎯"
            )
            gun.displayName?.let { name ->
                InfoRow(
                    label = "Display Name",
                    value = name,
                    icon = "📌"
                )
            }
            gun.themeUuid?.let { theme ->
                InfoRow(
                    label = "Theme",
                    value = theme.take(13) + "...",
                    icon = "🎨"
                )
            }
            gun.contentTierUuid?.let { tier ->
                InfoRow(
                    label = "Rarity",
                    value = getRarityName(tier),
                    icon = "⭐"
                )
            }
        }
    }
}
@Composable
private fun InfoRow(
    label: String,
    value: String,
    icon: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {
                contentDescription = "$label: $value"
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = icon,
                style = Theme.typography.body16
            )
            Text(
                text = label,
                color = Theme.colors.textSecondary,
                style = Theme.typography.body12,
                fontWeight = FontWeight.Medium
            )
        }
        Text(
            text = value,
            color = Theme.colors.textPrimary,
            style = Theme.typography.body16,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}
