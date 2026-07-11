package com.example.composecarousellib.effects.shader

import android.graphics.RuntimeShader
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.example.composecarousellib.effects.CarouselEffect
import com.example.composecarousellib.internal.absPageOffset
import org.intellij.lang.annotations.Language

/**
 * Morph — a soft radial swirl that "smears" the outgoing page like paint on water.
 */
class MorphEffect(
    private val swirlStrength: Float = 3.0f,
) : CarouselEffect {
    override val name: String = "Morph"

    @Composable
    override fun rememberItemModifier(page: Int, pagerState: PagerState): Modifier {
        val shader = remember { RuntimeShader(SRC) }
        return Modifier.graphicsLayer {
            val progress = pagerState.absPageOffset(page).coerceIn(0f, 1f)
            shader.setFloatUniform("resolution", size.width, size.height)
            shader.setFloatUniform("progress", progress)
            shader.setFloatUniform("strength", swirlStrength)
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
            uniform float strength;

            half4 main(float2 fragCoord) {
                float2 uv = fragCoord / resolution;
                float2 centered = uv - 0.5;
                float dist = length(centered);
                float angle = strength * progress * (1.0 - smoothstep(0.0, 0.55, dist));
                float s = sin(angle);
                float c = cos(angle);
                float2 rotated = float2(centered.x * c - centered.y * s, centered.x * s + centered.y * c);
                float2 sampleUv = (rotated + 0.5) * resolution;
                return content.eval(sampleUv);
            }
        """
    }
}
