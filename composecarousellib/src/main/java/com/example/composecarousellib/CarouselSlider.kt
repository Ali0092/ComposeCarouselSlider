package com.example.composecarousellib

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.composecarousellib.effects.CarouselEffect
import com.example.composecarousellib.effects.NoEffect
import com.example.composecarousellib.indicators.CarouselIndicator
import com.example.composecarousellib.indicators.ExpandingPillIndicator
import kotlinx.coroutines.delay
import kotlin.math.abs

/**
 * Generic image carousel driven by a swappable [CarouselEffect] and [CarouselIndicator].
 *
 * The slider is a thin host: it owns the [PagerState] + auto-scroll loop, lets the
 * effect render an optional full-slider backdrop and decorate each page, and lets
 * the indicator render itself. Swapping effect/indicator at runtime is safe — the
 * pager state is preserved.
 *
 * When [pageHeight] is null the pager fills its parent's height, which is what
 * backdrop-driven effects (e.g. WaterRipple) need. Pass a concrete Dp to pin the
 * pager to a card-style height instead.
 */
@Composable
fun CarouselSlider(
    items: List<CarouselImage>,
    modifier: Modifier = Modifier,
    showBackgroundImage: Boolean = true,
    backgroundBlurRadius: Dp = 40.dp,
    pageHeight: Dp? = null,
    contentPadding: PaddingValues = PaddingValues(horizontal = 32.dp),
    pageSpacing: Dp = 8.dp,
    itemCornerRadius: Dp = 20.dp,
    effect: CarouselEffect = NoEffect,
    indicator: CarouselIndicator? = ExpandingPillIndicator(),
    autoScroll: Boolean = false,
    autoScrollDelayMs: Long = 3_000L,
    autoScrollAnimationSpec: AnimationSpec<Float> = spring(),
    pagerState: PagerState = rememberPagerState { items.size },
    onItemClick: ((Int) -> Unit)? = null,
) {
    Box(modifier = modifier) {
        if (showBackgroundImage && items.isNotEmpty()) {
            CarouselBackgroundLayer(
                items = items,
                pagerState = pagerState,
                blurRadius = backgroundBlurRadius,
                modifier = Modifier.matchParentSize(),
            )
        }
        CarouselSliderContent(
            pageCount = items.size,
            modifier = Modifier,
            pageHeight = pageHeight,
            contentPadding = contentPadding,
            pageSpacing = pageSpacing,
            effect = effect,
            indicator = indicator,
            autoScroll = autoScroll,
            autoScrollDelayMs = autoScrollDelayMs,
            autoScrollAnimationSpec = autoScrollAnimationSpec,
            pagerState = pagerState,
            renderImage = { index, imageModifier ->
                CarouselImagePainter(
                    image = items[index],
                    cornerRadius = itemCornerRadius,
                    modifier = imageModifier,
                )
            },
            page = { pageIndex ->
                CarouselImageContent(
                    image = items[pageIndex],
                    cornerRadius = itemCornerRadius,
                    onClick = if (onItemClick != null) ({ onItemClick(pageIndex) }) else null,
                )
            },
        )
    }
}

/**
 * Slot-based [CarouselSlider] for non-image content. Callers supply both the per-page
 * renderer ([page]) and an [renderImage] callback that effects can use to draw the
 * "current image" in their backdrop.
 */
