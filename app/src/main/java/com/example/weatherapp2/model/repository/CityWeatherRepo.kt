package com.example.weatherapp2.model.repository

import com.example.weatherapp2.model.common.CityFullInfo

interface CityWeatherRepo {

    suspend fun getCityWeatherFullInfo(
        cityFullInfo: CityFullInfo,
        lang: String
    ): CityFullInfo

    suspend fun getCityCoordinateByName(
        cityName: String
    ): List<CityFullInfo>

    suspend fun getCityNameByCoordinates(
        lat: String,
        lon: String
    ): CityFullInfo
}
