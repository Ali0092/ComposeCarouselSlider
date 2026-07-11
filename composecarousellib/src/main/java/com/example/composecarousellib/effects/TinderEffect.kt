package com.example.composecarousellib.effects

import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.util.lerp
import androidx.compose.ui.zIndex
import com.example.composecarousellib.internal.signedPageOffset
import kotlin.math.abs

/**
 * Tinder-style deck swipe.
 *
 * Forward through the deck: swipe left throws the top card to the LEFT, swipe right
 * throws it to the RIGHT, and the stack behind grows into place. Direction is picked
 * directly from the sign of the pager offset, so the drag direction *is* the throw
 * direction — nothing to configure.
 *
 * Reverse through the deck: the previous card slides back into the top slot,
 * alternating between left and right by page parity so the deck feels like it's
 * being rewound rather than reeled from one side.
 *
 * The "top card" is picked from [PagerState.settledPage] — a value that only
 * updates once the snap animation finishes — so the throw animation runs cleanly
 * from start to finish without the card silently changing role mid-flight.
 */
class TinderEffect(
    private val throwRotationDeg: Float = 22f,
    private val throwXExtraFraction: Float = 0.25f,
    private val throwYFraction: Float = 0.06f,
    private val underneathScaleStep: Float = 0.06f,
    private val underneathLiftDp: Float = 14f,
    private val visibleDepth: Int = 3,
) : CarouselEffect {
    override val name: String = "Tinder"

    override fun buildItemModifier(page: Int, pagerState: PagerState): Modifier =
        Modifier
            .zIndex(-abs(pagerState.signedPageOffset(page)))
            .graphicsLayer {
                val offset = pagerState.signedPageOffset(page)
                when {
                    // Top card: throw in whichever direction the user is dragging.
                    // Sign of offset feeds directly into rotation and translation,
                    // so a leftward drag tips + throws left, rightward drag right.
                    page == pagerState.settledPage -> {
                        val throwProgress = abs(offset).coerceAtMost(1f)
                        rotationZ = offset * throwRotationDeg
                        translationX = -offset * size.width * throwXExtraFraction
                        translationY = throwProgress * size.height * throwYFraction
                        val ease = throwProgress * throwProgress
                        alpha = (1f - ease).coerceIn(0f, 1f)
                        transformOrigin = TransformOrigin(0.5f, 0.85f)
                    }
                    // Deck ahead of the top card: stacked centered behind, scaled and
                    // lifted by depth so they peek out. The card at depth 1 grows into
                    // the top slot as the current top is thrown off.
                    offset > 0f -> {
                        val depth = offset.coerceAtMost(visibleDepth.toFloat())
                        translationX = -offset * size.width
                        val scale = lerp(1f, 1f - underneathScaleStep * visibleDepth,
                            depth / visibleDepth)
                        scaleX = scale
                        scaleY = scale
                        translationY = underneathLiftDp * density * depth
                        alpha = when {
                            depth >= visibleDepth -> 0f
                            else -> lerp(1f, 0.6f, depth / (visibleDepth + 1f))
                        }
                    }
                    // Deck behind the top card: slide back into place during a reverse
                    // swipe. Even pages return from the left (the pager's own drift),
                    // odd pages are mirrored across center so they return from the
                    // right — consecutive rewinds alternate sides.
                    else -> {
                        val comesFromRight = page % 2 != 0
                        translationX = if (comesFromRight) {
                            // Mirror the pager placement: at offset=-1 the card sits
                            // fully off-screen right, at offset=0 it's centered.
                            -2f * offset * size.width
                        } else {
                            // Leave the pager's own translation → slides in from left.
                            0f
                        }
                    }
                }
            }
}
