package com.example.composecarousellib.effects.shader

import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.graphicsLayer
import com.example.composecarousellib.effects.CarouselEffect
import com.example.composecarousellib.internal.absPageOffset

/**
 * Directional motion blur that ramps up mid-swipe and clears at rest. Uses the
 * built-in Compose [BlurEffect] rather than a custom AGSL shader — faster and
 * more battery friendly.
 */
class MotionBlurEffect(
    private val maxBlurPx: Float = 24f,
) : CarouselEffect {
    override val name: String = "Motion Blur"

    override fun buildItemModifier(page: Int, pagerState: PagerState): Modifier =
        Modifier.graphicsLayer {
            val abs = pagerState.absPageOffset(page).coerceIn(0f, 1f)
            val blur = maxBlurPx * abs
            renderEffect = if (blur > 0.5f) BlurEffect(blur, blur * 0.15f, TileMode.Decal) else null
            clip = true
        }
}
