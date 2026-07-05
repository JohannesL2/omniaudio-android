package com.johannesl2.omniaudio

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.johannesl2.omniaudio.data.model.RadioStation
import com.johannesl2.omniaudio.data.repository.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MainViewModel(): ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadInitStation()
    }
    fun addStation(station: RadioStation){
        val stations: MutableList<RadioStation> = _uiState.value.stationList.toMutableStateList()
        stations.add(station)

        _uiState.update { currentState ->
            currentState.copy(
                stationList = stations
            )
        }
    }

    fun addStations(stations: List<RadioStation>){
        _uiState.update { currentState ->
            currentState.copy(
                stationList = stations
            )
        }
    }

    fun changeUrlInput(url: String){
        _uiState.update { currentState ->
            currentState.copy(
                urlInput = url
            )
        }
    }

    fun changeVolume(vol: Float) {
        _uiState.update { currentState ->
            currentState.copy( volume = vol )
        }
    }

    fun changeCurrentPlayingUrl(url: String?) {
        _uiState.update { currentState ->
            _uiState.value.copy(
                currentPlayingUrl = url
            )
        }
    }

    private fun loadInitStation() {
        viewModelScope.launch {
            try {
                _uiState.update { currentState ->
                    currentState.copy(
                        stationList = RetrofitInstance.api.getStations()
                            .filter { it.url.isNotBlank() }.take(20)
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}