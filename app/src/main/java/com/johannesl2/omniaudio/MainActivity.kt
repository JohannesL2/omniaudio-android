package com.johannesl2.omniaudio

import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.johannesl2.omniaudio.data.model.RadioStation
import com.johannesl2.omniaudio.data.repository.RetrofitInstance
import com.johannesl2.omniaudio.player.PlayerManager
import com.johannesl2.omniaudio.ui.VolumeSlider
import com.johannesl2.omniaudio.ui.theme.OmniAudioTheme
import com.johannesl2.omniaudio.visualizer.VisualizerView
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var playerManager: PlayerManager
    private val viewModel: MainViewModel = MainViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        playerManager = PlayerManager(this)

        enableEdgeToEdge()
        setContent {
            OmniAudioTheme {
                val uiState by viewModel.uiState.collectAsState()

                val scope = rememberCoroutineScope()

                // NEW FEATURE: Allows users to select local audio files
                val audioPicker =
                    rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.GetContent()
                    ) { uri ->

                        uri?.let {
                            viewModel.addStation(RadioStation(
                                name = getFileName(it),
                                url = it.toString()
                            ))
                        }
                    }
                LaunchedEffect(Unit) {
                    scope.launch {
                        try {
                            val stations = RetrofitInstance.api.getStations()

                            val stationList = stations
                                .filter { it.url.isNotBlank() }
                                .take(20)
                            viewModel.addStations(stationList)

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }


                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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
                            "Radio channels",
                            fontSize = 24.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        VisualizerView(
                            isPlaying = uiState.currentPlayingUrl != null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                        )

                        TextField(
                            value = uiState.urlInput,
                            onValueChange = { viewModel.changeUrlInput(it) },
                            label = { Text("Add radio-URL here") },
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                                .padding(top = 4.dp)
                        )

                        Button(
                            onClick = {
                                if (uiState.urlInput.isNotBlank()) {
                                    viewModel.addStation(RadioStation(
                                        name = "Custom",
                                        url = uiState.urlInput
                                    ))
                                }
                            },
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)
                                .fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(0.dp),
                            colors = ButtonDefaults.buttonColors(Color.Black)
                        ) {
                            Text("Add to list")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // NEW FEATURE: Load local music from device storage
                        Button(
                            onClick = {
                                audioPicker.launch("audio/*")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        ) {
                            Text("Load Custom Music")
                        }
                        VolumeSlider(
                            volume = uiState.volume,
                            onVolumeChange = { viewModel.changeVolume(it)}
//                            onVolumeChange = {
//                                volume = it
//                                playerManager.setVolume(it)
//                            }
                        )
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(uiState.stationList.size) { index ->
                                val station = uiState.stationList[index]

                                // Check whether this station/file is currently playing
                                val isPlaying = uiState.currentPlayingUrl == station.url

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    onClick = {
                                        if (isPlaying) {
                                            playerManager.pause()
                                            viewModel.changeCurrentPlayingUrl(null)
                                        } else {
                                            playerManager.play(station.url) {}
                                            viewModel.changeCurrentPlayingUrl(station.url)
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
                                            text = station.name,
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
    private fun getFileName(uri: Uri): String {
        var name = "Custom Audio"

        contentResolver.query(
            uri,
            null,
            null,
            null,
            null
        )?.use { cursor ->

            val nameIndex =
                cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)

            if (cursor.moveToFirst() && nameIndex >= 0) {
                name = cursor.getString(nameIndex)
            }
        }

        return name
    }
    }

