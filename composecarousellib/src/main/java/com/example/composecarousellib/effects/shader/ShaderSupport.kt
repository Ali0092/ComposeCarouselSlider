package com.example.composecarousellib.effects.shader

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import androidx.compose.ui.graphics.asComposeRenderEffect

/**
 * Wraps a [RuntimeShader] into a Compose [androidx.compose.ui.graphics.RenderEffect]
 * whose [contentInputName] uniform is bound to the layer being drawn.
 */
internal fun RuntimeShader.toRenderEffect(
    contentInputName: String = "content",
): androidx.compose.ui.graphics.RenderEffect =
    RenderEffect.createRuntimeShaderEffect(this, contentInputName).asComposeRenderEffect()
