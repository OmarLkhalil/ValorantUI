package com.larryyu.ui.view

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.larryyu.domain.model.BundlesData
import com.larryyu.domain.model.GunsData
import com.larryyu.presentation.uistates.GunsIntent
import com.larryyu.presentation.viewmodel.GunsViewModel
import com.larryyu.ui.components.CoilImage
import com.larryyu.ui.theme.Theme
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import valorantui.composeapp.generated.resources.Res
import valorantui.composeapp.generated.resources.arrow
import valorantui.composeapp.generated.resources.valorant


/**
 * Main GunsScreen - State Holder
 * All states are managed here at the parent level
 */
@Composable
fun GunsScreen(
    gunsViewModel: GunsViewModel = koinInject(),
    onThemeToggle: () -> Unit = {}
) {
    // Fetch data on first composition
    LaunchedEffect(Unit) {
        gunsViewModel.sendIntent(GunsIntent.FetchGuns)
    }

    // Collect state properly
    val gunsState by gunsViewModel.gState.collectAsState()
    val guns = gunsState.guns
    val bundles = gunsState.bundles

    // Pager and scroll states
    val bundlesPagerState = rememberPagerState(
        pageCount = { bundles.size },
        initialPage = 0
    )
    val indicatorScrollState = rememberLazyListState()

    // Auto-scroll effect for bundles pager
    LaunchedEffect(bundlesPagerState.pageCount) {
        if (bundlesPagerState.pageCount == 0) return@LaunchedEffect

        snapshotFlow { bundlesPagerState.currentPage }.collect { page ->
            delay(2500)
            val nextPage = if (page >= bundlesPagerState.pageCount - 1) {
                0
            } else {
                page + 1
            }
            bundlesPagerState.animateScrollToPage(nextPage)
        }
    }

    // Sync indicator scroll with pager
    LaunchedEffect(bundlesPagerState.currentPage) {
        if (bundlesPagerState.pageCount == 0) return@LaunchedEffect

        val currentPage = bundlesPagerState.currentPage
        val visibleItems = indicatorScrollState.layoutInfo.visibleItemsInfo
        if (visibleItems.isEmpty()) return@LaunchedEffect

        val lastVisibleIndex = visibleItems.last().index
        val firstVisibleIndex = visibleItems.first().index

        when {
            currentPage > lastVisibleIndex - 1 -> {
                indicatorScrollState.animateScrollToItem((currentPage - 2).coerceAtLeast(0))
            }
            currentPage <= firstVisibleIndex + 1 -> {
                indicatorScrollState.animateScrollToItem((currentPage - 1).coerceAtLeast(0))
            }
        }
    }

    GunsScreenContent(
        guns = guns,
        bundles = bundles,
        isLoading = gunsState.isLoading,
        error = gunsState.error,
        bundlesPagerState = bundlesPagerState,
        indicatorScrollState = indicatorScrollState,
        onThemeToggle = onThemeToggle
    )
}

/**
 * GunsScreen Content - Stateless UI
 */
