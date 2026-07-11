package com.example.composecarousellib.indicators

/**
 * Convenient factories for the shipped indicator strategies. Prefer instantiating the
 * indicator directly (e.g. `DotIndicator(activeColor = ...)`) when you need to tweak
 * defaults; use this object for demo/registry code paths.
 */
object CarouselIndicators {
    val dots: CarouselIndicator get() = DotIndicator()
    val worm: CarouselIndicator get() = WormIndicator()
    val pill: CarouselIndicator get() = ExpandingPillIndicator()
    val bar: CarouselIndicator get() = BarIndicator()
    val numbers: CarouselIndicator get() = NumbersIndicator()
}
