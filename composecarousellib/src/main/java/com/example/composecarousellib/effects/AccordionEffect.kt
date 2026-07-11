package com.example.composecarousellib.effects

import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.util.lerp
import com.example.composecarousellib.internal.signedPageOffset

/**
 * Accordion — pages compress horizontally against the leading edge, like folding paper.
 */
class AccordionEffect(
    private val minScaleX: Float = 0f,
) : CarouselEffect {
    override val name: String = "Accordion"

    override fun buildItemModifier(page: Int, pagerState: PagerState): Modifier =
        Modifier.graphicsLayer {
            val offset = pagerState.signedPageOffset(page).coerceIn(-1f, 1f)
            transformOrigin = TransformOrigin(if (offset < 0f) 0f else 1f, 0.5f)
            scaleX = lerp(minScaleX, 1f, 1f - kotlin.math.abs(offset))
        }
}
