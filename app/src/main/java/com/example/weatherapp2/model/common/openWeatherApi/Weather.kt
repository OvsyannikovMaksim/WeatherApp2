package com.example.weatherapp2.model.common.openWeatherApi

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)
