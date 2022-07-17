package com.example.weatherapp2.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp2.model.common.CityCoordinate
import com.example.weatherapp2.model.common.openWeatherApi.CityWeatherFullInfo
import com.example.weatherapp2.model.repository.CityWeatherRepoImpl
import kotlinx.coroutines.*

class HomeViewModel(private val cityWeatherRepoImpl: CityWeatherRepoImpl) : ViewModel() {

    var cityWeatherList: MutableLiveData<List<CityWeatherFullInfo>> = MutableLiveData()

    fun getCitiesInfo(cities: List<CityCoordinate>, language: String) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            val result = mutableListOf<CityWeatherFullInfo>()
            cities.forEach { result.add(loadOneCityInfo(it, language)) }
            Log.d("TAG", result.toString())
            withContext(Dispatchers.Main) {
                cityWeatherList.postValue(result)
            }
        }
    }

    private suspend fun loadOneCityInfo(cityCoordinate: CityCoordinate, language: String): CityWeatherFullInfo {
        return cityWeatherRepoImpl.getCityWeatherFullInfo(
            cityCoordinate.lat,
            cityCoordinate.lon,
            language
        )
    }
}
