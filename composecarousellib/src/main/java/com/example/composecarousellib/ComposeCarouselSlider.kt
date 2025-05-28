package com.example.composecarousellib

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import kotlin.math.absoluteValue
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.util.lerp


@Composable
fun ComposeCarouselSlider(
    popularMoviesList: List<Int> = emptyList()
) {
    val pagerState = rememberPagerState(pageCount = { popularMoviesList.size })

    Column(modifier = Modifier.fillMaxSize().padding(vertical = 100.dp)) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 50.dp),
            verticalAlignment = Alignment.CenterVertically,
            pageSpacing = 8.dp
        ) { page ->
            //defined in ClubbedPhotos.kt.

            Card(
                modifier = Modifier
                    .clickable {

                    }
                    .height(350.dp)
                    .width(320.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .carouselTransition(page, pagerState), shape = RoundedCornerShape(15.dp)
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = rememberAsyncImagePainter(popularMoviesList[page]),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
        }
    }

}

fun Modifier.carouselTransition(page: Int, pagerState: PagerState) = graphicsLayer {
    val pageOffset =
        ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue

    val transformation = lerp(
        start = 0.80f, stop = 1f, fraction = 1f - pageOffset.coerceIn(0f, 1f)
    )
    alpha = transformation
    scaleY = transformation
}
