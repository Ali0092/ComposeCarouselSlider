package com.example.composecarousellib.effects

import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.util.lerp
import com.example.composecarousellib.internal.signedPageOffset
import kotlin.math.abs

/**
 * iTunes / Apple cover-flow. Pages rotate around the Y axis and tilt into perspective
 * as they leave center. Center page is upright, neighbors lean back.
 */
class CoverFlowEffect(
    private val maxRotationDeg: Float = 45f,
    private val cameraDistance: Float = 12f,
    private val minScale: Float = 0.8f,
    private val overlap: Float = 0.25f,
) : CarouselEffect {
    override val name: String = "Cover Flow"

    override fun buildItemModifier(page: Int, pagerState: PagerState): Modifier =
        Modifier.graphicsLayer {
            val offset = pagerState.signedPageOffset(page).coerceIn(-1f, 1f)
            val absOffset = abs(offset)
            rotationY = -offset * maxRotationDeg
            this.cameraDistance = cameraDistance * density
            val scale = lerp(minScale, 1f, 1f - absOffset)
            scaleX = scale
            scaleY = scale
            translationX = -offset * size.width * overlap
            transformOrigin = TransformOrigin(if (offset < 0f) 1f else 0f, 0.5f)
        }
}
