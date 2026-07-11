package com.example.composecarousellib.effects.shader

import android.graphics.RuntimeShader
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.example.composecarousellib.effects.CarouselEffect
import com.example.composecarousellib.internal.absPageOffset
import com.example.composecarousellib.internal.signedPageOffset
import org.intellij.lang.annotations.Language

/**
 * Liquid glass — refractive, chromatic-tinted distortion that intensifies as the
 * page leaves center. Inspired by iOS "liquid glass" UI treatments.
 */
class LiquidGlassEffect(
    private val maxDistortionPx: Float = 26f,
    private val edgeHighlight: Float = 0.35f,
) : CarouselEffect {
    override val name: String = "Liquid Glass"

    @Composable
    override fun rememberItemModifier(page: Int, pagerState: PagerState): Modifier {
        val shader = remember { RuntimeShader(SRC) }
        return Modifier.graphicsLayer {
            val abs = pagerState.absPageOffset(page).coerceIn(0f, 1f)
            val signed = pagerState.signedPageOffset(page).coerceIn(-1f, 1f)
            shader.setFloatUniform("resolution", size.width, size.height)
            shader.setFloatUniform("progress", abs)
            shader.setFloatUniform("direction", signed)
            shader.setFloatUniform("distortion", maxDistortionPx)
            shader.setFloatUniform("edgeHighlight", edgeHighlight)
            renderEffect = shader.toRenderEffect()
            clip = true
        }
    }

    private companion object {
        @Language("AGSL")
        const val SRC = """
            uniform shader content;
            uniform float2 resolution;
            uniform float progress;
            uniform float direction;
            uniform float distortion;
            uniform float edgeHighlight;

            half4 main(float2 fragCoord) {
                float2 uv = fragCoord / resolution;
                float2 centered = uv - 0.5;

                // Radial bulge that lifts and refracts as we drag away from center.
                float dist = length(centered);
                float bulge = smoothstep(0.55, 0.0, dist) * progress;
                float2 refract = normalize(centered + float2(0.0001)) * bulge * distortion;

                // Sample base + chromatic split for the "glass" tint.
                half4 baseColor = content.eval(fragCoord - refract);
                half4 redSample = content.eval(fragCoord - refract * 1.05);
                half4 blueSample = content.eval(fragCoord - refract * 0.95);
                half3 tinted = half3(redSample.r, baseColor.g, blueSample.b);

                // Rim highlight — mimics the specular streak on real glass.
                float rim = smoothstep(0.35, 0.5, dist) * (1.0 - smoothstep(0.5, 0.6, dist));
                tinted += half3(rim * edgeHighlight);

                return half4(tinted, baseColor.a);
            }
        """
    }
}
