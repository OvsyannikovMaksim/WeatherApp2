package com.example.weatherapp2.model.common.openWeatherApi

data class Daily(
    val dt: Int,
    val temp: Temp,
    val weather: List<Weather>,
    val pop: Double
)
