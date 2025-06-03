package com.example.composecarousel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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

    Column(modifier = modifier.fillMaxSize().background(Color.White), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        ComposeCarouselSlider(
            modifier = Modifier.padding(vertical = 12.dp),
            height = 600.dp,
            sidePadding = 40.dp,
            pageSpacing = 1.dp,
            imageCornerRoundness = 5.dp,
            nonSelectedDotColor = Color.Gray,
            selectedDotColor = Color.DarkGray,
            imagesList = listOf(
                CarouselImage.Resource(R.drawable.image1),
                CarouselImage.Resource(R.drawable.image2),
                CarouselImage.Resource(R.drawable.image3),
                CarouselImage.Resource(R.drawable.image4),
                CarouselImage.Resource(R.drawable.image5)
            )
        ) { index ->

        }
    }

}

