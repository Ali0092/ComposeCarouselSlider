package com.example.composecarousellib.indicators

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Classic dot indicator. The active dot is drawn larger and in [activeColor].
 */
class DotIndicator(
    private val activeColor: Color = Color.White,
    private val inactiveColor: Color = Color.Gray,
    private val activeSize: Dp = 12.dp,
    private val inactiveSize: Dp = 9.dp,
    private val spacing: Dp = 4.dp,
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
                Box(
                    modifier = Modifier
                        .padding(horizontal = spacing)
                        .size(if (isActive) activeSize else inactiveSize)
                        .clip(CircleShape)
                        .background(if (isActive) activeColor else inactiveColor)
                )
            }
        }
    }
}
