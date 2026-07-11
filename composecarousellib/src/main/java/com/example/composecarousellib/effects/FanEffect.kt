package com.example.composecarousellib.effects

import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import com.example.composecarousellib.internal.signedPageOffset
import kotlin.math.abs

/**
 * Fan / poker-hand spread — neighbors rotate around the bottom-center, fanning out.
 */
class FanEffect(
    private val maxRotationDeg: Float = 25f,
    private val liftPx: Float = 40f,
) : CarouselEffect {
    override val name: String = "Fan"

    override fun buildItemModifier(page: Int, pagerState: PagerState): Modifier =
        Modifier.graphicsLayer {
            val offset = pagerState.signedPageOffset(page).coerceIn(-1f, 1f)
            rotationZ = offset * maxRotationDeg
            translationY = abs(offset) * liftPx * density
            transformOrigin = TransformOrigin(0.5f, 1f)
        }
}
