package com.example.composecarousellib.effects

import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.util.lerp
import com.example.composecarousellib.internal.absPageOffset

/**
 * The original ComposeCarouselSlider v1 look: neighboring pages shrink slightly.
 */
class ScaleEffect(
    private val minScale: Float = 0.80f,
    private val scaleAxis: Axis = Axis.Both,
) : CarouselEffect {
    override val name: String = "Scale"

    override fun buildItemModifier(page: Int, pagerState: PagerState): Modifier =
        Modifier.graphicsLayer {
            val offset = pagerState.absPageOffset(page).coerceIn(0f, 1f)
            val s = lerp(minScale, 1f, 1f - offset)
            when (scaleAxis) {
                Axis.X -> scaleX = s
                Axis.Y -> scaleY = s
                Axis.Both -> {
                    scaleX = s
                    scaleY = s
                }
            }
        }

    enum class Axis { X, Y, Both }
}
