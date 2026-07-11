package com.example.composecarousellib.effects

import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import com.example.composecarousellib.internal.signedPageOffset

/**
 * 3D cube — pages rotate around the leading edge, like faces of a cube.
 */
class Cube3DEffect(
    private val maxRotationDeg: Float = 90f,
    private val cameraDistance: Float = 20f,
) : CarouselEffect {
    override val name: String = "Cube 3D"

    override fun buildItemModifier(page: Int, pagerState: PagerState): Modifier =
        Modifier.graphicsLayer {
            val offset = pagerState.signedPageOffset(page).coerceIn(-1f, 1f)
            this.cameraDistance = cameraDistance * density
            rotationY = -offset * maxRotationDeg
            transformOrigin = TransformOrigin(pivotFractionX = if (offset > 0f) 0f else 1f, pivotFractionY = 0.5f)
        }
}
