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
 * Horizontal sine-wave displacement — like the image is a flag rippling as it swipes.
 */
class WaveDistortionEffect(
    private val amplitudePx: Float = 24f,
    private val frequency: Float = 6f,
) : CarouselEffect {
    override val name: String = "Wave"

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
                float dx = sin(uv.y * frequency + progress * 6.0) * amplitude * progress;
                float dy = cos(uv.x * frequency + progress * 4.0) * amplitude * 0.5 * progress;
                return content.eval(fragCoord + float2(dx, dy));
            }
        """
    }
}
