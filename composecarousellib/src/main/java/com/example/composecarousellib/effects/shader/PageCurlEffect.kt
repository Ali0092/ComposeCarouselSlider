package com.example.composecarousellib.effects.shader

import android.graphics.RuntimeShader
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.zIndex
import com.example.composecarousellib.effects.CarouselEffect
import com.example.composecarousellib.internal.absPageOffset
import com.example.composecarousellib.internal.signedPageOffset
import org.intellij.lang.annotations.Language

/**
 * Book-style page curl. The page peels from the trailing edge as it swipes; the back
 * of the curl fades to a cream paper tone, a lit crease and a soft drop-shadow trail
 * behind it sell the fold.
 *
 * This is a shader approximation (no true 3D mesh) but tuned so the fold looks
 * organic — the crease bows inward, brightness peaks along the fold, and the
 * curled section darkens with distance from the fold.
 */
class PageCurlEffect(
    private val maxCurlFraction: Float = 0.7f,
) : CarouselEffect {
    override val name: String = "Page Curl"

    @Composable
    override fun rememberItemModifier(page: Int, pagerState: PagerState): Modifier {
        val shader = remember { RuntimeShader(SRC) }
        return Modifier
            .zIndex(1f - pagerState.absPageOffset(page).coerceAtMost(1f))
            .graphicsLayer {
                val signed = pagerState.signedPageOffset(page).coerceIn(-1f, 1f)
                shader.setFloatUniform("resolution", size.width, size.height)
                shader.setFloatUniform("progress", signed)
                shader.setFloatUniform("maxCurl", maxCurlFraction)
                renderEffect = shader.toRenderEffect()
                clip = true
            }
    }

    private companion object {
        @Language("AGSL")
        const val SRC = """
            uniform shader content;
            uniform float2 resolution;
            uniform float progress;   // signed: -1 (fully curled leaving left) to 1 (leaving right)
            uniform float maxCurl;    // width of the curled region as a fraction of the page

            half3 paperBack(float t) {
                // Cream → deeper cream → warm shadow as we descend the curl.
                half3 warm = half3(0.96, 0.92, 0.83);
                half3 shade = half3(0.55, 0.5, 0.42);
                return mix(warm, shade, clamp(t, 0.0, 1.0));
            }

            half4 main(float2 fragCoord) {
                float2 uv = fragCoord / resolution;
                float p = clamp(progress, -1.0, 1.0);
                float mag = abs(p);
                float dir = sign(p);
                if (mag < 0.001) return content.eval(fragCoord);

                // Fold anchor moves in from the trailing edge.
                float foldX = dir > 0.0 ? 1.0 - mag * maxCurl : mag * maxCurl;

                // Distance from fold in the curl direction (positive = curled side).
                // A subtle vertical bow (parabolic) gives the crease an organic curve.
                float bow = (uv.y - 0.5) * (uv.y - 0.5) * 0.12;
                float d = (uv.x - foldX - dir * bow) * dir;

                if (d < 0.0) {
                    // Flat portion — untouched content, plus a soft drop-shadow near the fold.
                    float shadow = smoothstep(0.0, -0.05, d) * 0.4;
                    half4 c = content.eval(fragCoord);
                    return half4(half3(c.rgb) * (1.0 - shadow), c.a);
                }

                // Curled portion.
                float t = d / (maxCurl + 0.001);
                if (t > 1.0) {
                    return half4(0.0, 0.0, 0.0, 0.0);
                }

                half3 back = paperBack(t);

                // Faint ghost of the underlying image on the back — like paper isn't fully opaque.
                float2 flipped = float2((2.0 * foldX - uv.x), uv.y) * resolution;
                half4 ghost = content.eval(clamp(flipped, float2(0.0), resolution));
                back = mix(back, half3(ghost.rgb) * 0.6 + half3(0.35, 0.32, 0.27) * 0.4, 0.35);

                // Lit crease at the fold line — brightest right at d=0, softens outward.
                float crease = smoothstep(0.015, 0.0, d);
                back += half3(crease * 0.55);

                // Trailing dark edge where the curled paper approaches the back-face fade.
                float trail = smoothstep(0.85, 1.0, t);
                back *= (1.0 - trail * 0.5);

                // Slight alpha falloff at the very tip of the curl for a soft edge.
                float alpha = 1.0 - smoothstep(0.9, 1.0, t) * 0.85;
                return half4(back, alpha);
            }
        """
    }
}
