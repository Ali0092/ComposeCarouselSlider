package com.example.composecarousellib

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter

sealed class CarouselImage {

    data class Resource(val resId: Int) : CarouselImage()

    data class Url(val url: String) : CarouselImage()

    data class AsImageBitmap(val imageBitmap: ImageBitmap) : CarouselImage()

    data class Custom(val painter: Painter) : CarouselImage()

}