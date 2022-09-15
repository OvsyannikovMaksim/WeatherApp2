package com.example.weatherapp2.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp2.model.common.CityFullInfo
import com.example.weatherapp2.model.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: Repository
    //private val cityWeatherRepoImpl: CityWeatherRepoImpl,
    //private val localRepo: LocalRepo
) : ViewModel() {

    var cityWeatherList: MutableLiveData<List<CityFullInfo>> = repository.cityWeatherList
    private val viewModelScope = CoroutineScope(SupervisorJob())

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
/*
    private suspend fun loadOneCityInfoFromApi(cityFullInfo: CityFullInfo, language: String): CityFullInfo {
        return cityWeatherRepoImpl.getCityWeatherFullInfo(cityFullInfo, language)
    }

    private suspend fun getCitiesCoordinatesList() = withContext(job + Dispatchers.IO) {
        Log.d("getCitiesCoordinatesList", "here")
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

    private suspend fun getCitiesInfoFromApi(language: String) = withContext(job + Dispatchers.IO) {
        try {
            launch {
                resultForAllCitiesFromApi.clear()
                if (LocalDataCache.getInternetAccess()) {
                    cityFullInfo.forEach {
                        resultForAllCitiesFromApi.add(
                            loadOneCityInfoFromApi(it, language)
                        )
                    }
                    withContext(Dispatchers.Main) {
                        cityWeatherList.postValue(resultForAllCitiesFromApi)
                    }
                }
            }.join()
            true
        } catch (e: Throwable) {
            Log.d("HomeViewModel.kt: getCitiesInfoFromApi()", e.toString())
            false
        }
    }

    private suspend fun getAllCitiesInfoFromRepo() = withContext(job + Dispatchers.IO) {
        Log.d("getAllCitiesInfoFromRepo", "here")
        try {
            launch {
                resultForAllCitiesFromRepo.clear()
                for (cityFullInfo in localRepo.getAllCityFullInfo()) {
                    if (cityFullInfo.current != null) {
                        resultForAllCitiesFromRepo.add(cityFullInfo)
                    }
                }
                if (resultForAllCitiesFromRepo.isNotEmpty()) {
                    Log.d("TAG", resultForAllCitiesFromRepo.toString())
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
        job + Dispatchers.IO
    ) {
        Log.d("loadCitiesToRepo", "here")
        try {
            launch {
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
     */
}
