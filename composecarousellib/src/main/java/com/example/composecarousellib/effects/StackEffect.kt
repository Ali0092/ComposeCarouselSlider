package com.example.composecarousellib.effects

import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.zIndex
import com.example.composecarousellib.internal.signedPageOffset
import kotlin.math.abs

/**
 * Photo-stack — the top card is fully visible; up to [visibleDepth] cards peek out
 * from behind, each scaled down and lifted, giving obvious depth. As the top card is
 * dragged away it lifts and tilts; the card behind rises to fill its place.
 */
class StackEffect(
    private val visibleDepth: Int = 3,
    private val scaleStepPerDepth: Float = 0.08f,
    private val liftPxPerDepth: Float = 24f,
    private val tiltDeg: Float = 6f,
    private val throwHeightFraction: Float = 0.18f,
) : CarouselEffect {
    override val name: String = "Stack"

    override fun buildItemModifier(page: Int, pagerState: PagerState): Modifier {
        val offset = pagerState.signedPageOffset(page)
        val abs = abs(offset)
        return Modifier
            .zIndex(-abs)
            .graphicsLayer {
                when {
                    page == pagerState.currentPage -> {
                        // Top card. On drag: lift up + soft tilt in drag direction.
                        rotationZ = offset * tiltDeg
                        translationY = -abs * size.height * throwHeightFraction
                        alpha = (1f - abs.coerceAtMost(1f)).coerceAtLeast(0f)
                        translationX = -offset * size.width * 0.1f
                    }
                    offset > 0f -> {
                        // Card ahead — visibly stacked behind, lifted and scaled down.
                        val depth = offset.coerceAtMost(visibleDepth.toFloat())
                        translationX = -offset * size.width
                        val scale = 1f - scaleStepPerDepth * depth
                        scaleX = scale
                        scaleY = scale
                        translationY = liftPxPerDepth * density * depth
                        alpha = if (depth >= visibleDepth) 0f else 1f - (depth / (visibleDepth + 1f)) * 0.3f
                    }
                    else -> {
                        // Discarded — invisible.
                        alpha = 0f
                    }
                }
            }
    }
}
