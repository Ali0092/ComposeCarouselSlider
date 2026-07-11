package com.example.composecarousellib.effects.shader

import android.graphics.RuntimeShader
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.zIndex
import com.example.composecarousellib.effects.CarouselEffect
import com.example.composecarousellib.internal.absPageOffset
import com.example.composecarousellib.internal.signedPageOffset
import org.intellij.lang.annotations.Language

/**
 * Premium liquid-glass carousel.
 *
 * - The current image is drawn full-bleed and heavily blurred as the backdrop —
 *   the entire slider area becomes tinted with the active scene.
 * - Each page floats on top as a smaller, sharp card. A radial refraction shader
 *   distorts the card near its edges; a chromatic rim highlights the border like
 *   real light bending through glass.
 * - Neighbor pages soften and shrink so the focus stays on the front card.
 */
class LiquidGlassEffect(
    private val cardScale: Float = 0.82f,
    private val neighborScale: Float = 0.72f,
    private val backdropBlurDp: Float = 60f,
    private val edgeRefractionPx: Float = 22f,
    private val edgeHighlight: Float = 0.35f,
) : CarouselEffect {
    override val name: String = "Liquid Glass"
    override val prefersBackdrop: Boolean = true

    @Composable
    override fun Backdrop(
        pagerState: PagerState,
        pageCount: Int,
        renderImage: @Composable (index: Int, modifier: Modifier) -> Unit,
    ) {
        val target by animateFloatAsState(
            targetValue = pagerState.currentPage.toFloat(),
            animationSpec = tween(600),
            label = "backdrop-page",
        )
        val current = target.toInt().coerceIn(0, (pageCount - 1).coerceAtLeast(0))
        val next = (current + 1).coerceIn(0, (pageCount - 1).coerceAtLeast(0))
        val fade = (target - current).coerceIn(0f, 1f)

        Box(modifier = Modifier.fillMaxSize()) {
            renderImage(
                current,
                Modifier
                    .fillMaxSize()
                    .blur(backdropBlurDp.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                    .graphicsLayer { alpha = 1f - fade },
            )
            if (next != current) {
                renderImage(
                    next,
                    Modifier
                        .fillMaxSize()
                        .blur(backdropBlurDp.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                        .graphicsLayer { alpha = fade },
                )
            }
            // Subtle darkening so cards read clearly against the blurred backdrop.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.35f)),
            )
        }
    }

    @Composable
    override fun rememberItemModifier(page: Int, pagerState: PagerState): Modifier {
        val shader = remember { RuntimeShader(SRC) }
        return Modifier
            .zIndex(1f - pagerState.absPageOffset(page).coerceAtMost(2f))
            .graphicsLayer {
                val abs = pagerState.absPageOffset(page).coerceIn(0f, 2f)
                val signed = pagerState.signedPageOffset(page).coerceIn(-1f, 1f)
                val focus = (1f - abs).coerceIn(0f, 1f)
                val scale = lerp(neighborScale, cardScale, focus)
                scaleX = scale
                scaleY = scale
                alpha = (1f - abs * 0.5f).coerceAtLeast(0.2f)

                shader.setFloatUniform("resolution", size.width, size.height)
                shader.setFloatUniform("focus", focus)
                shader.setFloatUniform("direction", signed)
                shader.setFloatUniform("refractPx", edgeRefractionPx)
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
            uniform float focus;          // 1 at center, 0 at neighbor
            uniform float direction;      // signed page offset
            uniform float refractPx;      // max refraction magnitude in px
            uniform float edgeHighlight;  // rim brightness

            half4 main(float2 fragCoord) {
                float2 uv = fragCoord / resolution;
                float2 centered = uv - 0.5;

                // Radial "glass dome" — strongest refraction near the border.
                float d = length(centered) * 2.0;
                float rim = smoothstep(0.75, 1.0, d);
                float2 dir = normalize(centered + float2(0.0001));

                // Slight parallax that follows the drag direction, so the card feels
                // physically responsive when swiping.
                float2 warp = dir * rim * refractPx + float2(direction * refractPx * 0.35, 0.0) * rim;

                // Chromatic split at the rim only.
                half4 baseC = content.eval(fragCoord - warp);
                half4 rC = content.eval(fragCoord - warp * 1.06);
                half4 bC = content.eval(fragCoord - warp * 0.94);
                half3 col = mix(half3(baseC.rgb), half3(rC.r, baseC.g, bC.b), rim);

                // Bright specular streak near the outer edge.
                float highlight = smoothstep(0.85, 1.0, d) * (1.0 - smoothstep(1.0, 1.05, d));
                col += half3(highlight * edgeHighlight * focus);

                // Subtle inner shading gives thickness/volume.
                float inner = 1.0 - smoothstep(0.0, 0.9, d) * 0.15;
                col *= inner;

                return half4(col, baseC.a);
            }
        """
    }
}

