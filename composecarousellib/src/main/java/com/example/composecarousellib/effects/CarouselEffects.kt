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
 * Convenient registry of every effect the library ships. Useful for demo pickers
 * and for programmatic iteration over effects.
 */
object CarouselEffects {

    val geometry: List<CarouselEffect> = listOf(
        NoEffect,
        ScaleEffect(),
        DepthEffect(),
        ZoomOutEffect(),
        FadeEffect(),
        ParallaxEffect(),
        CoverFlowEffect(),
        Cube3DEffect(),
        StackEffect(),
        FanEffect(),
        RotateEffect(),
        TinderEffect(),
        Flip3DEffect(),
        AccordionEffect(),
        BookFoldEffect(),
        ElasticEffect(),
    )

    val shader: List<CarouselEffect> = listOf(
        LiquidGlassEffect(),
        WaterRippleEffect(),
        WaveDistortionEffect(),
        MotionBlurEffect(),
        PageCurlEffect(),
        PixelateEffect(),
        GlitchEffect(),
        NoiseDissolveEffect(),
        ChromaticAberrationEffect(),
        MorphEffect(),
    )

    val all: List<CarouselEffect> get() = geometry + shader
}
