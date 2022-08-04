package com.example.weatherapp2.model.repository

import com.example.weatherapp2.model.common.CityFullInfo
import com.example.weatherapp2.model.common.CityInfo

interface CityWeatherRepo {

    suspend fun getCityWeatherFullInfo(
        cityInfo: CityInfo,
        lang: String
    ): CityFullInfo

    suspend fun getCityCoordinateByName(
        cityName: String
    ): List<CityInfo>
}