@Composable
fun CarouselSliderContent(
    pageCount: Int,
    modifier: Modifier = Modifier,
    pageHeight: Dp? = null,
    contentPadding: PaddingValues = PaddingValues(horizontal = 32.dp),
    pageSpacing: Dp = 8.dp,
    effect: CarouselEffect = NoEffect,
    indicator: CarouselIndicator? = ExpandingPillIndicator(),
    autoScroll: Boolean = false,
    autoScrollDelayMs: Long = 3_000L,
    autoScrollAnimationSpec: AnimationSpec<Float> = spring(),
    pagerState: PagerState = rememberPagerState { pageCount },
    renderImage: @Composable (index: Int, modifier: Modifier) -> Unit = { _, _ -> },
    page: @Composable (page: Int) -> Unit,
) {
    if (autoScroll && pageCount > 1) {
        LaunchedEffect(pagerState, autoScrollDelayMs) {
            while (true) {
                delay(autoScrollDelayMs)
                val next = (pagerState.currentPage + 1) % pagerState.pageCount
                pagerState.animateScrollToPage(next, animationSpec = autoScrollAnimationSpec)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            if (effect.prefersBackdrop) {
                effect.Backdrop(
                    pagerState = pagerState,
                    pageCount = pageCount,
                    renderImage = renderImage,
                )
            }

            HorizontalPager(
                modifier = modifier.fillMaxSize(),
                state = pagerState,
                contentPadding = contentPadding,
                verticalAlignment = Alignment.CenterVertically,
                pageSpacing = pageSpacing,
            ) { pageIndex ->
                val itemHeightModifier = if (pageHeight != null) {
                    Modifier.height(pageHeight)
                } else {
                    Modifier.fillMaxHeight()
                }
                Box(
                    modifier = Modifier
                        .then(itemHeightModifier)
                        .fillMaxWidth()
                        .then(effect.rememberItemModifier(pageIndex, pagerState)),
                    contentAlignment = Alignment.Center,
                ) {
                    effect.BackgroundContent(pageIndex, pagerState)
                    page(pageIndex)
                    effect.ForegroundContent(pageIndex, pagerState)
                }
            }
        }

        if (indicator != null && pageCount > 0) {
            indicator.Content(
                pagerState = pagerState,
                pageCount = pageCount,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
            )
        }
    }
}

@Composable
private fun CarouselImageContent(
    image: CarouselImage,
    cornerRadius: Dp,
    onClick: (() -> Unit)?,
) {
    val painter = when (image) {
        is CarouselImage.Resource -> rememberAsyncImagePainter(image.resId)
        is CarouselImage.Url -> rememberAsyncImagePainter(image.url)
        is CarouselImage.AsImageBitmap -> BitmapPainter(image.imageBitmap)
        is CarouselImage.Custom -> image.painter
    }
    val m = Modifier
        .fillMaxSize()
        .clip(RoundedCornerShape(cornerRadius))
        .background(Color.Black)
        .let { if (onClick != null) it.clickable(onClick = onClick) else it }
    Image(
        modifier = m,
        painter = painter,
        contentDescription = null,
        contentScale = ContentScale.Crop,
    )
}

@Composable
private fun CarouselImagePainter(
    image: CarouselImage,
    cornerRadius: Dp,
    modifier: Modifier,
) {
    val painter = when (image) {
        is CarouselImage.Resource -> rememberAsyncImagePainter(image.resId)
        is CarouselImage.Url -> rememberAsyncImagePainter(image.url)
        is CarouselImage.AsImageBitmap -> BitmapPainter(image.imageBitmap)
        is CarouselImage.Custom -> image.painter
    }
    Image(
        modifier = modifier.clip(RoundedCornerShape(cornerRadius)),
        painter = painter,
        contentDescription = null,
        contentScale = ContentScale.Crop,
    )
}

@Composable
private fun CarouselBackgroundLayer(
    items: List<CarouselImage>,
    pagerState: PagerState,
    blurRadius: Dp,
    modifier: Modifier = Modifier,
) {
    val settledIndex = pagerState.settledPage.coerceIn(0, items.lastIndex)
    val nextIndex = (settledIndex + 1).coerceAtMost(items.lastIndex)
    val prevIndex = (settledIndex - 1).coerceAtLeast(0)
    // Blur the whole backdrop so the carousel card in front visually pops; the fade
    // between pages happens inside the blurred layer so the transition looks natural.
    Box(modifier = modifier.blur(blurRadius)) {
        CarouselBackgroundImage(
            image = items[settledIndex],
            modifier = Modifier.matchParentSize(),
        )
        if (nextIndex != settledIndex) {
            CarouselBackgroundImage(
                image = items[nextIndex],
                modifier = Modifier
                    .matchParentSize()
                    .graphicsLayer {
                        val f = pagerState.currentPageOffsetFraction
                        alpha = if (f > 0f) f.coerceIn(0f, 1f) else 0f
                    },
            )
        }
        if (prevIndex != settledIndex) {
            CarouselBackgroundImage(
                image = items[prevIndex],
                modifier = Modifier
                    .matchParentSize()
                    .graphicsLayer {
                        val f = pagerState.currentPageOffsetFraction
                        alpha = if (f < 0f) abs(f).coerceIn(0f, 1f) else 0f
                    },
            )
        }
    }
}

@Composable
private fun CarouselBackgroundImage(
    image: CarouselImage,
    modifier: Modifier = Modifier,
) {
    val painter = when (image) {
        is CarouselImage.Resource -> rememberAsyncImagePainter(image.resId)
        is CarouselImage.Url -> rememberAsyncImagePainter(image.url)
        is CarouselImage.AsImageBitmap -> BitmapPainter(image.imageBitmap)
        is CarouselImage.Custom -> image.painter
    }
    Image(
        modifier = modifier,
        painter = painter,
        contentDescription = null,
        contentScale = ContentScale.Crop,
    )
}
