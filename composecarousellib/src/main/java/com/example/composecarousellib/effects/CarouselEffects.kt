package com.example.composecarousellib.effects

import com.example.composecarousellib.effects.shader.ChromaticAberrationEffect
import com.example.composecarousellib.effects.shader.GlitchEffect
import com.example.composecarousellib.effects.shader.MorphEffect
import com.example.composecarousellib.effects.shader.MotionBlurEffect
import com.example.composecarousellib.effects.shader.NoiseDissolveEffect
import com.example.composecarousellib.effects.shader.PageCurlEffect
import com.example.composecarousellib.effects.shader.PixelateEffect
import com.example.composecarousellib.effects.shader.WaterRippleEffect

/**
 * Convenient registry of every effect the library ships. Ordering is intentional:
 * the flashy/premium effects come first so demo pickers naturally surface them, with
 * everyday effects clustered at the end.
 */
object CarouselEffects {

    /** Shader-driven premium effects — the ones users notice at first glance. */
    val fancy: List<CarouselEffect> = listOf(
        WaterRippleEffect(),
        PageCurlEffect(),
        MorphEffect(),
        GlitchEffect(),
        ChromaticAberrationEffect(),
        NoiseDissolveEffect(),
        PixelateEffect(),
        MotionBlurEffect(),
    )

    /** 3D / card-deck geometric effects — still eye-catching but graphicsLayer only. */
    val geometry3d: List<CarouselEffect> = listOf(
        CoverFlowEffect(),
        Cube3DEffect(),
        StackEffect(),
        Flip3DEffect(),
        BookFoldEffect(),
        FanEffect(),
    )

    /** Common / everyday effects — safe defaults for most apps. */
    val common: List<CarouselEffect> = listOf(
        AccordionEffect(),
        ElasticEffect(),
        RotateEffect(),
        ParallaxEffect(),
        DepthEffect(),
        ZoomOutEffect(),
        FadeEffect(),
        ScaleEffect(),
        NoEffect,
    )

    /**
     * All effects. Ordering surfaces the most impressive-feeling effects first —
     * WaterRipple leads, Tinder rides in the #2 slot as a hero deck-swipe, and the
     * rest of the fancy shader stack follows before the calmer 3D and common effects.
     */
    val all: List<CarouselEffect> = buildList {
        add(WaterRippleEffect())
        add(TinderEffect())
        addAll(fancy.drop(1))
        addAll(geometry3d)
        addAll(common)
    }
}
