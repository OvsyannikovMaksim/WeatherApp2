package com.example.weatherapp2.model.repository

import androidx.lifecycle.MutableLiveData
import com.example.weatherapp2.model.common.CityFullInfo

interface Repository {

    var cityWeatherList: MutableLiveData<List<CityFullInfo>>
    val cityFullInfo: MutableList<CityFullInfo>
    val resultForAllCitiesFromApi: MutableList<CityFullInfo>
    val resultForAllCitiesFromRepo: MutableList<CityFullInfo>

    suspend fun getAllCitiesInfoFromRepo()
    suspend fun getCitiesCoordinates()
    suspend fun getCitiesInfoFromApi(language: String): Boolean
    suspend fun putCitiesToRepo()
}