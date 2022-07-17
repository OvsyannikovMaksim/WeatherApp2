package com.example.weatherapp2.model.repository

import com.example.weatherapp2.model.common.openWeatherApi.CityWeatherFullInfo

interface CityWeatherRepo {

    suspend fun getCityWeatherFullInfo(
        latitude: String,
        longitude: String,
        lang: String
    ): CityWeatherFullInfo
}
