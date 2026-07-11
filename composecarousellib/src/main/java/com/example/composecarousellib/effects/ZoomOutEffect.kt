package com.example.composecarousellib.effects

import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.util.lerp
import com.example.composecarousellib.internal.absPageOffset

/**
 * Zoom-out — both neighbors shrink and fade symmetrically as they leave center.
 */
class ZoomOutEffect(
    private val minScale: Float = 0.85f,
    private val minAlpha: Float = 0.5f,
) : CarouselEffect {
    override val name: String = "Zoom Out"

    override fun buildItemModifier(page: Int, pagerState: PagerState): Modifier =
        Modifier.graphicsLayer {
            val offset = pagerState.absPageOffset(page).coerceIn(0f, 1f)
            val scale = lerp(minScale, 1f, 1f - offset)
            scaleX = scale
            scaleY = scale
            alpha = lerp(minAlpha, 1f, 1f - offset)
        }
}
