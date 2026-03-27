package com.johannesl2.omniaudio.player

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer

class PlayerManager(context: Context) {

    private val player: ExoPlayer = ExoPlayer.Builder(context).build()

    fun play(url: String, onReady: (Int) -> Unit) {
        val mediaItem = MediaItem.fromUri(url)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()

        player.addListener(object : androidx.media3.common.Player.Listener {
            @OptIn(UnstableApi::class)
            override fun onPlaybackStateChanged(state: Int) {
                if (state == androidx.media3.common.Player.STATE_READY) {
                    onReady(player.audioSessionId)
                }
            }
        })
    }

    fun pause() {
        player.pause()
    }

    fun release() {
        player.release()
    }

    fun setVolume(volume: Float) {
        player.volume = volume
    }

    @androidx.media3.common.util.UnstableApi
    fun getAudioSessionId(): Int {
        return player.audioSessionId
    }
}