package com.larryyu.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import com.larryyu.ui.theme.Theme


@Composable
fun CoilImage(
    url: String?,
    contentDescription: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    onSuccess: ((AsyncImagePainter.State.Success) -> Unit)? = null,
) {
    SubcomposeAsyncImage(
        model = url,
        contentDescription = contentDescription,
        loading = {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    modifier = Modifier.size(30.dp),
                    color = Theme.colors.textPrimary
                )
            }
        },
        onSuccess = onSuccess,
        contentScale = contentScale,
        modifier = modifier
    )
}
