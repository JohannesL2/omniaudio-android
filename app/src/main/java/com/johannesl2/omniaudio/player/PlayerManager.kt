package com.johannesl2.omniaudio.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

class PlayerManager(context: Context) {

    private val player: ExoPlayer = ExoPlayer.Builder(context).build()

    fun play(url: String) {
        val mediaItem = MediaItem.fromUri(url)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
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
}