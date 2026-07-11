package com.example.composecarousellib.effects

import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.util.lerp
import com.example.composecarousellib.internal.signedPageOffset
import kotlin.math.abs

/**
 * Depth (out) — the ViewPager2 classic. The outgoing page fades and scales down
 * while the incoming page slides in front over it.
 */
class DepthEffect(
    private val minScale: Float = 0.75f,
    private val minAlpha: Float = 0f,
) : CarouselEffect {
    override val name: String = "Depth"

    override fun buildItemModifier(page: Int, pagerState: PagerState): Modifier =
        Modifier.graphicsLayer {
            val offset = pagerState.signedPageOffset(page)
            val absOffset = abs(offset)
            when {
                absOffset >= 1f -> alpha = 0f
                offset <= 0f -> {
                    // Incoming from left: slide in as normal
                    alpha = 1f
                }
                else -> {
                    // Outgoing to the right: fade + scale down, cancel translation
                    val scale = lerp(minScale, 1f, 1f - absOffset)
                    alpha = lerp(minAlpha, 1f, 1f - absOffset)
                    scaleX = scale
                    scaleY = scale
                    translationX = -size.width * offset
                    transformOrigin = TransformOrigin.Center
                }
            }
        }
}
