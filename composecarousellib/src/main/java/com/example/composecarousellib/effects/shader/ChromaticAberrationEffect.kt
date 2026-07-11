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
 * Chromatic aberration — RGB channels drift apart at the edges, most visible mid-swipe.
 */
class ChromaticAberrationEffect(
    private val maxOffsetPx: Float = 18f,
) : CarouselEffect {
    override val name: String = "Chromatic Split"

    @Composable
    override fun rememberItemModifier(page: Int, pagerState: PagerState): Modifier {
        val shader = remember { RuntimeShader(SRC) }
        return Modifier.graphicsLayer {
            val absOff = pagerState.absPageOffset(page).coerceIn(0f, 1f)
            val dir = pagerState.signedPageOffset(page).coerceIn(-1f, 1f)
            shader.setFloatUniform("resolution", size.width, size.height)
            shader.setFloatUniform("offsetPx", maxOffsetPx * absOff * dir)
            renderEffect = shader.toRenderEffect()
            clip = true
        }
    }

    private companion object {
        @Language("AGSL")
        const val SRC = """
            uniform shader content;
            uniform float2 resolution;
            uniform float offsetPx;

            half4 main(float2 fragCoord) {
                float2 uv = fragCoord / resolution;
                float2 centered = uv - 0.5;
                float radial = length(centered) * 2.0;
                float2 dir = normalize(centered + float2(0.0001));
                float scale = radial * offsetPx;

                half4 r = content.eval(fragCoord - dir * scale);
                half4 g = content.eval(fragCoord);
                half4 b = content.eval(fragCoord + dir * scale);
                return half4(r.r, g.g, b.b, g.a);
            }
        """
    }
}