@Composable
private fun GunsScreenContent(
    guns: List<GunsData>,
    bundles: List<BundlesData>,
    isLoading: Boolean,
    error: String?,
    bundlesPagerState: PagerState,
    indicatorScrollState: androidx.compose.foundation.lazy.LazyListState,
    onThemeToggle: () -> Unit
) {
    Scaffold(
        topBar = {
            GunsTopBar(onThemeToggle = onThemeToggle)
        },
        modifier = Modifier.fillMaxSize(),
        containerColor = Theme.colors.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                // Loading State
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Theme.colors.textPrimary
                    )
                }

                // Error State
                error != null -> {
                    Text(
                        text = error,
                        color = Theme.colors.error,
                        style = Theme.typography.body16,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }

                // Content State
                else -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Featured Bundles Section
                        if (bundles.isNotEmpty()) {
                            SectionHeader(
                                title = "Featured Bundles",
                                onSeeAllClick = { /* TODO: Navigate to all bundles */ }
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            BundlesPager(
                                bundles = bundles,
                                pagerState = bundlesPagerState,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            PagerIndicator(
                                pagerState = bundlesPagerState,
                                indicatorScrollState = indicatorScrollState
                            )

                            Spacer(modifier = Modifier.height(24.dp))
                        }

                        // Featured Guns Section
                        if (guns.isNotEmpty()) {
                            SectionHeader(
                                title = "Featured Guns",
                                onSeeAllClick = { /* TODO: Navigate to all guns */ }
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            GunsGrid(
                                guns = guns,
                                onGunClick = { /* TODO: Navigate to gun details */ }
                            )
                        }

                        // Empty State
                        if (guns.isEmpty() && bundles.isEmpty()) {
                            Text(
                                text = "No data available",
                                color = Theme.colors.textSecondary,
                                style = Theme.typography.body16,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(32.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * GunsScreen TopBar - Stateless Component
 */
@Composable
private fun GunsTopBar(
    onThemeToggle: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(top = 20.dp, bottom = 3.dp)
    ) {
        CoilImage(
            url = Res.drawable.valorant,
            contentDescription = "Valorant Logo",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            contentScale = ContentScale.Fit
        )

        Icon(
            painter = painterResource(Res.drawable.arrow),
            contentDescription = "Toggle Theme",
            tint = Theme.colors.textPrimary,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp)
                .size(26.dp)
                .clickable { onThemeToggle() }
        )
    }
}

/**
 * Section Header - Reusable Component
 */
@Composable
private fun SectionHeader(
    title: String,
    onSeeAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            color = Theme.colors.textPrimary,
            style = Theme.typography.headline18,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "See all",
            color = Theme.colors.textSecondary,
            style = Theme.typography.body12,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.clickable { onSeeAllClick() }
        )
    }
}


/**
 * Bundles Pager - Stateless Component
 */
@Composable
private fun BundlesPager(
    bundles: List<BundlesData>,
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    HorizontalPager(
        state = pagerState,
        modifier = modifier
    ) { page ->
        BundleCard(
            bundle = bundles[page]
        )
    }
}

/**
 * Pager Indicator - Stateless Component
 */
@Composable
private fun PagerIndicator(
    pagerState: PagerState,
    indicatorScrollState: androidx.compose.foundation.lazy.LazyListState,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .wrapContentHeight()
            .width((15 * 8).dp),
        state = indicatorScrollState,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(pagerState.pageCount) { index ->
            val isSelected = pagerState.currentPage == index
            val size by animateDpAsState(
                targetValue = if (isSelected) 15.dp else 8.dp,
                label = "indicator_size"
            )
            val color = if (isSelected) Theme.colors.textPrimary else Theme.colors.textSecondary

            Box(
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

/**
 * Bundle Card - Stateless Component
 */
@Composable
private fun BundleCard(
    bundle: BundlesData,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .height(160.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {
        SubcomposeAsyncImage(
            model = bundle.displayIcon,
            contentDescription = bundle.displayName,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            loading = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Theme.colors.textPrimary,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        )

        // Gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(0.3f))
        )

        // Bundle info
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = bundle.displayName ?: "Bundle",
                color = Color.White,
                style = Theme.typography.headline18,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )

            if (!bundle.displayNameSubText.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = bundle.displayNameSubText,
                    color = Color.White.copy(0.8f),
                    style = Theme.typography.body12,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * Guns Grid - Stateless Component
 */
@Composable
private fun GunsGrid(
    guns: List<GunsData>,
    onGunClick: (GunsData) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(guns) { gun ->
            GunCard(
                gun = gun,
                onClick = { onGunClick(gun) }
            )
        }
    }
}

/**
 * Gun Card - Stateless Component
 */
@Composable
private fun GunCard(
    gun: GunsData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Theme.colors.surface)
            .height(140.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(12.dp)
        ) {
            // Gun Image
            SubcomposeAsyncImage(
                model = gun.displayIcon,
                contentDescription = gun.displayName,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                contentScale = ContentScale.Fit,
                loading = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Theme.colors.textPrimary,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Gun Name
            Text(
                text = gun.displayName ?: "Unknown Gun",
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