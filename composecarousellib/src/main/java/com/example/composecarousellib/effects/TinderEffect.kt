package com.example.composecarousellib.effects

import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import com.example.composecarousellib.internal.signedPageOffset

/**
 * Tinder-style swipe. Outgoing page rotates and translates as if being thrown off,
 * next page underneath is prescaled up to full size.
 */
class TinderEffect(
    private val throwRotationDeg: Float = 22f,
    private val underneathScaleFrom: Float = 0.92f,
) : CarouselEffect {
    override val name: String = "Tinder"

    override fun buildItemModifier(page: Int, pagerState: PagerState): Modifier =
        Modifier.graphicsLayer {
            val offset = pagerState.signedPageOffset(page)
            when {
                offset < 0f -> {
                    // Outgoing — thrown off, rotates and translates
                    val progress = (-offset).coerceIn(0f, 1f)
                    rotationZ = -throwRotationDeg * progress
                    translationX = -size.width * progress
                    translationY = -size.height * 0.15f * progress
                    transformOrigin = TransformOrigin(0.5f, 1f)
                    alpha = 1f - progress
                }
                offset in 0f..1f -> {
                    // Next card underneath — scale up as we swipe the top away
                    val progress = 1f - offset
                    val scale = underneathScaleFrom + (1f - underneathScaleFrom) * progress
                    scaleX = scale
                    scaleY = scale
                }
                else -> {
                    alpha = 0f
                }
            }
        }
}
