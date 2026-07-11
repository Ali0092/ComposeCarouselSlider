package com.example.composecarousellib.effects

import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.example.composecarousellib.internal.signedPageOffset

/**
 * Simple Z-axis rotation as the page moves off-center.
 */
class RotateEffect(
    private val maxRotationDeg: Float = 15f,
) : CarouselEffect {
    override val name: String = "Rotate"

    override fun buildItemModifier(page: Int, pagerState: PagerState): Modifier =
        Modifier.graphicsLayer {
            val offset = pagerState.signedPageOffset(page).coerceIn(-1f, 1f)
            rotationZ = offset * maxRotationDeg
        }
}
