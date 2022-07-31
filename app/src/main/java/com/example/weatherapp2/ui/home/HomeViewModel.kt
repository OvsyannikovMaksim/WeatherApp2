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
    private val resultForAllCitiesFromApi = mutableListOf<CityWeatherFullInfo>()
    private val resultForAllCitiesFromRepo = mutableListOf<CityWeatherFullInfo>()

    fun getCitiesInfoAndLoadItToLocalRepo(language: String) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            getCitiesList()
            Log.d("HomeViewModel.kt: getCitiesInfoAndLoadItToLocalRepo()", cities.toString())
            getAllCitiesInfoFromRepo()
            if (getCitiesInfoFromApi(language) && resultForAllCitiesFromApi.isNotEmpty()) {
                Log.d("HomeViewModel.kt: getCitiesInfoAndLoadItToLocalRepo()", "Download to repo")
                loadCitiesToRepo(resultForAllCitiesFromApi)
            }
        }
    }

    private suspend fun loadOneCityInfoFromApi(cityCoordinate: CityCoordinate, language: String): CityWeatherFullInfo {
        return cityWeatherRepoImpl.getCityWeatherFullInfo(
            cityCoordinate.id!!,
            cityCoordinate.lat.toString(),
            cityCoordinate.lon.toString(),
            cityCoordinate.name,
            cityCoordinate.state,
            cityCoordinate.country,
            language
        )
    }

    private suspend fun loadAllCitiesInfoFromRepo(): List<CityWeatherFullInfo> {
        return localRepo.getAllCityWeatherFullInfo()
    }

    private suspend fun getCitiesList() = withContext(SupervisorJob() + Dispatchers.IO) {
        try {
            launch {
                val temp = localRepo.getCitiesCoordinates()
                withContext(Dispatchers.Main) {
                    cities.clear()
                    cities.addAll(temp)
                }
            }.join()
            true
        } catch (e: Throwable) {
            Log.d("HomeViewModel.kt: getCitiesList()", e.toString())
            false
        }
    }

    private suspend fun getCitiesInfoFromApi(language: String) = withContext(
        SupervisorJob() + Dispatchers.IO
    ) {
        try {
            launch {
                resultForAllCitiesFromApi.clear()
                cities.forEach { resultForAllCitiesFromApi.add(loadOneCityInfoFromApi(it, language)) }
                Log.d(
                    "HomeViewModel.kt: getCitiesInfoFromApi()",
                    "SuccessDownloadInfoFromApi $resultForAllCitiesFromApi"
                )
                withContext(Dispatchers.Main) {
                    cityWeatherList.postValue(resultForAllCitiesFromApi)
                }
            }.join()
            true
        } catch (e: Throwable) {
            Log.d("HomeViewModel.kt: getCitiesInfo()", e.toString())
            false
        }
    }

    private suspend fun getAllCitiesInfoFromRepo() = withContext(SupervisorJob() + Dispatchers.IO) {
        try {
            launch {
                resultForAllCitiesFromRepo.clear()
                resultForAllCitiesFromRepo.addAll(loadAllCitiesInfoFromRepo())
                Log.d(
                    "HomeViewModel.kt: getAllCitiesInfoFromRepo()",
                    "SuccessDownloadInfoFromRepo $resultForAllCitiesFromRepo"
                )
                if (resultForAllCitiesFromRepo.isNotEmpty()) {
                    withContext(Dispatchers.Main) {
                        cityWeatherList.postValue(resultForAllCitiesFromRepo)
                    }
                }
            }.join()
            true
        } catch (e: Throwable) {
            Log.d("HomeViewModel.kt: getCitiesInfoFromRepo()", e.toString())
            false
        }
    }

    private suspend fun loadCitiesToRepo(citiesWeatherFullInfo: List<CityWeatherFullInfo>) = withContext(
        SupervisorJob() + Dispatchers.IO
    ) {
        try {
            launch {
                citiesWeatherFullInfo.forEach { localRepo.addOneCityWeatherFullInfo(it) }
            }
            true
        } catch (e: Throwable) {
            Log.d("HomeViewModel.kt: loadCitiesToRepo()", e.toString())
            false
        }
    }
}
