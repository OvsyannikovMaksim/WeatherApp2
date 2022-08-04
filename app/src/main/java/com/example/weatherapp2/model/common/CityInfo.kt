package com.example.weatherapp2.model.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CityInfo(
    val id: Int,
    val name: String,
    val state: String?,
    val country: String,
    val lat: Double,
    val lon: Double,
    val comment: String?,
    val pic: String?
) : Parcelable
