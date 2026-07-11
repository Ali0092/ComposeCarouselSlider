package com.example.composecarousellib.effects

import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.zIndex
import com.example.composecarousellib.internal.absPageOffset
import com.example.composecarousellib.internal.signedPageOffset

/**
 * Z-axis rotation. Neighbors tilt away and sit behind the center page (zIndex).
 */
class RotateEffect(
    private val maxRotationDeg: Float = 18f,
    private val minScale: Float = 0.85f,
) : CarouselEffect {
    override val name: String = "Rotate"

    override fun buildItemModifier(page: Int, pagerState: PagerState): Modifier {
        val absOff = pagerState.absPageOffset(page).coerceIn(0f, 2f)
        return Modifier
            .zIndex(1f - absOff)
            .graphicsLayer {
                val offset = pagerState.signedPageOffset(page).coerceIn(-1f, 1f)
                rotationZ = offset * maxRotationDeg
                val scale = 1f - (1f - minScale) * absOff.coerceIn(0f, 1f)
                scaleX = scale
                scaleY = scale
            }
    }
}
