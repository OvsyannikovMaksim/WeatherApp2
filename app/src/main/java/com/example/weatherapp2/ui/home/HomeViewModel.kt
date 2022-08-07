package com.example.weatherapp2.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp2.model.common.CityFullInfo
import com.example.weatherapp2.model.repository.CityWeatherRepoImpl
import com.example.weatherapp2.model.repository.LocalRepo
import kotlinx.coroutines.*

class HomeViewModel(
    private val cityWeatherRepoImpl: CityWeatherRepoImpl,
    private val localRepo: LocalRepo
) : ViewModel() {

    var cityWeatherList: MutableLiveData<List<CityFullInfo>> = MutableLiveData()
    private val cityFullInfo = mutableListOf<CityFullInfo>()
    private val resultForAllCitiesFromApi = mutableListOf<CityFullInfo>()
    private val resultForAllCitiesFromRepo = mutableListOf<CityFullInfo>()

    fun getCitiesInfoAndLoadItToLocalRepo(language: String) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            getCitiesCoordinatesList()
            Log.d("HomeViewModel.kt: getCitiesInfoAndLoadItToLocalRepo()", cityFullInfo.toString())
            delay(1000)
            getAllCitiesInfoFromRepo()
            delay(2000)
            if (getCitiesInfoFromApi(language) && resultForAllCitiesFromApi.isNotEmpty()) {
                Log.d("HomeViewModel.kt: getCitiesInfoAndLoadItToLocalRepo()", "Download to repo")
                loadCitiesToRepo(resultForAllCitiesFromApi)
            }
        }
    }

    private suspend fun loadOneCityInfoFromApi(cityFullInfo: CityFullInfo, language: String): CityFullInfo {
        return cityWeatherRepoImpl.getCityWeatherFullInfo(cityFullInfo, language)
    }

    private suspend fun getCitiesCoordinatesList() = withContext(SupervisorJob() + Dispatchers.IO) {
        try {
            launch {
                val temp = localRepo.getAllCityFullInfo()
                withContext(Dispatchers.Main) {
                    cityFullInfo.clear()
                    cityFullInfo.addAll(temp)
                }
            }.join()
            true
        } catch (e: Throwable) {
            Log.d("HomeViewModel.kt: getCitiesCoordinatesList()", e.toString())
            false
        }
    }

    private suspend fun getCitiesInfoFromApi(language: String) = withContext(
        SupervisorJob() + Dispatchers.IO
    ) {
        try {
            launch {
                resultForAllCitiesFromApi.clear()
                cityFullInfo.forEach {
                    resultForAllCitiesFromApi.add(
                        loadOneCityInfoFromApi(it, language)
                    )
                }
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
                for (cityFullInfo in localRepo.getAllCityFullInfo()) {
                    if (cityFullInfo.current != null) {
                        resultForAllCitiesFromRepo.add(cityFullInfo)
                    }
                }
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

    private suspend fun loadCitiesToRepo(citiesFullInfo: List<CityFullInfo>) = withContext(
        SupervisorJob() + Dispatchers.IO
    ) {
        try {
            launch {
                Log.d("HomeViewModel.kt: loadCitiesToRepo() input:", citiesFullInfo.toString())

                for (cityFullInfo in citiesFullInfo) {
                    if (localRepo.getOneCityFullInfo(cityFullInfo.id!!) != null) {
                        localRepo.updateCityFullInfo(cityFullInfo)
                    } else {
                        localRepo.insertCityFullInfo(cityFullInfo)
                    }
                }
            }
            true
        } catch (e: Throwable) {
            Log.d("HomeViewModel.kt: loadCitiesToRepo()", e.toString())
            false
        }
    }
}
