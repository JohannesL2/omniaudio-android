package com.johannesl2.omniaudio.visualizer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun VisualizerView(
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    var bars by remember { mutableStateOf(List(32) { 0f }) }

    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            bars = bars.map {
                val newValue = Random.nextFloat()
                it * 0.7f + newValue * 0.3f
            }
            delay(80)
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val barWidth = size.width / bars.size

        bars.forEachIndexed { index, value ->
            val height = value * size.height

            drawLine(
                color = Color(0xFFBDBDBD),
                start = Offset(
                    x = index * barWidth + barWidth / 2,
                    y = size.height
                ),
                end = Offset(
                    x = index * barWidth + barWidth / 2,
                    y = size.height - height
                ),
                strokeWidth = (barWidth * 0.8f).coerceAtLeast(4f)
            )
        }
    }
}