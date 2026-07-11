package com.example.composecarousellib.effects

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A pluggable per-page transformation for [com.example.composecarousellib.CarouselSlider].
 *
 * Effects are strategies: given the page index and current [PagerState] they return
 * a [Modifier] applied to the page item, and can optionally render:
 * - a full-slider [Backdrop] behind all pages (a blurred image, a shader-driven
 *   full-bleed layer, etc.);
 * - per-page [BackgroundContent] / [ForegroundContent] behind/above the page content.
 *
 * Effects that need `remember`-scoped resources (a [android.graphics.RuntimeShader],
 * animatable state, coroutine scopes) should override [rememberItemModifier] rather
 * than [buildItemModifier].
 */
interface CarouselEffect {
    /** Stable, user-facing name. */
    val name: String

    /**
     * If true, the slider draws [Backdrop] behind the pager and stops clipping the
     * pager to a fixed card, so full-bleed effects (blurred backdrop, water surface)
     * can render across the whole slider area.
     */
    val prefersBackdrop: Boolean get() = false

    /** Stateless per-page modifier. Default: no-op. */
    fun buildItemModifier(page: Int, pagerState: PagerState): Modifier = Modifier

    /** Composable per-page modifier; can `remember` per-item state. */
    @Composable
    fun rememberItemModifier(page: Int, pagerState: PagerState): Modifier =
        buildItemModifier(page, pagerState)

    /**
     * Full-slider layer drawn behind the pager. Only rendered when [prefersBackdrop]
     * is true.
     *
     * @param renderImage a callback that renders the image at [index] with the given
     * [Modifier]. Effects use this to draw the current (or blurred, or ripple-warped)
     * image as their backdrop without knowing the concrete image type.
     */
    @Composable
    fun Backdrop(
        pagerState: PagerState,
        pageCount: Int,
        renderImage: @Composable (index: Int, modifier: Modifier) -> Unit,
    ) {}

    /** Composable drawn behind the item content. */
    @Composable
    fun BackgroundContent(page: Int, pagerState: PagerState) {}

    /** Composable drawn above the item content. */
    @Composable
    fun ForegroundContent(page: Int, pagerState: PagerState) {}
}

/** No-op effect — pages just slide. */
object NoEffect : CarouselEffect {
    override val name: String = "None"
}
