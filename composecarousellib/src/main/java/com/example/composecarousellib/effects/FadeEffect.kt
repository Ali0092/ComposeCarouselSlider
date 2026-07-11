package com.example.composecarousellib.effects

import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.example.composecarousellib.internal.absPageOffset
import com.example.composecarousellib.internal.signedPageOffset

/**
 * Cross-fade — pages don't slide, they only fade in and out.
 */
class FadeEffect : CarouselEffect {
    override val name: String = "Fade"

    override fun buildItemModifier(page: Int, pagerState: PagerState): Modifier =
        Modifier.graphicsLayer {
            val offset = pagerState.signedPageOffset(page)
            val absOffset = pagerState.absPageOffset(page).coerceIn(0f, 1f)
            translationX = -size.width * offset
            alpha = 1f - absOffset
        }
}
