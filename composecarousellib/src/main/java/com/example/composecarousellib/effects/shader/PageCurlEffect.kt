package com.example.composecarousellib.effects.shader

import android.graphics.RuntimeShader
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.example.composecarousellib.effects.CarouselEffect
import com.example.composecarousellib.internal.signedPageOffset
import org.intellij.lang.annotations.Language

/**
 * A shader-approximated page curl. Not a true 3D mesh curl but a horizontally
 * gradient-driven fold with a highlighted crease and shadow.
 */
class PageCurlEffect : CarouselEffect {
    override val name: String = "Page Curl"

    @Composable
    override fun rememberItemModifier(page: Int, pagerState: PagerState): Modifier {
        val shader = remember { RuntimeShader(SRC) }
        return Modifier.graphicsLayer {
            val signed = pagerState.signedPageOffset(page).coerceIn(-1f, 1f)
            shader.setFloatUniform("resolution", size.width, size.height)
            shader.setFloatUniform("progress", signed)
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

            half4 main(float2 fragCoord) {
                float2 uv = fragCoord / resolution;
                float dir = sign(progress);
                float mag = abs(progress);
                float curlLine = dir > 0.0 ? mag : (1.0 - mag);

                // Fold coordinate — distance from the moving crease.
                float d = (uv.x - curlLine) * dir;

                if (d < 0.0) {
                    // Uncurled part — leave content untouched.
                    return content.eval(fragCoord);
                }

                // Curled — sample flipped content and fade with a lit crease.
                float2 flipped = float2(2.0 * curlLine * resolution.x - fragCoord.x, fragCoord.y);
                half4 back = content.eval(flipped);
                float shade = smoothstep(0.0, 0.15, d);
                float3 shaded = mix(half3(back.rgb), half3(0.05), shade * 0.6);
                float crease = smoothstep(0.02, 0.0, d);
                shaded += float3(crease * 0.5);
                float alpha = 1.0 - smoothstep(0.35, 0.5, d) * mag;
                return half4(shaded, alpha);
            }
        """
    }
}
