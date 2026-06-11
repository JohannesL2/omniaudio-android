package com.johannesl2.omniaudio.data.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.johannesl2.omniaudio.data.repository.RetrofitInstance
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    var urlInput by mutableStateOf("")
        private set

    var stationList by mutableStateOf<List<RadioStation>>(emptyList())
        private set

    var volume by mutableStateOf(0.7f)
        private set

    var currentPlayingUrl by mutableStateOf<String?>(null)
        private set

    init {
        loadStations()
    }

    private fun loadStations() {
        viewModelScope.launch {
            try {
                val stations = RetrofitInstance.api.getStations()

                stationList = stations
                    .filter { it.url.isNotBlank() }
                    .take(20)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateUrlInput(value: String) {
        urlInput = value
    }

    fun addCustomStation() {
        if (urlInput.isNotBlank()) {
            stationList = stationList + RadioStation(
                name = "Custom",
                url = urlInput
            )
        }
    }

    fun updateCurrentPlaying(url: String?) {
        currentPlayingUrl = url
    }

    fun updateVolume(newVolume: Float) {
        volume = newVolume
    }
}