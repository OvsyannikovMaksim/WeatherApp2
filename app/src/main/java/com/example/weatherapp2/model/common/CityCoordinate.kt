package com.example.weatherapp2.model.common

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class CityCoordinate(
    val name: String,
    val state: String?,
    val country: String,
    val lat: Double,
    val lon: Double,
    val comment: String?,
    val pic_uri: String?,
    @PrimaryKey(autoGenerate = true) val id: Int? = null
) : Parcelable
