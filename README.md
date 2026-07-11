# ComposeCarouselLib
[![](https://jitpack.io/v/Ali0092/ComposeCarouselSlider.svg)](https://jitpack.io/#Ali0092/ComposeCarouselSlider)

A Jetpack Compose carousel with pluggable page effects — Tinder deck swipe, water ripple, cover flow, page curl, and ~20 more. Swap effects at runtime, or write your own.

## Requirements

`minSdk = 33` (shader effects use AGSL `RuntimeShader`).

## Install

Add JitPack to your root `settings.gradle`:

```gradle
dependencyResolutionManagement {
    repositories {
        maven { url = uri("https://jitpack.io") }
    }
}
```

Then depend on the library:

```gradle
dependencies {
    implementation("com.github.Ali0092:ComposeCarouselSlider:2.0.1")
}
```

## Usage

```kotlin
val images = listOf(
    CarouselImage.Resource(R.drawable.image1),
    CarouselImage.Url("https://example.com/image2.jpg"),
)

CarouselSlider(
    items = images,
    effect = TinderEffect(),
    onItemClick = { index -> /* ... */ },
)
```

That's the whole API. Change `effect = ...` to switch styles; everything else has a sensible default.

## Effects

Pass any of these classes to the `effect` parameter — e.g. `effect = WaterRippleEffect()`.

**Shader-based (fancy)**
- `WaterRippleEffect()`
- `PageCurlEffect()`
- `MorphEffect()`
- `GlitchEffect()`
- `ChromaticAberrationEffect()`
- `NoiseDissolveEffect()`
- `PixelateEffect()`
- `MotionBlurEffect()`

**3D / card-deck**
- `TinderEffect()`
- `CoverFlowEffect()`
- `Cube3DEffect()`
- `StackEffect()`
- `Flip3DEffect()`
- `BookFoldEffect()`
- `FanEffect()`

**Everyday**
- `AccordionEffect()`
- `ElasticEffect()`
- `RotateEffect()`
- `ParallaxEffect()`
- `DepthEffect()`
- `ZoomOutEffect()`
- `FadeEffect()`
- `ScaleEffect()`
- `NoEffect` (default — pages just slide)

Or grab them all at once: `CarouselEffects.all` (curated order), `CarouselEffects.fancy`, `CarouselEffects.geometry3d`, `CarouselEffects.common`.

Roll your own by implementing `CarouselEffect` and overriding `buildItemModifier` (or `rememberItemModifier` if you need state).

## Parameters

| Name | Default | What it does |
| --- | --- | --- |
| `items` | required | `List<CarouselImage>` (Resource, Url, ImageBitmap, or Painter) |
| `showBackgroundImage` | `true` | Blurred full-bleed backdrop of the current image |
| `backgroundBlurRadius` | `40.dp` | Blur strength for the backdrop |
| `pageHeight` | `null` | Fixed card height; `null` fills parent |
| `contentPadding` | `32.dp` horizontal | Padding around the pager |
| `pageSpacing` | `8.dp` | Space between pages |
| `itemCornerRadius` | `20.dp` | Corner radius on the image card |
| `effect` | `NoEffect` | The page transform |
| `autoScroll` | `false` | Auto-advance through pages |
| `autoScrollDelayMs` | `3000` | Delay between auto-scrolls |
| `autoScrollAnimationSpec` | `spring()` | Auto-scroll animation |
| `pagerState` | remembered | Provide one to control the pager externally |
| `onItemClick` | `null` | Called with the tapped page index |

## Preview
<img src="https://github.com/user-attachments/assets/b5ed01c1-e3ed-429a-ae96-578423ec697a" alt="Carousel preview" width="40%">

## License

Apache 2.0 — see [LICENSE](LICENSE).

## Author

**Muhammad Ali** — [GitHub](https://github.com/Ali0092/) · [LinkedIn](https://www.linkedin.com/in/muhammad-ali-a28422222/)
