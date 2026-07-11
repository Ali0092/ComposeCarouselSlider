# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project overview

Jetpack Compose carousel library published to JitPack as `com.github.Ali0092:ComposeCarouselSlider`. The repo is a two-module Gradle build:

- `composecarousellib/` — the published Android library (`com.example.composecarousellib`). This is the artifact users depend on; keep public API stable.
- `app/` — a demo app that exercises every effect via a chip picker (`MainActivity.CarouselGallery`). Not published.

## Build & run

```sh
./gradlew :composecarousellib:assembleRelease   # build the library AAR
./gradlew :app:installDebug                     # install the demo on a connected device
./gradlew build                                 # full build across modules
```

There are no unit/instrumented tests in the repo yet, so `./gradlew test` is a no-op.

`minSdk = 33` is a hard requirement, not a preference: shader effects use `android.graphics.RuntimeShader` (AGSL), which is API 33+. Do not lower it.

## Architecture

The slider is a thin host built around two strategy interfaces. Understanding this split is the "big picture" — most work in this repo is either implementing a new strategy or tweaking the host.

### Host: `CarouselSlider` / `CarouselSliderContent` (`CarouselSlider.kt`)

- Owns `PagerState`, the auto-scroll `LaunchedEffect`, and the `HorizontalPager` layout.
- Two entry points:
  - `CarouselSlider(items: List<CarouselImage>, …)` — the image convenience API. `CarouselImage` is a sealed class covering `Resource`, `Url` (Coil), `AsImageBitmap`, and `Custom(Painter)`.
  - `CarouselSliderContent(pageCount, page = { … }, renderImage = { … }, …)` — slot-based for non-image content. Callers pass both the per-page composable AND a `renderImage` callback so effects can draw the "current image" in their backdrop without knowing the concrete image type.
- Each page is wrapped `BackgroundContent` → `page(index)` → `ForegroundContent`, with the effect's `rememberItemModifier` applied to the page `Box`. When `effect.prefersBackdrop` is true, `effect.Backdrop(...)` renders behind the pager and the pager stops clipping to a fixed card — full-bleed shader effects need this.
- `pageHeight: Dp? = null` — when null the pager fills its parent (backdrop effects need this); pass a concrete Dp for a card-style height.

### Effects: `CarouselEffect` (`effects/CarouselEffect.kt`)

Pluggable per-page transformation. Every effect is a class implementing this interface. The four extension points, in order of complexity:

1. `buildItemModifier(page, pagerState)` — stateless per-page `Modifier`. Most `graphicsLayer`-only effects (Scale, Rotate, ZoomOut, Depth, CoverFlow, Cube3D, Stack, Tinder…) only override this.
2. `rememberItemModifier(page, pagerState)` — composable version; use when you need `remember`-scoped resources like a `RuntimeShader`, `Animatable`, or a coroutine scope. All shader effects override this.
3. `BackgroundContent` / `ForegroundContent` — per-page composables drawn behind/above the page.
4. `prefersBackdrop` + `Backdrop(pagerState, pageCount, renderImage)` — full-slider layer. Set the flag and implement `Backdrop` to draw a blurred image, warped surface, etc. across the whole slider.

Effects live under `effects/` (graphicsLayer-based) and `effects/shader/` (AGSL `RuntimeShader`-based). Shader effects use the `RuntimeShader.toRenderEffect()` helper in `effects/shader/ShaderSupport.kt` to wrap AGSL into a Compose `RenderEffect`, then bind uniforms inside `graphicsLayer { … }`.

`CarouselEffects` (`effects/CarouselEffects.kt`) is the public registry — three grouped lists (`fancy`, `geometry3d`, `common`) plus a curated `all` list. **Ordering in `all` is intentional**: the most impressive effects lead so demo pickers surface them first. When adding a new effect, register it in the appropriate list; changing the head of `all` is a visible product decision, not a mechanical edit.

### Indicators: `CarouselIndicator` (`indicators/CarouselIndicator.kt`)

`fun interface` — a stateless composable driven by `PagerState`. `ExpandingPillIndicator` is the default. Slider passes `indicator: CarouselIndicator? = ExpandingPillIndicator()`; pass `null` to hide.

### Shared math: `internal/PagerMath.kt`

`PagerState.signedPageOffset(page)` and `.absPageOffset(page)` give the continuous drag-inclusive offset from `currentPage`. Effects should key off these — **not** `page == currentPage` — so animations stay smooth mid-swipe (see the TinderEffect kdoc for a worked example of why this matters).

## Conventions when adding an effect

- Prefer overriding `buildItemModifier` unless you genuinely need `remember`. Stateless effects are cheaper and easier to reason about.
- Drive animations off `signedPageOffset` / `absPageOffset`, not discrete page equality.
- For shader effects: define the AGSL in a `companion object` `const val SRC` annotated with `@Language("AGSL")`, set uniforms inside `graphicsLayer { … }` (where `size` is available), and apply via `renderEffect = shader.toRenderEffect()`.
- Register the new effect in the appropriate `CarouselEffects` list.
- Give it a stable `name: String` — this is what shows up in the demo chip picker and any user-facing UI.
