package com.example.composecarousellib.effects

import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.example.composecarousellib.internal.signedPageOffset

/**
 * Parallax — the page slides slower than the pager itself, creating depth.
 *
 * @param parallaxFactor 0f = no parallax (pages slide at pager speed).
 * 1f = pages don't visually move relative to the viewport.
 */
class ParallaxEffect(
    private val parallaxFactor: Float = 0.4f,
) : CarouselEffect {
    override val name: String = "Parallax"

    override fun buildItemModifier(page: Int, pagerState: PagerState): Modifier =
        Modifier.graphicsLayer {
            val offset = pagerState.signedPageOffset(page)
            translationX = size.width * offset * parallaxFactor
        }
}
