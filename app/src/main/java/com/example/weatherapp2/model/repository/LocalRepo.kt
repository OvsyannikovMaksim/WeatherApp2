package com.example.weatherapp2.model.repository

import com.example.weatherapp2.model.common.CityCoordinate
import com.example.weatherapp2.model.common.openWeatherApi.CityWeatherFullInfo

interface LocalRepo {

    // Это для координат городов
    suspend fun getCitiesCoordinates(): List<CityCoordinate>

    suspend fun addCityCoordinate(cityCoordinate: CityCoordinate)

    suspend fun deleteCityCoordinate(cityCoordinate: CityCoordinate)

    // Это для погоды
    suspend fun getOneCityWeatherFullInfo(latitude: Double, longitude: Double): CityWeatherFullInfo

    suspend fun addOneCityWeatherFullInfo(cityWeatherFullInfo: CityWeatherFullInfo)

    suspend fun deleteOneCityWeatherFullInfo(cityWeatherFullInfo: CityWeatherFullInfo)
}
