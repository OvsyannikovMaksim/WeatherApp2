package com.example.weatherapp2.ui.mapCityInput

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp2.model.common.CityFullInfo
import com.example.weatherapp2.model.repository.CityWeatherRepoImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MapCityInputModel(private val cityWeatherRepoImpl: CityWeatherRepoImpl) : ViewModel() {

    private val resultOfSearch: MutableLiveData<CityFullInfo> = MutableLiveData()

    fun getResultOfSearch(): LiveData<CityFullInfo>{
        return resultOfSearch
    }

    fun getCitiesFromLine(lat: String, lon: String) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            resultOfSearch.postValue(cityWeatherRepoImpl.getCityNameByCoordinates(lat, lon))
        }
    }
}
