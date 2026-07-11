package com.example.composecarousellib.indicators

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A pluggable indicator strategy for the carousel.
 *
 * Indicators are stateless composables driven by the current [PagerState]. The
 * indicator decides its own layout, size, and drawing.
 */
fun interface CarouselIndicator {
    @Composable
    fun Content(pagerState: PagerState, pageCount: Int, modifier: Modifier)
}
