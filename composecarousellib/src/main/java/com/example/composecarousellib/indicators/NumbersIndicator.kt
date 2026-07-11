package com.example.composecarousellib.indicators

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * "3 / 12"-style page counter. Handy when there are too many pages for dots.
 */
class NumbersIndicator(
    private val backgroundColor: Color = Color.Black.copy(alpha = 0.35f),
    private val textColor: Color = Color.White,
) : CarouselIndicator {

    @Composable
    override fun Content(pagerState: PagerState, pageCount: Int, modifier: Modifier) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(backgroundColor)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${pagerState.currentPage + 1} / $pageCount",
                    color = textColor,
                )
            }
        }
    }
}
