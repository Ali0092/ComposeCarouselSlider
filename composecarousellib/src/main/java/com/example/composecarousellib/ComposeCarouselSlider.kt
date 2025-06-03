package com.example.composecarousellib

import android.R.attr.bitmap
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.asImageBitmap

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue



@Composable
fun ComposeCarouselSlider(
    modifier: Modifier = Modifier,
    height: Dp,
    sidePadding: Dp,
    pageSpacing: Dp = 8.dp,
    imageCornerRoundness: Dp = 8.dp,
    imagesList: List<CarouselImage> = emptyList(),
    useDotIndicator: Boolean = true,
    nonSelectedDotColor: Color = Color.Gray,
    selectedDotColor: Color = Color.White,
    delay: Long = 3_000L,
    enableAutoScroll: Boolean = false,
    enableAnimationOnAutoScroll: Boolean = false,
    animationSpecs: AnimationSpec<Float> = spring(),
    getOnClick: (Int) -> Unit =  {}
) {
    val pagerState = rememberPagerState(pageCount = { imagesList.size })

    if (enableAutoScroll) {
        LaunchedEffect(pagerState) {
            while (true) {
                delay(delay)
                val nextPage = (pagerState.currentPage + 1) % pagerState.pageCount
                pagerState.animateScrollToPage(nextPage, animationSpec = if (enableAnimationOnAutoScroll) {
                    animationSpecs
                }else spring())
            }
        }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        HorizontalPager(
            modifier = modifier,
            state = pagerState,
            contentPadding = PaddingValues(horizontal = sidePadding),
            verticalAlignment = Alignment.CenterVertically,
            pageSpacing = pageSpacing
        ) { page ->
            Card(modifier = Modifier
                .clickable {
                    getOnClick(page)
                }
                .height(height)
                .fillMaxWidth()
                .carouselTransition(page, pagerState),
                shape = RoundedCornerShape(imageCornerRoundness)) {

                CarouselImageItem(imagesList[page])
            }
        }

       if (useDotIndicator) {
           DotIndicator(
               pageCount = imagesList.size,
               currentPage = pagerState.currentPage,
               nonSelectedDotColor = nonSelectedDotColor,
               selectedDotColor = selectedDotColor
           )
       }
    }

}

@Composable
fun CarouselImageItem(image: CarouselImage) {

    val painter = when (image) {
        is CarouselImage.Resource -> rememberAsyncImagePainter(image.resId)
        is CarouselImage.Url -> rememberAsyncImagePainter(image.url)
        is CarouselImage.AsImageBitmap -> BitmapPainter(image = image.imageBitmap)
        is CarouselImage.Custom -> image.painter
    }

    Image(
        modifier = Modifier.fillMaxSize(),
        painter = painter,
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
}

fun Modifier.carouselTransition(page: Int, pagerState: PagerState) = graphicsLayer {
    val pageOffset = ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue

    val transformation = lerp(
        start = 0.80f, stop = 1f, fraction = 1f - pageOffset.coerceIn(0f, 1f)
    )
//    alpha = transformation
    scaleY = transformation
}


@Composable
fun DotIndicator(
    pageCount: Int,
    currentPage: Int,
    nonSelectedDotColor: Color = Color.Gray,
    selectedDotColor: Color = Color.White
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(pageCount) { index ->
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(if (index == currentPage) 12.dp else 9.dp)
                    .clip(CircleShape)
                    .background(if (index == currentPage) selectedDotColor else nonSelectedDotColor)
            )
        }
    }
}