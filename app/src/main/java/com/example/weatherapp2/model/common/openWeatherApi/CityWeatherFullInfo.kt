package com.example.weatherapp2.model.common.openWeatherApi

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CityWeatherFullInfo(
    @PrimaryKey var id: Int,
    val lat: Double,
    val lon: Double,
    var name: String,
    var state: String?,
    var country: String,
    @Embedded val current: Current
    // @Ignore val daily: List<Daily>
)
