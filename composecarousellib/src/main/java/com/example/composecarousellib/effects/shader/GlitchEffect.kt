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
 * VHS-style glitch — horizontal band displacement plus RGB channel split mid-swipe.
 */
class GlitchEffect(
    private val intensity: Float = 1f,
) : CarouselEffect {
    override val name: String = "Glitch"

    @Composable
    override fun rememberItemModifier(page: Int, pagerState: PagerState): Modifier {
        val shader = remember { RuntimeShader(SRC) }
        return Modifier.graphicsLayer {
            val progress = pagerState.absPageOffset(page).coerceIn(0f, 1f)
            shader.setFloatUniform("resolution", size.width, size.height)
            shader.setFloatUniform("progress", progress)
            shader.setFloatUniform("intensity", intensity)
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
            uniform float intensity;

            float hash(float2 p) {
                return fract(sin(dot(p, float2(12.9898, 78.233))) * 43758.5453);
            }

            half4 main(float2 fragCoord) {
                float band = floor(fragCoord.y / 12.0);
                float noise = hash(float2(band, floor(progress * 60.0)));
                float shift = (noise - 0.5) * 60.0 * progress * intensity;
                float2 shifted = fragCoord + float2(shift, 0.0);

                float split = 8.0 * progress * intensity;
                half4 r = content.eval(shifted + float2(split, 0.0));
                half4 g = content.eval(shifted);
                half4 b = content.eval(shifted - float2(split, 0.0));
                return half4(r.r, g.g, b.b, g.a);
            }
        """
    }
}
