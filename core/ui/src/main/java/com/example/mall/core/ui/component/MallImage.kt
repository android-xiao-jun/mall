package com.example.mall.core.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest

/**
 * 统一图片加载组件
 *
 * 基于 Coil 封装，所有图片加载必须使用此组件
 */
@Composable
fun MallImage(
    imageUrl: String?,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    placeholder: Int? = null,
    error: Int? = null,
    fallback: Int? = null,
) {
    val context = LocalContext.current
    val requestBuilder = ImageRequest.Builder(context)
        .data(imageUrl)
        .crossfade(true)

    placeholder?.let { requestBuilder.placeholder(it) }
    error?.let { requestBuilder.error(it) }
    fallback?.let { requestBuilder.fallback(it) }

    AsyncImage(
        model = requestBuilder.build(),
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
    )
}
