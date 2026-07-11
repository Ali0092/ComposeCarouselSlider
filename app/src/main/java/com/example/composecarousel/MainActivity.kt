package com.example.composecarousel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.composecarousel.ui.theme.ComposeCarouselTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeCarouselTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF0B0B10)) {
                    Scaffold(
                        containerColor = Color.Transparent,
                        contentWindowInsets = WindowInsets.systemBars,
                    ) { innerPadding ->
                        CarouselGallery(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                        )
                    }
                }
            }
        }
    }

}

