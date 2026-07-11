package com.example.composecarousellib.indicators

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Instagram-story-style bar indicator — one segment per page, current segment fills
 * left-to-right as the user drags/auto-scrolls.
 */
class BarIndicator(
    private val activeColor: Color = Color.White,
    private val inactiveColor: Color = Color.White.copy(alpha = 0.35f),
    private val height: Dp = 3.dp,
    private val spacing: Dp = 4.dp,
    private val cornerRadius: Dp = 2.dp,
) : CarouselIndicator {

    @Composable
    override fun Content(pagerState: PagerState, pageCount: Int, modifier: Modifier) {
        Row(
            modifier = modifier.padding(horizontal = 16.dp),
        ) {
            val current = pagerState.currentPage
            val offset = pagerState.currentPageOffsetFraction
            repeat(pageCount) { index ->
                val fill = when {
                    index < current -> 1f
                    index == current -> (1f - offset.coerceIn(0f, 1f) + offset.coerceIn(-1f, 0f)).coerceIn(0f, 1f)
                    index == current + 1 -> offset.coerceIn(0f, 1f)
                    else -> 0f
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = spacing / 2)
                        .height(height)
                        .clip(RoundedCornerShape(cornerRadius))
                        .background(inactiveColor)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fill)
                            .height(height)
                            .background(activeColor)
                    )
                }
            }
        }
    }
}
