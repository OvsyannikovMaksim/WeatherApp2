package com.example.weatherapp2.model.common.openWeatherApi

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Entity
@Parcelize
data class CityWeatherFullInfo(
    @PrimaryKey var id: Int,
    val lat: Double,
    val lon: Double,
    var name: String,
    var state: String?,
    var country: String,
    @Embedded val current: @RawValue Current
    // @Ignore val daily: List<Daily>
) : Parcelable
