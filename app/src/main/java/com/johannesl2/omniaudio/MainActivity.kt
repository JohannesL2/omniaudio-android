package com.johannesl2.omniaudio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.johannesl2.omniaudio.player.PlayerManager
import com.johannesl2.omniaudio.ui.theme.OmniAudioTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.johannesl2.omniaudio.ui.VolumeSlider
import com.johannesl2.omniaudio.visualizer.VisualizerViewModel
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.johannesl2.omniaudio.visualizer.VisualizerView

class MainActivity : ComponentActivity() {

    private lateinit var playerManager: PlayerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        playerManager = PlayerManager(this)

        enableEdgeToEdge()
        setContent {
            OmniAudioTheme {

                var isPlaying by remember { mutableStateOf(false) }

                var urlInput by remember { mutableStateOf("") }

                var stationList by remember { mutableStateOf(listOf<String>()) }

                var volume by remember { mutableStateOf(0.7f) }

                var currentPlayingUrl by remember { mutableStateOf<String?>(null) }


                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    androidx.compose.foundation.layout.Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.omni_audio),
                            contentDescription = "OmniAudio Logo",
                            modifier = Modifier
                                .height(280.dp)
                                .padding(vertical = 16.dp)
                        )

                        Text("Radio channels", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

                        VolumeSlider(
                            volume = volume,
                            onVolumeChange = { newVolume ->
                                volume = newVolume
                                playerManager.setVolume(newVolume)
                            }
                        )

                        VisualizerView(
                            isPlaying = currentPlayingUrl != null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                        )

                        TextField(
                            value = urlInput,
                            onValueChange = { urlInput = it },
                            label = { Text("Add radio-URL here")},
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = {
                                if (urlInput.isNotBlank()) {
                                    stationList = stationList + urlInput
                                    urlInput = ""
                                }
                            },
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Text("Add to list")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(stationList.size) { index ->
                                val url = stationList[index]
                                val isPlaying = currentPlayingUrl == url

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    onClick = {
                                        if (isPlaying) {
                                            playerManager.pause()
                                            currentPlayingUrl = null
                                        } else {
                                            playerManager.play(url) {}
                                            currentPlayingUrl = url
                                        }
                                    }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                                            contentDescription = null
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            text = "Station ${index + 1}: $url",
                                            modifier = Modifier.weight(1f),
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }
                        }
                    }
                }
            }
        }
    }