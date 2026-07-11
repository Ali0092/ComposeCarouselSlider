package com.example.composecarousellib.effects

import com.example.composecarousellib.effects.shader.ChromaticAberrationEffect
import com.example.composecarousellib.effects.shader.GlitchEffect
import com.example.composecarousellib.effects.shader.LiquidGlassEffect
import com.example.composecarousellib.effects.shader.MorphEffect
import com.example.composecarousellib.effects.shader.MotionBlurEffect
import com.example.composecarousellib.effects.shader.NoiseDissolveEffect
import com.example.composecarousellib.effects.shader.PageCurlEffect
import com.example.composecarousellib.effects.shader.PixelateEffect
import com.example.composecarousellib.effects.shader.WaterRippleEffect
import com.example.composecarousellib.effects.shader.WaveDistortionEffect

/**
 * Convenient registry of every effect the library ships. Ordering is intentional:
 * the flashy/premium effects come first so demo pickers naturally surface them, with
 * everyday effects clustered at the end.
 */
object CarouselEffects {

    /** Shader-driven premium effects — the ones users notice at first glance. */
    val fancy: List<CarouselEffect> = listOf(
        LiquidGlassEffect(),
        WaterRippleEffect(),
        PageCurlEffect(),
        MorphEffect(),
        GlitchEffect(),
        WaveDistortionEffect(),
        ChromaticAberrationEffect(),
        NoiseDissolveEffect(),
        PixelateEffect(),
        MotionBlurEffect(),
    )

    /** 3D / card-deck geometric effects — still eye-catching but graphicsLayer only. */
    val geometry3d: List<CarouselEffect> = listOf(
        CoverFlowEffect(),
        Cube3DEffect(),
        TinderEffect(),
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

    /** All effects: fancy → 3D → common → none. */
    val all: List<CarouselEffect> = fancy + geometry3d + common
}
