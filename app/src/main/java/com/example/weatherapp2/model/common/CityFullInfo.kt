package com.example.weatherapp2.model.common

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherapp2.model.common.openWeatherApi.Current
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Entity
@Parcelize
data class CityFullInfo(
    var name: String,
    var state: String?,
    var country: String,
    val lat: Double,
    val lon: Double,
    var updateTime: String?,
    var comment: String?,
    var pic: String?,
    @Embedded val current: @RawValue Current?,
    @PrimaryKey(autoGenerate = true) var id: Int? = null
) : Parcelable
