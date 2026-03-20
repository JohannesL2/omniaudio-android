package com.johannesl2.omniaudio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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

class MainActivity : ComponentActivity() {

    private lateinit var playerManager: PlayerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        playerManager = PlayerManager(this)

        enableEdgeToEdge()
        setContent {
            var isPlaying by remember { mutableStateOf(false) }
            OmniAudioTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    androidx.compose.foundation.layout.Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                    ) {
                        Text("NRJ Radio")

                        androidx.compose.foundation.layout.Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        androidx.compose.material3.Button(
                            onClick = {
                                if (isPlaying) {
                                    playerManager.pause()
                                } else {
                                    playerManager.play("https://streaming.nrjaudio.fm/oumrha8fnozc")
                                }
                                isPlaying = !isPlaying
                            }
                        ) {
                            Text(if (isPlaying) "Pause" else "Play")
                        }
                    }
                }
            }
        }
    }
}