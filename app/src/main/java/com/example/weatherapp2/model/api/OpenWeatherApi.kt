package com.example.weatherapp2.model.api

import com.example.weatherapp2.model.common.openWeatherApi.CityWeatherFullInfo
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {

    @GET("data/2.5/onecall")
    suspend fun getCityWeatherInfo(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("exclude") exclude: String,
        @Query("lang") lang: String,
        @Query("units") units: String,
        @Query("appid") appid: String
    ): CityWeatherFullInfo
}
