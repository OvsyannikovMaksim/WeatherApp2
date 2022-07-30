package com.example.weatherapp2.ui.inputCities

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp2.model.common.CityInfo
import com.example.weatherapp2.model.repository.CityWeatherRepoImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class InputCitiesModel(
    private val cityWeatherRepoImpl: CityWeatherRepoImpl
) : ViewModel() {

    var resultOfSearch: MutableLiveData<List<CityInfo>> = MutableLiveData()

    fun getCitiesFromLine(cityName: String) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            resultOfSearch.postValue(cityWeatherRepoImpl.getCityCoordinateByName(cityName))
        }
    }
}
