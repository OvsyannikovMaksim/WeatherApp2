package com.example.weatherapp2.model.api

import com.example.weatherapp2.model.common.CityFullInfo
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
    ): CityFullInfo

    @GET("geo/1.0/direct")
    suspend fun getCityCoordinateByName(
        @Query("q") cityName: String,
        @Query("limit") limit: String,
        @Query("appid") appid: String
    ): List<CityFullInfo>

    @GET("geo/1.0/reverse")
    suspend fun getCityNameByCoordinate(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("limit") limit: String,
        @Query("appid") appid: String
    ): List<CityFullInfo>
}
