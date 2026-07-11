package com.example.composecarousellib.effects

import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.zIndex
import com.example.composecarousellib.internal.signedPageOffset
import kotlin.math.abs

/**
 * Tinder-style deck swipe. Feels like holding a stack of cards and flicking them off
 * to either side.
 *
 * - The top card rotates and translates in the drag direction (works both L→R and R→L).
 * - The next card underneath is scaled slightly down; it smoothly rises to full size
 *   as the top card is thrown off.
 * - A second card further behind peeks at the very bottom for visible deck depth.
 */
class TinderEffect(
    private val throwRotationDeg: Float = 24f,
    private val throwXExtraFraction: Float = 0.3f,
    private val throwYFraction: Float = 0.08f,
    private val underneathScaleStep: Float = 0.06f,
    private val underneathLiftPx: Float = 16f,
    private val visibleDepth: Int = 3,
) : CarouselEffect {
    override val name: String = "Tinder"

    override fun buildItemModifier(page: Int, pagerState: PagerState): Modifier {
        val offset = pagerState.signedPageOffset(page)
        val abs = abs(offset)
        return Modifier
            .zIndex(-abs)
            .graphicsLayer {
                when {
                    page == pagerState.currentPage -> {
                        // The top card — being thrown off in the drag direction.
                        // offset < 0 → dragging forward (left). offset > 0 → dragging back (right).
                        rotationZ = offset * throwRotationDeg
                        // Add extra horizontal throw on top of the pager's own translation.
                        translationX = -offset * size.width * throwXExtraFraction
                        translationY = -abs * size.height * throwYFraction
                        alpha = (1f - abs.coerceAtMost(1f) * 0.5f).coerceAtLeast(0f)
                        transformOrigin = TransformOrigin(0.5f, 1f)
                    }
                    offset > 0f -> {
                        // Cards ahead of the top — stacked behind.
                        val depth = offset.coerceAtMost(visibleDepth.toFloat())
                        translationX = -offset * size.width
                        val scale = 1f - underneathScaleStep * depth
                        scaleX = scale
                        scaleY = scale
                        translationY = underneathLiftPx * density * depth
                        alpha = if (depth >= visibleDepth) 0f else 1f - (depth / (visibleDepth + 1f)) * 0.4f
                    }
                    else -> {
                        // Past cards — already thrown off.
                        alpha = 0f
                    }
                }
            }
    }
}
