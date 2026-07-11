package com.example.composecarousellib.effects

import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.example.composecarousellib.internal.signedPageOffset
import kotlin.math.abs

/**
 * 3D flip — pages flip in place around the Y axis. Back-face gets hidden.
 */
class Flip3DEffect(
    private val cameraDistance: Float = 20f,
) : CarouselEffect {
    override val name: String = "Flip 3D"

    override fun buildItemModifier(page: Int, pagerState: PagerState): Modifier =
        Modifier.graphicsLayer {
            val offset = pagerState.signedPageOffset(page).coerceIn(-1f, 1f)
            this.cameraDistance = cameraDistance * density
            rotationY = 180f * offset
            translationX = -offset * size.width
            alpha = if (abs(offset) > 0.5f) 0f else 1f
        }
}
