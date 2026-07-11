package com.example.composecarousellib.indicators

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Expanding-pill indicator — the active page is a rounded pill, inactive pages are
 * small dots. Standard on modern onboarding flows.
 */
class ExpandingPillIndicator(
    private val activeColor: Color = Color.White,
    private val inactiveColor: Color = Color.White.copy(alpha = 0.35f),
    private val pillHeight: Dp = 8.dp,
    private val inactiveWidth: Dp = 8.dp,
    private val activeWidth: Dp = 26.dp,
    private val spacing: Dp = 6.dp,
) : CarouselIndicator {

    @Composable
    override fun Content(pagerState: PagerState, pageCount: Int, modifier: Modifier) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(pageCount) { index ->
                val isActive = index == pagerState.currentPage
                val w by animateDpAsState(
                    targetValue = if (isActive) activeWidth else inactiveWidth,
                    animationSpec = tween(durationMillis = 260),
                    label = "pill-width",
                )
                Box(
                    modifier = Modifier
                        .padding(horizontal = spacing / 2)
                        .height(pillHeight)
                        .width(w)
                        .clip(RoundedCornerShape(50))
                        .background(if (isActive) activeColor else inactiveColor),
                )
            }
        }
    }
}
