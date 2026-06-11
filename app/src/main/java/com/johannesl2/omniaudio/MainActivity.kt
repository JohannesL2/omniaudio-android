package com.johannesl2.omniaudio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.johannesl2.omniaudio.data.model.MainViewModel
import com.johannesl2.omniaudio.player.PlayerManager
import com.johannesl2.omniaudio.ui.VolumeSlider
import com.johannesl2.omniaudio.ui.theme.OmniAudioTheme
import com.johannesl2.omniaudio.visualizer.VisualizerView

class MainActivity : ComponentActivity() {

    private lateinit var playerManager: PlayerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        playerManager = PlayerManager(this)

        enableEdgeToEdge()

        setContent {
            OmniAudioTheme {

                // Obtain ViewModel
                val viewModel: MainViewModel = viewModel()

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.omni_audio),
                            contentDescription = "OmniAudio Logo",
                            modifier = Modifier
                                .height(200.dp)
                                .padding(vertical = 16.dp)
                        )

                        Text(
                            text = "Radio channels",
                            fontSize = 24.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Audio visualizer
                        VisualizerView(
                            isPlaying = viewModel.currentPlayingUrl != null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                        )
                        // Volume control
                        VolumeSlider(
                            volume = viewModel.volume,
                            onVolumeChange = {
                                viewModel.updateVolume(it)
                                playerManager.setVolume(it)
                            }
                        )

                        // URL input field
                        TextField(
                            value = viewModel.urlInput,
                            onValueChange = viewModel::updateUrlInput,
                            label = { Text("Add radio-URL here") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                                .padding(top = 4.dp)
                        )

                        // Add custom station
                        Button(
                            onClick = {
                                viewModel.addCustomStation()
                            },
                            modifier = Modifier
                                .padding(vertical = 8.dp, horizontal = 8.dp)
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(0.dp),
                            colors = ButtonDefaults.buttonColors(Color.Black)
                        ) {
                            Text("Add to list")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Station list
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {

                            items(viewModel.stationList) { station ->

                                val isPlaying =
                                    viewModel.currentPlayingUrl == station.url

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    onClick = {

                                        if (isPlaying) {
                                            playerManager.pause()
                                            viewModel.updateCurrentPlaying(null)
                                        } else {
                                            playerManager.play(station.url) {}
                                            viewModel.updateCurrentPlaying(station.url)
                                        }
                                    }
                                ) {

                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        Icon(
                                            imageVector =
                                                if (isPlaying)
                                                    Icons.Filled.Pause
                                                else
                                                    Icons.Filled.PlayArrow,
                                            contentDescription = null
                                        )

                                        Spacer(
                                            modifier = Modifier.width(12.dp)
                                        )

                                        Text(
                                            text = station.name ?: "Unknown station",
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