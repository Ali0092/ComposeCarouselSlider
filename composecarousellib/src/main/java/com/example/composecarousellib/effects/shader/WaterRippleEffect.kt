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
 * Concentric water ripples emanating from the page center as it swipes.
 */
class WaterRippleEffect(
    private val amplitudePx: Float = 14f,
    private val frequency: Float = 22f,
) : CarouselEffect {
    override val name: String = "Water Ripple"

    @Composable
    override fun rememberItemModifier(page: Int, pagerState: PagerState): Modifier {
        val shader = remember { RuntimeShader(SRC) }
        return Modifier.graphicsLayer {
            val progress = pagerState.absPageOffset(page).coerceIn(0f, 1f)
            shader.setFloatUniform("resolution", size.width, size.height)
            shader.setFloatUniform("progress", progress)
            shader.setFloatUniform("amplitude", amplitudePx)
            shader.setFloatUniform("frequency", frequency)
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
            uniform float amplitude;
            uniform float frequency;

            half4 main(float2 fragCoord) {
                float2 uv = fragCoord / resolution;
                float2 centered = uv - 0.5;
                float dist = length(centered);
                float wave = sin(dist * frequency - progress * 12.0);
                float falloff = smoothstep(0.75, 0.0, dist) * progress;
                float2 offset = normalize(centered + float2(0.0001)) * wave * amplitude * falloff;
                return content.eval(fragCoord + offset);
            }
        """
    }
}
