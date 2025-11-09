package com.larryyu.ui.view

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.larryyu.domain.model.BundlesData
import com.larryyu.domain.model.GunsData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import valorantui.composeapp.generated.resources.Res
import valorantui.composeapp.generated.resources.valorant


@Composable
fun GunsScreen(
    guns: List<GunsData>,
    bundles: List<BundlesData>
) {
    val horizontalPagerState = rememberPagerState(
        pageCount = { guns.size },
        initialPage = 0
    )

    val coroutineScope = rememberCoroutineScope()
    val indicatorScrollState = rememberLazyListState()

    Scaffold(
        topBar = {
            SubcomposeAsyncImage(
                model = Res.drawable.valorant,
                contentDescription = "",
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 5.dp)
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .height(60.dp),
                contentScale = ContentScale.Fit
            )
        },
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black.copy(0.9f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Featured Bundles",
                    color = Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(start = 5.dp),
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = "See all",
                    color = Color.LightGray,
                    fontSize = 12.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 5.dp),
                    fontWeight = FontWeight.Bold,
                )
            }
            if (bundles.isNotEmpty()) {
                HorizontalPager(

                    state = horizontalPagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                ) { page ->
                    BundlesCard(
                        bundle = bundles[page], modifier = Modifier
                    )
                }
            }
            LazyRow(
                Modifier
                    .wrapContentHeight()
                    .width((15 * 8).dp),
                state = indicatorScrollState,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (bundles.isNotEmpty()) {
                    repeat(bundles.size) { iteration ->
                        item(key = "items$iteration") {
                            val color =
                                if (horizontalPagerState.currentPage == iteration) Color.White else Color.LightGray
                            val size by animateDpAsState(
                                targetValue = if (horizontalPagerState.currentPage == iteration) 15.dp else 8.dp,
                                label = ""
                            )
                            Box(
                                modifier = Modifier
                                    .size(size)
                                    .clip(CircleShape)
                                    .background(color)
                                    .padding(5.dp)
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Featured Guns",
                    color = Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(start = 5.dp),
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = "See all",
                    color = Color.LightGray,
                    fontSize = 12.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 5.dp),
                    fontWeight = FontWeight.Bold,
                )
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(5.dp),
                modifier = Modifier.padding(5.dp)
            ) {
                items(guns) { gun ->
                    GunsCard(gun)
                }
            }
        }
    }

    LaunchedEffect(horizontalPagerState) {
        snapshotFlow { horizontalPagerState.currentPage }.collect { page ->
            if (horizontalPagerState.pageCount == 0) return@collect
            coroutineScope.launch {
                delay(2500)
                val pageCount = horizontalPagerState.pageCount
                if (pageCount == 0) return@launch
                val nextPage = if (page == pageCount - 1) 0 else (page + 1).coerceAtMost(pageCount - 1)
                horizontalPagerState.animateScrollToPage(nextPage)
            }
        }
    }

    if (horizontalPagerState.pageCount != 0) {
        LaunchedEffect(key1 = horizontalPagerState.currentPage, block = {
            val currentPage = horizontalPagerState.currentPage
            val size = indicatorScrollState.layoutInfo.visibleItemsInfo.size
            val lastVisibleIndex =
                indicatorScrollState.layoutInfo.visibleItemsInfo.last().index
            val firstVisibleItemIndex = indicatorScrollState.firstVisibleItemIndex

            if (currentPage > lastVisibleIndex - 1) {
                indicatorScrollState.animateScrollToItem(currentPage - size + 2)
            } else if (currentPage <= firstVisibleItemIndex + 1) {
                indicatorScrollState.animateScrollToItem((currentPage - 1).coerceAtLeast(0))
            }
        })
    }
}


@Composable
fun BundlesCard(
    bundle: BundlesData,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .height(150.dp)
            .width(280.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        SubcomposeAsyncImage(
            model = bundle.displayIcon,
            contentDescription = "image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
            loading = {
                Box(modifier = Modifier.size(50.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(0.2f))
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = bundle.displayName ?: "",
                color = Color.White,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(10.dp),
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = bundle.displayNameSubText ?: "",
                color = Color.White,
                fontSize = 8.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
fun GunsCard(
    guns: GunsData,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(5.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .width(100.dp)
            .height(60.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        SubcomposeAsyncImage(
            model = guns.displayIcon,
            contentDescription = "image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
            loading = {
                Box(modifier = Modifier.size(50.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(0.4f))
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = guns.displayName ?: "",
                color = Color.White,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(10.dp),
                fontWeight = FontWeight.Bold,
            )
        }
    }
}