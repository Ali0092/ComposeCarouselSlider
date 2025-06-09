package com.example.composecarousel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.composecarousel.ui.theme.ComposeCarouselTheme
import com.example.composecarousellib.CarouselImage
import com.example.composecarousellib.ComposeCarouselSlider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeCarouselTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {

    val dataList = listOf(
        CarouselImage.Resource(R.drawable.image1),
        CarouselImage.Resource(R.drawable.image2),
        CarouselImage.Resource(R.drawable.image3),
        CarouselImage.Resource(R.drawable.image4),
        CarouselImage.Resource(R.drawable.image5)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
//            .background(Color(0xFF3F3F3F))
        ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ComposeCarouselSlider(
            modifier = Modifier.padding(vertical = 12.dp),
            height = 600.dp,
            sidePadding = 40.dp,
            pageSpacing = 1.dp,
            imageCornerRoundness = 25.dp,
            nonSelectedDotColor = Color.Gray,
            selectedDotColor = Color.White,
            delay = 2000,
            enableAutoScroll = false,
            enableAnimationOnAutoScroll = false,
            animationSpecs = spring(
                dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow
            ),
            imagesList = dataList,
            getOnClick = { index ->
                //index of clicked image
            })
    }

}

