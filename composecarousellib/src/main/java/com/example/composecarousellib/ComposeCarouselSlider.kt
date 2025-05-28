package com.example.composecarousellib

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil.compose.rememberAsyncImagePainter
import kotlin.math.absoluteValue

@Composable
fun ComposeCarouselSlider(
    modifier: Modifier = Modifier,
    height: Dp,
    sidePadding: Dp,
    pageSpacing: Dp = 8.dp,
    imageCornerRoundness: Dp = 8.dp,
    popularMoviesList: List<Int> = emptyList(),
) {
    val pagerState = rememberPagerState(pageCount = { popularMoviesList.size })
    HorizontalPager(
        modifier = modifier,
        state = pagerState,
        contentPadding = PaddingValues(horizontal = sidePadding),
        verticalAlignment = Alignment.CenterVertically,
        pageSpacing = pageSpacing
    ) { page ->
        Card(modifier = Modifier
            .clickable {

            }
            .height(height)
            .fillMaxWidth()
            .carouselTransition(page, pagerState),
            shape = RoundedCornerShape(imageCornerRoundness)) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = rememberAsyncImagePainter(popularMoviesList[page]),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
    }

}

fun Modifier.carouselTransition(page: Int, pagerState: PagerState) = graphicsLayer {
    val pageOffset =
        ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue

    val transformation = lerp(
        start = 0.90f, stop = 1f, fraction = 1f - pageOffset.coerceIn(0f, 1f)
    )
    alpha = transformation
    scaleY = transformation
}
