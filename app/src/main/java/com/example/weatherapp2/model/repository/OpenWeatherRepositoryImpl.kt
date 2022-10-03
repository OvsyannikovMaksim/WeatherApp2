package com.example.weatherapp2.model.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.weatherapp2.model.common.CityFullInfo
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OpenWeatherRepositoryImpl(
    val dao: Dao,
    val api: Api
) : Repository {

    private val apiKeyOpenWeather: String = LocalDataCache.getMetaData("openWeatherApiKey")

    object SharedPref {
        private lateinit var preferences: SharedPreferences

        fun init(context: Context) {
            preferences = context.getSharedPreferences(
                SharedPreferencesTag,
                Context.MODE_PRIVATE
            )
        }
    }

    override fun dbUpdateLiveData(): LiveData<List<CityFullInfo>> {
        return dao.dbUpdateLiveData()
    }

    override suspend fun getCitiesInfoAndLoadItToLocalRepo(language: String): Unit = withContext(
        Dispatchers.IO
    ) {
        launch {
            try {
                val citiesFromRepo = mutableListOf<CityFullInfo>()
                val resultFromApi = mutableListOf<CityFullInfo>()
                citiesFromRepo.addAll(dao.getAllCityFullInfo())
                if (LocalDataCache.getInternetAccess()) {
                    citiesFromRepo.forEach {
                        resultFromApi.add(getCityWeatherFullInfo(it, language))
                    }
                    for (cityFullInfo in resultFromApi) {
                        if (dao.getOneCityFullInfo(cityFullInfo.id!!) != null) {
                            dao.updateCityFullInfo(cityFullInfo)
                        } else {
                            dao.insertCityFullInfo(cityFullInfo)
                        }
                    }
                }
            } catch (e: Throwable) {
                Log.d(
                    "WeatherApiRepositoryImpl.kt: getCitiesInfoAndLoadItToLocalRepo()",
                    e.toString()
                )
            }
        }
    }

    override suspend fun getCityByName(cityName: String): List<CityFullInfo> {
        return api.getCityCoordinateByName(cityName, "5", apiKeyOpenWeather)
    }

    override suspend fun getOneCityFullInfo(latitude: Double, longitude: Double): CityFullInfo? {
        return dao.getOneCityFullInfo(latitude, longitude)
    }

    override suspend fun insertCityFullInfo(cityFullInfo: CityFullInfo) {
        dao.insertCityFullInfo(cityFullInfo)
    }

    override suspend fun updateCityFullInfo(cityFullInfo: CityFullInfo) {
        dao.updateCityFullInfo(cityFullInfo)
    }

    override suspend fun getCityByCoordinates(lat: String, lon: String): CityFullInfo? {
        val result = api.getCityNameByCoordinate(lat, lon, "1", apiKeyOpenWeather)
        return if (result != emptyList<CityFullInfo>()) {
            result.first()
        } else {
            null
        }
    }

    override suspend fun deleteCityFullInfo(cityFullInfo: CityFullInfo) {
        dao.deleteCityFullInfo(cityFullInfo)
    }

    override suspend fun getOneCityFullInfo(id: Int): CityFullInfo? {
        return dao.getOneCityFullInfo(id)
    }

    private suspend fun getCityWeatherFullInfo(
        cityFullInfo: CityFullInfo,
        lang: String
    ): CityFullInfo {
        val result = api.getCityWeatherInfo(
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

    companion object {
        private const val SharedPreferencesTag = "SharedPreferencesTag"
        private const val excludeFullInfo: String = "minutely,hourly,alerts"
    }
}
