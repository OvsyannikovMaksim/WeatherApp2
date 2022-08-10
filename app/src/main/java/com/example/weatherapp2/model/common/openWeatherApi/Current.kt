package com.example.weatherapp2.model.common.openWeatherApi

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Current(
    val temp: Double,
    val feels_like: Double,
    val pressure: Int,
    val humidity: Int,
    val uvi: Double,
    val wind_speed: Double,
    val wind_deg: Int,
    val weather: @RawValue List<Weather>
) : Parcelable
