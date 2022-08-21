package com.example.weatherapp2.model

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.weatherapp2.model.api.OpenWeatherApiRetrofit
import com.example.weatherapp2.model.common.CityFullInfo
import com.example.weatherapp2.model.db.DataBase
import com.example.weatherapp2.model.repository.CityWeatherRepoImpl
import com.example.weatherapp2.model.repository.LocalRepo
import com.example.weatherapp2.model.repository.LocalRepoImpl
import kotlinx.coroutines.*

class UpdateWeatherWorker(
    context: Context,
    workerParams: WorkerParameters,
) :
    Worker(context, workerParams) {

    val localRepo: LocalRepo = LocalRepoImpl(DataBase.getDataBase(context)!!.localDao())
    val cityWeatherRepoImpl: CityWeatherRepoImpl = CityWeatherRepoImpl(OpenWeatherApiRetrofit.openWeatherApi)

    private val cityFullInfo = mutableListOf<CityFullInfo>()
    private val resultForAllCitiesFromApi = mutableListOf<CityFullInfo>()

    override fun doWork(): Result {
        try {
            getCitiesInfoAndLoadItToLocalRepo("en")
        } catch (e: Exception) {
            Log.d("UpdateWeatherWorker.kt", e.toString())
            return Result.failure()
        }
        return Result.success()
    }

    private fun getCitiesInfoAndLoadItToLocalRepo(language: String) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            getCitiesCoordinatesList()
            if (getCitiesInfoFromApi(language) && resultForAllCitiesFromApi.isNotEmpty()) {
                loadCitiesToRepo(resultForAllCitiesFromApi)
            }
        }
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
            }.join()
            true
        } catch (e: Throwable) {
            Log.d("HomeViewModel.kt: getCitiesInfoFromApi()", e.toString())
            false
        }
    }

    private suspend fun loadOneCityInfoFromApi(cityFullInfo: CityFullInfo, language: String): CityFullInfo {
        return cityWeatherRepoImpl.getCityWeatherFullInfo(cityFullInfo, language)
    }

    private suspend fun loadCitiesToRepo(citiesFullInfo: List<CityFullInfo>) = withContext(
        SupervisorJob() + Dispatchers.IO
    ) {
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
}
