package com.example.weatherapp2.model.repository

import com.example.weatherapp2.model.common.CityFullInfo

interface Api {

    suspend fun getCityWeatherInfo(
        latitude: String,
        longitude: String,
        exclude: String,
        lang: String,
        units: String,
        appid: String
    ): CityFullInfo

    suspend fun getCityCoordinateByName(
        cityName: String,
        limit: String,
        appid: String
    ): List<CityFullInfo>

    suspend fun getCityNameByCoordinate(
        lat: String,
        lon: String,
        limit: String,
        appid: String
    ): List<CityFullInfo>
}
