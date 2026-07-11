package com.example.composecarousellib.effects

import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.util.lerp
import com.example.composecarousellib.internal.signedPageOffset

/**
 * Stacked cards — like a Tinder deck. Pages behind peek out from beneath the top one,
 * slightly scaled down and offset. The active page floats on top.
 */
class StackEffect(
    private val stackScaleStep: Float = 0.06f,
    private val stackOffsetStep: Float = 24f,
    private val visibleDepth: Int = 2,
) : CarouselEffect {
    override val name: String = "Stack"

    override fun buildItemModifier(page: Int, pagerState: PagerState): Modifier =
        Modifier.graphicsLayer {
            val offset = pagerState.signedPageOffset(page)
            when {
                offset > 0f -> {
                    // Behind the current page — stacked underneath
                    val depth = offset.coerceAtMost(visibleDepth.toFloat())
                    val scale = 1f - stackScaleStep * depth
                    scaleX = scale
                    scaleY = scale
                    translationY = -stackOffsetStep * density * depth
                    translationX = 0f
                    alpha = if (depth >= visibleDepth) 0f else 1f
                }
                offset < 0f -> {
                    // Already swiped away — slide off to the left
                    translationX = size.width * offset
                    alpha = (1f + offset * 2f).coerceIn(0f, 1f)
                }
                else -> {
                    // Current page — natural
                }
            }
        }
}
