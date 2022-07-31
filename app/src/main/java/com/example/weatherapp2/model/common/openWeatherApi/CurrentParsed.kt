package com.example.weatherapp2.model.common.openWeatherApi

data class CurrentParsed(
    val temp: String,
    val feels_like: String,
    val pressure: String,
    val humidity: String,
    val uvi: String,
    val wind: String,
    val weatherDescription: String,
    val weatherPicture: String
)
