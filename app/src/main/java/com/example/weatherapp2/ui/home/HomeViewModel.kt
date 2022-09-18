package com.example.weatherapp2.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp2.model.common.CityFullInfo
import com.example.weatherapp2.model.repository.Repository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: Repository
) : ViewModel() {

    var cityWeatherList: MutableLiveData<List<CityFullInfo>> = repository.cityWeatherList

    fun getCitiesInfoAndLoadItToLocalRepo(language: String) {
        viewModelScope.launch {
            delay(200)
            repository.getAllCitiesInfoFromRepo()
            repository.getCitiesCoordinates()
            delay(200)
            repository.getCitiesInfoFromApi(language)
            repository.putCitiesToRepo()
        }
    }

    fun getCitiesInfo() {
        viewModelScope.launch {
            delay(500)
            repository.getAllCitiesInfoFromRepo()
        }
    }
}
