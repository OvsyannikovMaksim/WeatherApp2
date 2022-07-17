package com.example.weatherapp2.model.common

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CityCoordinate(
    val lat: Double,
    val lon: Double,
    @PrimaryKey(autoGenerate = true) val id: Int? = null
)
