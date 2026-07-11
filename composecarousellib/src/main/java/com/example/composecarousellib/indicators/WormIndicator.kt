package com.example.composecarousellib.indicators

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Worm indicator — the active pill stretches between two adjacent dots as you drag.
 */
class WormIndicator(
    private val activeColor: Color = Color.White,
    private val inactiveColor: Color = Color.Gray.copy(alpha = 0.5f),
    private val dotSize: Dp = 8.dp,
    private val spacing: Dp = 8.dp,
) : CarouselIndicator {

    @Composable
    override fun Content(pagerState: PagerState, pageCount: Int, modifier: Modifier) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Track container
            Layout(
                content = {
                    // Base dots
                    repeat(pageCount) {
                        Box(
                            modifier = Modifier
                                .size(dotSize)
                                .clip(CircleShape)
                                .background(inactiveColor)
                        )
                    }
                    // Worm
                    Box(
                        modifier = Modifier
                            .height(dotSize)
                            .clip(CircleShape)
                            .background(activeColor)
                    )
                },
            ) { measurables, constraints ->
                val dotPx = dotSize.roundToPx()
                val spacingPx = spacing.roundToPx()
                val dots = measurables.take(pageCount)
                val wormMeasurable = measurables.last()

                val current = pagerState.currentPage
                val fraction = pagerState.currentPageOffsetFraction
                val target = current + if (fraction >= 0f) 1 else -1

                val startX: Int
                val endX: Int
                if (fraction == 0f || pageCount <= 1) {
                    startX = current * (dotPx + spacingPx)
                    endX = startX + dotPx
                } else {
                    val a = current * (dotPx + spacingPx)
                    val b = target.coerceIn(0, pageCount - 1) * (dotPx + spacingPx)
                    val progress = abs(fraction).coerceIn(0f, 1f)
                    val leadingCenter = if (fraction > 0f) a else b
                    val trailingCenter = if (fraction > 0f) b else a
                    val stretchStart = leadingCenter + (trailingCenter - leadingCenter) * (progress - 0.5f).coerceAtLeast(0f) * 2f
                    val stretchEnd = leadingCenter + (trailingCenter - leadingCenter) * min(progress * 2f, 1f)
                    startX = min(stretchStart, stretchEnd).roundToInt()
                    endX = (max(stretchStart, stretchEnd) + dotPx).roundToInt()
                }

                val wormWidth = max(dotPx, endX - startX)
                val wormPlaceable = wormMeasurable.measure(
                    androidx.compose.ui.unit.Constraints.fixed(wormWidth, dotPx)
                )
                val dotPlaceables = dots.map { it.measure(androidx.compose.ui.unit.Constraints.fixed(dotPx, dotPx)) }

                val totalWidth = pageCount * dotPx + (pageCount - 1).coerceAtLeast(0) * spacingPx
                layout(totalWidth, dotPx) {
                    dotPlaceables.forEachIndexed { i, p -> p.place(i * (dotPx + spacingPx), 0) }
                    wormPlaceable.place(startX, 0)
                }
            }
        }
    }
}
