package com.example.composecarousel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composecarousel.ui.theme.ComposeCarouselTheme
import com.example.composecarousellib.CarouselImage
import com.example.composecarousellib.CarouselSlider
import com.example.composecarousellib.effects.CarouselEffect
import com.example.composecarousellib.effects.CarouselEffects
import com.example.composecarousellib.indicators.ExpandingPillIndicator

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

private val demoImages: List<CarouselImage> = listOf(
    CarouselImage.Resource(R.drawable.image3),
    CarouselImage.Resource(R.drawable.image1),
    CarouselImage.Resource(R.drawable.image2),
    CarouselImage.Resource(R.drawable.image4),
    CarouselImage.Resource(R.drawable.image5),
    CarouselImage.Resource(R.drawable.image6),
)

@Composable
private fun CarouselGallery(modifier: Modifier = Modifier) {
    val effects = remember { CarouselEffects.all }
    var selectedEffect by remember { mutableStateOf<CarouselEffect>(effects.first()) }
    val pagerState = rememberPagerState { demoImages.size }
    val indicator = remember { ExpandingPillIndicator() }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Compose Carousel",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Text(
            text = selectedEffect.name,
            color = Color(0xFFB6B6C4),
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
        )
        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            CarouselSlider(
                items = demoImages,
                pageHeight = null,
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 24.dp),
                pageSpacing = 8.dp,
                itemCornerRadius = 22.dp,
                effect = selectedEffect,
                indicator = indicator,
                pagerState = pagerState,
            )
        }

        EffectChips(
            effects = effects,
            selected = selectedEffect,
            onSelect = { selectedEffect = it },
        )
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun EffectChips(
    effects: List<CarouselEffect>,
    selected: CarouselEffect,
    onSelect: (CarouselEffect) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 12.dp, vertical = 8.dp),
    ) {
        effects.forEach { effect ->
            FilterChip(
                selected = effect === selected,
                onClick = { onSelect(effect) },
                label = { Text(effect.name) },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = Color(0xFF1B1B22),
                    labelColor = Color(0xFFCECEDA),
                    selectedContainerColor = Color(0xFF3D5AFE),
                    selectedLabelColor = Color.White,
                ),
                shape = RoundedCornerShape(50),
            )
            Spacer(Modifier.width(6.dp))
        }
    }
}
