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
 * Pixelate — the page dissolves into large pixel blocks mid-transition and sharpens
 * back at rest. Classic retro sprite reveal.
 */
class PixelateEffect(
    private val minCellPx: Float = 2f,
    private val maxCellPx: Float = 48f,
) : CarouselEffect {
    override val name: String = "Pixelate"

    @Composable
    override fun rememberItemModifier(page: Int, pagerState: PagerState): Modifier {
        val shader = remember { RuntimeShader(SRC) }
        return Modifier.graphicsLayer {
            val progress = pagerState.absPageOffset(page).coerceIn(0f, 1f)
            val cell = minCellPx + (maxCellPx - minCellPx) * progress
            shader.setFloatUniform("resolution", size.width, size.height)
            shader.setFloatUniform("cellSize", cell.coerceAtLeast(1f))
            renderEffect = shader.toRenderEffect()
            clip = true
        }
    }

    private companion object {
        @Language("AGSL")
        const val SRC = """
            uniform shader content;
            uniform float2 resolution;
            uniform float cellSize;

            half4 main(float2 fragCoord) {
                float2 snapped = floor(fragCoord / cellSize) * cellSize + cellSize * 0.5;
                return content.eval(snapped);
            }
        """
    }
}
