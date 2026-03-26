package com.johannesl2.omniaudio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp

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

                        androidx.compose.foundation.layout.Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        androidx.compose.material3.Button(
                            onClick = {
                                if (isPlaying) {
                                    playerManager.pause()
                                } else {
                                    playerManager.play("")
                                }
                                isPlaying = !isPlaying
                            },
                            modifier = Modifier.size(width = 180.dp, height = 60.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black
                            )
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                                contentDescription = if (isPlaying) "Pause" else "Play",
                                tint = Color.Yellow,
                                modifier = Modifier.size(30.dp)
                            )
                            Text(
                                text = if (isPlaying) "Pause" else "Play",
                                fontSize = 18.sp,
                                modifier = Modifier.padding(start = 8.dp)
                                )
                        }
                    }
                }
            }
        }
    }
}