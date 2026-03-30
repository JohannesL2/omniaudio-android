package com.johannesl2.omniaudio.data.repository

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.johannesl2.omniaudio.data.api.RadioApi

object RetrofitInstance {
    val api: RadioApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://de1.api.radio-browser.info/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RadioApi::class.java)
    }
}