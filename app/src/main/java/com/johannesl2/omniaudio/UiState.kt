package com.johannesl2.omniaudio

import com.johannesl2.omniaudio.data.model.RadioStation

/**
 * Data Class to manage the application state
 */
data class UiState(
    val isPlaying: Boolean = false,
    val urlInput: String = "",
    val stationList: List<RadioStation> = emptyList(),
    val volume: Float = 0.7f,
    val currentPlayingUrl: String? = null,
)
