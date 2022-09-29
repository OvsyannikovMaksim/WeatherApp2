package com.example.weatherapp2.model.repository

import androidx.lifecycle.LiveData
import com.example.weatherapp2.model.common.CityFullInfo

interface Repository {

    fun dbUpdateLiveData(): LiveData<List<CityFullInfo>>
    suspend fun getCitiesInfoAndLoadItToLocalRepo(language: String)
}
