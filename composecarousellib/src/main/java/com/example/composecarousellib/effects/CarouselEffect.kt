package com.example.composecarousellib.effects

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A pluggable per-page transformation for [com.example.composecarousellib.CarouselSlider].
 *
 * An effect is a pure strategy: given the page index and the current [PagerState] it
 * returns a [Modifier] to apply to the page's item. Effects should be stateless and
 * cheap; anything expensive belongs in remembered state on the slider itself.
 *
 * Implementations that need custom drawing (shader, canvas overlays) can optionally
 * provide [backgroundContent] and [foregroundContent], which are laid on top of the
 * item's own drawing behind/in front of the page respectively.
 */
interface CarouselEffect {
    /** Stable, user-facing name — used by the demo chip picker and for logs. */
    val name: String

    /**
     * Stateless modifier applied to the pager item root. Suitable for graphicsLayer
     * transforms that only need [page] and [pagerState].
     * Default: no-op.
     */
    fun buildItemModifier(page: Int, pagerState: PagerState): Modifier = Modifier

    /**
     * Composable variant that can `remember` per-item resources such as [android.graphics.RuntimeShader].
     * Default: delegates to [buildItemModifier].
     */
    @Composable
    fun rememberItemModifier(page: Int, pagerState: PagerState): Modifier =
        buildItemModifier(page, pagerState)

    /** Composable drawn behind the item content. Default: nothing. */
    @Composable
    fun BackgroundContent(page: Int, pagerState: PagerState) {}

    /** Composable drawn over the item content. Default: nothing. */
    @Composable
    fun ForegroundContent(page: Int, pagerState: PagerState) {}
}

/** No-op effect — pages just slide. */
object NoEffect : CarouselEffect {
    override val name: String = "None"
}
