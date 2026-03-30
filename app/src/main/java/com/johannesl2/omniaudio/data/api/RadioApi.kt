package com.johannesl2.omniaudio.data.api

import retrofit2.http.GET
import com.johannesl2.omniaudio.data.model.RadioStation

interface RadioApi {
    @GET("json/stations")
    suspend fun getStations(): List<RadioStation>
}