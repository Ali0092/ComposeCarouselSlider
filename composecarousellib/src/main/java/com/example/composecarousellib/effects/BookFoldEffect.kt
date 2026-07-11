package com.example.composecarousellib.effects

import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import com.example.composecarousellib.internal.signedPageOffset

/**
 * Book fold — pages rotate around the trailing edge, like turning book pages.
 */
class BookFoldEffect(
    private val maxRotationDeg: Float = 90f,
    private val cameraDistance: Float = 16f,
) : CarouselEffect {
    override val name: String = "Book Fold"

    override fun buildItemModifier(page: Int, pagerState: PagerState): Modifier =
        Modifier.graphicsLayer {
            val offset = pagerState.signedPageOffset(page).coerceIn(-1f, 1f)
            this.cameraDistance = cameraDistance * density
            rotationY = offset * maxRotationDeg
            transformOrigin = TransformOrigin(if (offset > 0f) 1f else 0f, 0.5f)
            alpha = 1f - kotlin.math.abs(offset).coerceIn(0f, 1f)
        }
}
