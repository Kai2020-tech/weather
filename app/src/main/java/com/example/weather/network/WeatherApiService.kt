package com.example.weather.network

import com.example.weather.WeatherData
import com.google.gson.annotations.SerializedName
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://opendata.cwb.gov.tw/api/"

interface WeatherApiService {
    @GET("v1/rest/datastore/F-C0032-001")
    fun getWeather(
        @Query("Authorization") key: String,
        @Query("locationName") city: String? = null
    ): Call<WeatherData>
}

object WeatherApi {
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    val retrofitService: WeatherApiService = retrofit.create(WeatherApiService::class.java)
}



