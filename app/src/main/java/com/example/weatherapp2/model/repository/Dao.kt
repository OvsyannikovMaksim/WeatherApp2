package com.example.weatherapp2.model.repository

import androidx.lifecycle.LiveData
import com.example.weatherapp2.model.common.CityFullInfo

interface Dao {

    fun dbUpdateLiveData(): LiveData<List<CityFullInfo>>

    suspend fun getAllCityFullInfo(): List<CityFullInfo>

    suspend fun getOneCityFullInfo(id: Int): CityFullInfo?

    suspend fun getOneCityFullInfo(latitude: Double, longitude: Double): CityFullInfo?

    suspend fun insertCityFullInfo(cityFullInfo: CityFullInfo)

    suspend fun updateCityFullInfo(cityFullInfo: CityFullInfo)

    suspend fun deleteCityFullInfo(cityId: Int)
}
