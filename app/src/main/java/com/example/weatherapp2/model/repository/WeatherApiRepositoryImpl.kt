package com.example.weatherapp2.model.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp2.model.api.OpenWeatherApi
import com.example.weatherapp2.model.common.CityFullInfo
import com.example.weatherapp2.model.db.LocalDao
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherApiRepositoryImpl(
    val localDao: LocalDao,
    val openWeatherApi: OpenWeatherApi
) : Repository {

    private val apiKeyOpenWeather: String = LocalDataCache.getMetaData("openWeatherApiKey")
    private val excludeFullInfo: String = "minutely,hourly,alerts"

    override val cityFullInfo: MutableList<CityFullInfo> = mutableListOf()
    override val resultForAllCitiesFromRepo: MutableList<CityFullInfo> = mutableListOf()
    override val resultForAllCitiesFromApi: MutableList<CityFullInfo> = mutableListOf()
    override var cityWeatherList: MutableLiveData<List<CityFullInfo>> = MutableLiveData()

    override suspend fun getAllCitiesInfoFromRepo(): Unit = withContext(Dispatchers.IO) {
        try {
            resultForAllCitiesFromRepo.clear()
            for (cityFullInfo in localDao.getAllCityFullInfo()) {
                if (cityFullInfo.current != null) {
                    resultForAllCitiesFromRepo.add(cityFullInfo)
                }
            }
            if (resultForAllCitiesFromRepo.isNotEmpty()) {
                cityWeatherList.postValue(resultForAllCitiesFromRepo)
            }
        } catch (e: Throwable) {
            Log.d("WeatherApiRepositoryImpl.kt: getCitiesCoordinatesList()", e.toString())
        }
    }

    override suspend fun getCitiesCoordinates(): Unit = withContext(Dispatchers.IO) {
        try {
            launch {
                cityFullInfo.clear()
                cityFullInfo.addAll(localDao.getAllCityFullInfo())
            }.join()
        } catch (e: Throwable) {
            Log.d("WeatherApiRepositoryImpl.kt: getCitiesCoordinatesList()", e.toString())
        }
    }

    override suspend fun getCitiesInfoFromApi(language: String) = withContext(Dispatchers.IO) {
        try {
            launch {
                resultForAllCitiesFromApi.clear()
                if (LocalDataCache.getInternetAccess()) {
                    cityFullInfo.forEach {
                        resultForAllCitiesFromApi.add(
                            getCityWeatherFullInfo(it, language)
                        )
                    }
                    withContext(Dispatchers.Main) {
                        cityWeatherList.postValue(resultForAllCitiesFromApi)
                    }
                }
            }.join()
            true
        } catch (e: Throwable) {
            Log.d("WeatherApiRepositoryImpl.kt: getCitiesInfoFromApi()", e.toString())
            false
        }
    }

    override suspend fun putCitiesToRepo(): Unit = withContext(Dispatchers.IO) {
        try {
            for (cityFullInfo in resultForAllCitiesFromApi) {
                if (localDao.getOneCityFullInfo(cityFullInfo.id!!) != null) {
                    localDao.updateCityFullInfo(cityFullInfo)
                } else {
                    localDao.insertCityFullInfo(cityFullInfo)
                }
            }
        } catch (e: Throwable) {
            Log.d("WeatherApiRepositoryImpl.kt: putCitiesToRepo()", e.toString())
        }
    }

    private suspend fun getCityWeatherFullInfo(
        cityFullInfo: CityFullInfo,
        lang: String
    ): CityFullInfo {
        val result = openWeatherApi.getCityWeatherInfo(
            cityFullInfo.lat.toString(),
            cityFullInfo.lon.toString(),
            excludeFullInfo,
            lang,
            "metric",
            apiKeyOpenWeather
        )
        result.name = cityFullInfo.name
        result.comment = cityFullInfo.comment
        result.state = cityFullInfo.state
        result.country = cityFullInfo.country
        result.pic = cityFullInfo.pic
        result.id = cityFullInfo.id
        result.updateTime = SimpleDateFormat("HH:mm", Locale.US).format(Date())
        return result
    }
}
