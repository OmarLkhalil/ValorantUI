package com.larryyu.ui.view.gundetails

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.larryyu.domain.model.GunsData
import com.larryyu.ui.components.BackHandler
import com.larryyu.ui.components.calculateDominantColor
import com.larryyu.ui.theme.Theme
import com.larryyu.ui.view.gundetails.components.BasicInfoSection
import com.larryyu.ui.view.gundetails.components.LevelsColumn
import com.larryyu.ui.view.gundetails.components.RotatableGunImage
import com.larryyu.ui.view.gundetails.components.SectionTitle
import com.larryyu.ui.view.gundetails.components.SkinsRow
import org.jetbrains.compose.resources.painterResource
import valorantui.composeapp.generated.resources.Res
import valorantui.composeapp.generated.resources.arrow

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun GunDetailsScreen(
    gun: GunsData,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onBack: () -> Unit
) {
    BackHandler(onBack = onBack)
    val surfaceColor = Theme.colors.surface
    var dominantColor by remember { mutableStateOf(surfaceColor) }

    LaunchedEffect(gun.displayIcon) {
        calculateDominantColor(
            source = gun.displayIcon.orEmpty(),
        ) { color ->
            dominantColor = color
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    with(sharedTransitionScope) {
                        Text(
                            text = gun.displayName ?: "Gun Details",
                            color = Theme.colors.textPrimary,
                            style = Theme.typography.headline18,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.sharedElement(
                                rememberSharedContentState(key = "gun-name-${gun.uuid}"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.semantics {
                            contentDescription = "Navigate back to weapons list"
                        }
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.arrow),
                            contentDescription = "Back button",
                            tint = Theme.colors.textPrimary,
                            modifier = Modifier
                                .size(48.dp)
                                .padding(12.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Theme.colors.background
                )
            )
        },
        containerColor = Theme.colors.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            GunDetailsContent(
                gun = gun,
                dominantColor = dominantColor,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope
            )
        }
    }
}
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun GunDetailsContent(
    gun: GunsData,
    dominantColor: Color,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    var selectedImageUrl by remember { mutableStateOf(gun.displayIcon) }
    val allChromas = remember(gun.skins) {
        gun.skins?.flatMap { skin ->
            skin?.chromas?.filterNotNull() ?: emptyList()
        } ?: emptyList()
    }
    val allLevels = remember(gun.skins) {
        gun.skins?.flatMap { skin ->
            skin?.levels?.filterNotNull() ?: emptyList()
        } ?: emptyList()
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .semantics {
                contentDescription = "${gun.displayName ?: "Weapon"} details screen, scroll to view all information"
            },
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            RotatableGunImage(
                imageUrl = selectedImageUrl,
                gunUuid = gun.uuid,
                gunName = gun.displayName,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope
            )
        }
        if (allChromas.isNotEmpty()) {
            item {
                SectionTitle(text = "Available Skins")
            }
            item {
                SkinsRow(
                    chromas = allChromas,
                    dominantColor = dominantColor,
                    onSkinClick = { chromaImageUrl: String? ->
                        selectedImageUrl = chromaImageUrl
                    }
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
            SectionTitle(text = "Basic Information")
        }
        item {
            BasicInfoSection(
                gun = gun,
                dominantColor = dominantColor
            )
        }
        if (allLevels.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                SectionTitle(text = "Upgrade Levels")
            }
            item {
                LevelsColumn(
                    levels = allLevels,
                    dominantColor = dominantColor
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
