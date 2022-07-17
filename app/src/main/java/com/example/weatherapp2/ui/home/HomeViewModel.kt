package com.example.weatherapp2.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp2.model.common.CityCoordinate
import com.example.weatherapp2.model.common.openWeatherApi.CityWeatherFullInfo
import com.example.weatherapp2.model.repository.CityWeatherRepoImpl
import com.example.weatherapp2.model.repository.LocalRepo
import kotlinx.coroutines.*

class HomeViewModel(
    private val cityWeatherRepoImpl: CityWeatherRepoImpl,
    private val localRepo: LocalRepo
) : ViewModel() {

    var cityWeatherList: MutableLiveData<List<CityWeatherFullInfo>> = MutableLiveData()
    private val cities = mutableListOf<CityCoordinate>()

    fun getCitiesInfo(language: String) {
        val job = CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            val temp = localRepo.getCitiesCoordinates()
            withContext(Dispatchers.Main) {
                cities.clear()
                cities.addAll(temp)
            }
        }

        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            job.join()
            Log.d("TAG", cities.toString())
            val result = mutableListOf<CityWeatherFullInfo>()
            cities.forEach { result.add(loadOneCityInfo(it, language)) }
            withContext(Dispatchers.Main) {
                cityWeatherList.postValue(result)
            }
        }
    }

    private suspend fun loadOneCityInfo(cityCoordinate: CityCoordinate, language: String): CityWeatherFullInfo {
        return cityWeatherRepoImpl.getCityWeatherFullInfo(
            cityCoordinate.lat.toString(),
            cityCoordinate.lon.toString(),
            language
        )
    }
}
