package com.example.weatherapp2.model.repository

import androidx.lifecycle.LiveData
import com.example.weatherapp2.model.common.CityFullInfo

interface Repository {
    // Api
    suspend fun getCitiesInfoAndLoadItToLocalRepo(language: String)
    suspend fun getCityByCoordinates(lat: String, lon: String): CityFullInfo?

    // Repo
    fun dbUpdateLiveData(): LiveData<List<CityFullInfo>>
    suspend fun getCityByName(cityName: String): List<CityFullInfo>
    suspend fun getOneCityFullInfo(latitude: Double, longitude: Double): CityFullInfo?
    suspend fun getOneCityFullInfo(id: Int): CityFullInfo?
    suspend fun putCityToRepo(cityFullInfo: CityFullInfo)
    suspend fun deleteCityFullInfo(cityId: Int)

    // SharedPref
    fun getMetaData(name: String): String
    fun getServiceUpdateTime(): Int
    fun setServiceUpdateTime(updateTime: Int)
    fun getServiceState(): Boolean
    fun setServiceState(isServiceOn: Boolean)
    fun getChosenMapId(): Int
    fun setChosenMapId(chosenMapId: Int)
    fun getLastCityInNotification(): Int
    fun putLastCityInNotification(cityId: Int)
}
