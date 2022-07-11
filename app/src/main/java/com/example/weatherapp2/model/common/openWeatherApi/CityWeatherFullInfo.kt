package com.example.weatherapp2.model.common.openWeatherApi

data class CityWeatherFullInfo(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int,
    val current: Current,
    val daily: List<Daily>
)
