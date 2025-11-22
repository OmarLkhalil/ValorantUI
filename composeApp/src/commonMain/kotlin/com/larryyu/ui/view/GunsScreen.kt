package com.larryyu.ui.view
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.larryyu.presentation.uistates.GunsState
import com.larryyu.presentation.viewmodel.GunsViewModel
import com.larryyu.ui.components.CoilImage
import com.larryyu.ui.theme.Theme
import com.larryyu.ui.view.gundetails.GunDetailsScreen
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import valorantui.composeapp.generated.resources.Res
import valorantui.composeapp.generated.resources.arrow
import valorantui.composeapp.generated.resources.valorant
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun GunsScreen(
    gunsViewModel: GunsViewModel = koinInject(),
    onThemeToggle: () -> Unit = {}
) {
    LaunchedEffect(Unit) {
        gunsViewModel.sendIntent(GunsIntent.FetchGuns)
    }
    val gunsState by gunsViewModel.gState.collectAsState()
    var selectedGun by remember { mutableStateOf<GunsData?>(null) }
    SharedTransitionLayout {
        AnimatedContent(
            targetState = selectedGun,
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            },
            label = "gunsTransition"
        ) { targetGun ->
            if (targetGun == null) {
                GunsGridRoute(
                    gunsState = gunsState,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@AnimatedContent,
                    onThemeToggle = onThemeToggle,
                    onGunClick = { gun -> selectedGun = gun }
                )
            } else {
                GunDetailsScreen(
                    gun = targetGun,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@AnimatedContent,
                    onBack = { selectedGun = null }
                )
            }
        }
    }
}
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun GunsGridRoute(
    gunsState: GunsState,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onThemeToggle: () -> Unit,
    onGunClick: (GunsData) -> Unit
) {
    val guns = gunsState.guns
    val bundles = gunsState.bundles
    val bundlesPagerState = rememberPagerState(
        pageCount = { bundles.size },
        initialPage = 0
    )
    val indicatorScrollState = rememberLazyListState()
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
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope,
        onThemeToggle = onThemeToggle,
        onGunClick = onGunClick
    )
}
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun GunsScreenContent(
    guns: List<GunsData>,
    bundles: List<BundlesData>,
    isLoading: Boolean,
    error: String?,
    bundlesPagerState: PagerState,
    indicatorScrollState: androidx.compose.foundation.lazy.LazyListState,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onThemeToggle: () -> Unit,
    onGunClick: (GunsData) -> Unit
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
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Theme.colors.textPrimary
                    )
                }
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
                else -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (bundles.isNotEmpty()) {
                            SectionHeader(
                                title = "Featured Bundles",
                                onSeeAllClick = {  }
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
                        if (guns.isNotEmpty()) {
                            SectionHeader(
                                title = "Featured Guns",
                                onSeeAllClick = {  }
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            GunsGrid(
                                guns = guns,
                                sharedTransitionScope = sharedTransitionScope,
                                animatedVisibilityScope = animatedVisibilityScope,
                                onGunClick = onGunClick
                            )
                        }
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(0.3f))
        )
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
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun GunsGrid(
    guns: List<GunsData>,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onGunClick: (GunsData) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(guns, key = { it.uuid ?: it.displayName ?: "" }) { gun ->
            GunCard(
                gun = gun,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
                onClick = { onGunClick(gun) }
            )
        }
    }
}
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun GunCard(
    gun: GunsData,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    with(sharedTransitionScope) {
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
                SubcomposeAsyncImage(
                    model = gun.displayIcon,
                    contentDescription = gun.displayName,
                    modifier = Modifier
                        .sharedElement(
                            rememberSharedContentState(key = "gun-image-${gun.uuid}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
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
                Text(
                    text = gun.displayName ?: "Unknown Gun",
                    color = Theme.colors.textPrimary,
                    style = Theme.typography.body12,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    modifier = Modifier
                        .sharedElement(
                            rememberSharedContentState(key = "gun-name-${gun.uuid}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                        .fillMaxWidth()
                )
            }
        }
    }
}
