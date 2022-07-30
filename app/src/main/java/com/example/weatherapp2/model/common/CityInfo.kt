package com.example.weatherapp2.model.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CityInfo(
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String,
    val state: String?
) : Parcelable
