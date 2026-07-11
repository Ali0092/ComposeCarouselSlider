package com.example.composecarousellib.effects.shader

import android.graphics.RuntimeShader
import android.os.SystemClock
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.composecarousellib.effects.CarouselEffect
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.Language

/**
 * Water-surface ripple applied directly to each page image (no backdrop). Ripples
 * emit wherever the finger touches or drags across the card — like a fingertip
 * running across a still pool — and pulse from center on page change.
 *
 * Up to [maxRipples] concurrent ripples are composited in a single AGSL pass; each
 * ripple contributes a damped sinusoidal wave near its expanding front.
 */
class WaterRippleEffect(
    private val amplitudePx: Float = 26f,
    private val rippleDurationMs: Int = 1500,
    private val maxRipples: Int = 6,
    private val dragMinDeltaDp: Float = 18f,
    private val dragMinIntervalMs: Long = 55L,
) : CarouselEffect {
    override val name: String = "Water Ripple"

    @Composable
    override fun rememberItemModifier(page: Int, pagerState: PagerState): Modifier {
        val shader = remember { RuntimeShader(SRC) }
        val ripples = remember { mutableStateListOf<Ripple>() }
        val scope = rememberCoroutineScope()

        // Pulse from center whenever this page (re-)becomes the current page.
        LaunchedEffect(page, pagerState) {
            snapshotFlow { pagerState.currentPage }
                .filter { it == page }
                .drop(1)
                .collect {
                    scope.launch { spawnRipple(ripples, Offset(0.5f, 0.5f)) }
                }
        }

        return Modifier
            .pointerInput(page) {
                val minDeltaPx = dragMinDeltaDp.dp.toPx()
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false, pass = PointerEventPass.Initial)
                    val w = size.width.toFloat().coerceAtLeast(1f)
                    val h = size.height.toFloat().coerceAtLeast(1f)
                    scope.launch {
                        spawnRipple(
                            ripples,
                            Offset(
                                (down.position.x / w).coerceIn(0f, 1f),
                                (down.position.y / h).coerceIn(0f, 1f),
                            ),
                        )
                    }
                    var lastPos = down.position
                    var lastEmit = SystemClock.uptimeMillis()
                    while (true) {
                        val event = awaitPointerEvent(PointerEventPass.Initial)
                        val change = event.changes.firstOrNull() ?: break
                        if (!change.pressed) break
                        val delta = (change.position - lastPos).getDistance()
                        val now = SystemClock.uptimeMillis()
                        if (delta >= minDeltaPx && now - lastEmit >= dragMinIntervalMs) {
                            scope.launch {
                                spawnRipple(
                                    ripples,
                                    Offset(
                                        (change.position.x / w).coerceIn(0f, 1f),
                                        (change.position.y / h).coerceIn(0f, 1f),
                                    ),
                                )
                            }
                            lastPos = change.position
                            lastEmit = now
                        }
                    }
                }
            }
            .graphicsLayer {
                shader.setFloatUniform("resolution", size.width, size.height)
                shader.setFloatUniform("amplitude", amplitudePx)
                for (i in 0 until maxRipples) {
                    val r = ripples.getOrNull(i)
                    val progress = r?.progress?.value ?: -1f
                    val px = r?.position?.x ?: 0f
                    val py = r?.position?.y ?: 0f
                    shader.setFloatUniform("r$i", px, py, progress)
                }
                renderEffect = shader.toRenderEffect()
                clip = true
            }
    }

    private suspend fun spawnRipple(
        list: SnapshotStateList<Ripple>,
        positionUv: Offset,
    ) {
        val ripple = Ripple(position = positionUv, progress = Animatable(0f))
        list.add(ripple)
        while (list.size > maxRipples) list.removeAt(0)
        ripple.progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(rippleDurationMs),
        )
        list.remove(ripple)
    }

    private data class Ripple(val position: Offset, val progress: Animatable<Float, *>)

    private companion object {
        @Language("AGSL")
        const val SRC = """
            uniform shader content;
            uniform float2 resolution;
            uniform float amplitude;
            // Each ripple = (uv.x, uv.y, progress). progress in [0,1] active, else inactive.
            uniform float3 r0;
            uniform float3 r1;
            uniform float3 r2;
            uniform float3 r3;
            uniform float3 r4;
            uniform float3 r5;

            float2 rippleOffset(float2 uv, float3 ripple) {
                if (ripple.z < 0.0 || ripple.z > 1.0) return float2(0.0);
                float2 c = ripple.xy;
                float d = distance(uv, c);
                float radius = ripple.z * 0.6;        // leading edge grows out to 60% of the card
                float envelope = 1.0 - ripple.z;      // amplitude fades over life
                float x = d - radius;
                // Damped sinusoidal — a few concentric rings around the front, softening with distance.
                float wave = sin(x * 55.0) * exp(-abs(x) * 10.0);
                float2 dir = (uv - c) / max(d, 0.0001);
                return dir * wave * envelope;
            }

            half4 main(float2 fragCoord) {
                float2 uv = fragCoord / resolution;
                float2 offsetUv = float2(0.0);
                offsetUv += rippleOffset(uv, r0);
                offsetUv += rippleOffset(uv, r1);
                offsetUv += rippleOffset(uv, r2);
                offsetUv += rippleOffset(uv, r3);
                offsetUv += rippleOffset(uv, r4);
                offsetUv += rippleOffset(uv, r5);
                return content.eval(fragCoord + offsetUv * amplitude);
            }
        """
    }
}
