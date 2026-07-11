package com.example.composecarousellib.effects

import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.example.composecarousellib.internal.absPageOffset
import kotlin.math.sin

/**
 * Elastic — the page squishes vertically as it leaves center, then bounces back.
 */
class ElasticEffect(
    private val stretchFactor: Float = 0.18f,
) : CarouselEffect {
    override val name: String = "Elastic"

    override fun buildItemModifier(page: Int, pagerState: PagerState): Modifier =
        Modifier.graphicsLayer {
            val offset = pagerState.absPageOffset(page).coerceIn(0f, 1f)
            val stretch = sin(offset * Math.PI).toFloat() * stretchFactor
            scaleY = 1f - stretch
            scaleX = 1f + stretch
        }
}
