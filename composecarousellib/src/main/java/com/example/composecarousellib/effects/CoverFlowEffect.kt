package com.example.composecarousellib.effects

import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.util.lerp
import androidx.compose.ui.zIndex
import com.example.composecarousellib.internal.absPageOffset
import com.example.composecarousellib.internal.signedPageOffset
import kotlin.math.abs

/**
 * Apple Cover-Flow. Neighbor pages rotate on Y-axis and slide partially behind the
 * front page. zIndex ensures the current page always draws on top.
 */
class CoverFlowEffect(
    private val maxRotationDeg: Float = 55f,
    private val cameraDistance: Float = 14f,
    private val minScale: Float = 0.78f,
    private val neighborOverlap: Float = 0.35f,
) : CarouselEffect {
    override val name: String = "Cover Flow"

    override fun buildItemModifier(page: Int, pagerState: PagerState): Modifier {
        val absOff = pagerState.absPageOffset(page).coerceIn(0f, 2f)
        return Modifier
            .zIndex(1f - absOff)
            .graphicsLayer {
                val offset = pagerState.signedPageOffset(page).coerceIn(-2f, 2f)
                val abs = abs(offset)
                rotationY = -offset.coerceIn(-1f, 1f) * maxRotationDeg
                this.cameraDistance = cameraDistance * density
                val scale = lerp(minScale, 1f, 1f - abs.coerceIn(0f, 1f))
                scaleX = scale
                scaleY = scale
                // Pull neighbors inward so they tuck behind the front page.
                translationX = -offset * size.width * neighborOverlap
                transformOrigin = TransformOrigin(if (offset < 0f) 1f else 0f, 0.5f)
            }
    }
}
