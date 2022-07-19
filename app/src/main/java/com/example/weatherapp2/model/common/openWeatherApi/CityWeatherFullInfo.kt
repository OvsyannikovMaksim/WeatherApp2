package com.example.weatherapp2.model.common.openWeatherApi

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CityWeatherFullInfo(
    @PrimaryKey var id: Int,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int,
    @Embedded val current: Current
    // @Ignore val daily: List<Daily>
)
