package com.example.weatherapp2.model.common.openWeatherApi

import androidx.room.Entity
import androidx.room.ForeignKey

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)
