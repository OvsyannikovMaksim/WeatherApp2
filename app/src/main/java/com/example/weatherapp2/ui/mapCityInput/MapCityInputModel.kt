package com.example.weatherapp2.ui.mapCityInput

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp2.model.common.CityFullInfo
import com.example.weatherapp2.model.repository.CityWeatherRepoImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MapCityInputModel(private val cityWeatherRepoImpl: CityWeatherRepoImpl) : ViewModel() {

    var resultOfSearch: MutableLiveData<CityFullInfo> = MutableLiveData()

    fun getCitiesFromLine(lat: String, lon: String) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            resultOfSearch.postValue(cityWeatherRepoImpl.getCityNameByCoordinates(lat, lon))
        }
    }
}
