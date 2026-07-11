package com.example.composecarousellib.indicators

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.composecarousellib.CarouselImage
import kotlinx.coroutines.launch

/**
 * Thumbnail strip below the pager. The active thumbnail is highlighted with a border
 * and clicking a thumbnail scrolls the pager to that page.
 */
class ThumbnailIndicator(
    private val thumbnails: List<CarouselImage>,
    private val size: Dp = 56.dp,
    private val cornerRadius: Dp = 8.dp,
    private val activeBorderColor: Color = Color.White,
    private val inactiveAlpha: Float = 0.5f,
) : CarouselIndicator {

    @Composable
    override fun Content(pagerState: PagerState, pageCount: Int, modifier: Modifier) {
        val scope = rememberCoroutineScope()
        val listState = rememberLazyListState()

        LaunchedEffect(pagerState.currentPage) {
            listState.animateScrollToItem(pagerState.currentPage)
        }

        LazyRow(
            modifier = modifier.height(size + 16.dp),
            state = listState,
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            itemsIndexed(thumbnails.take(pageCount)) { index, image ->
                val isActive = index == pagerState.currentPage
                val painter = when (image) {
                    is CarouselImage.Resource -> rememberAsyncImagePainter(image.resId)
                    is CarouselImage.Url -> rememberAsyncImagePainter(image.url)
                    is CarouselImage.AsImageBitmap -> androidx.compose.ui.graphics.painter.BitmapPainter(image.imageBitmap)
                    is CarouselImage.Custom -> image.painter
                }
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp, vertical = 8.dp)
                        .size(this@ThumbnailIndicator.size)
                        .clip(RoundedCornerShape(cornerRadius))
                        .background(Color.Black)
                        .border(
                            width = if (isActive) 2.dp else 0.dp,
                            color = if (isActive) activeBorderColor else Color.Transparent,
                            shape = RoundedCornerShape(cornerRadius),
                        )
                        .clickable {
                            scope.launch { pagerState.animateScrollToPage(index) }
                        }
                ) {
                    Image(
                        painter = painter,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(this@ThumbnailIndicator.size)
                            .let { if (!isActive) it.padding(0.dp) else it },
                        alpha = if (isActive) 1f else inactiveAlpha,
                    )
                }
            }
        }
    }
}
