package com.example.weatherapp.model.common

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
