package com.example.weatherapp2.model.repository

import androidx.room.*
import com.example.weatherapp2.model.common.CityFullInfo
import com.example.weatherapp2.model.common.CityInfo

interface LocalRepo {

    suspend fun getAllCityFullInfo(): List<CityFullInfo>

    suspend fun getOneCityFullInfo(id: Int): CityFullInfo?

    suspend fun getOneCityFullInfo(latitude: Double, longitude: Double): CityFullInfo?

    suspend fun getAllCityInfo(): List<CityInfo>

    suspend fun insertCityFullInfo(cityFullInfo: CityFullInfo)

    suspend fun updateCityFullInfo(cityFullInfo: CityFullInfo)

    suspend fun deleteCityFullInfo(cityFullInfo: CityFullInfo)
}
