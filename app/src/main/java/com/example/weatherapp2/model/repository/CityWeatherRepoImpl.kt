package com.example.weatherapp2.model.repository

import com.example.weatherapp2.model.api.OpenWeatherApi
import com.example.weatherapp2.model.common.CityFullInfo
import java.text.SimpleDateFormat
import java.util.*

class CityWeatherRepoImpl(private val openWeatherApi: OpenWeatherApi) : CityWeatherRepo {

    private val apiKeyOpenWeather: String = LocalDataCache.getMetaData("openWeatherApiKey")
    private val excludeFullInfo: String = "minutely,hourly,alerts"

    override suspend fun getCityWeatherFullInfo(
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

    override suspend fun getCityCoordinateByName(cityName: String): List<CityFullInfo> {
        return openWeatherApi.getCityCoordinateByName(cityName, "5", apiKeyOpenWeather)
    }

    override suspend fun getCityNameByCoordinates(lat: String, lon: String): CityFullInfo? {
        val result = openWeatherApi.getCityNameByCoordinate(lat, lon, "1", apiKeyOpenWeather)
        return if (result != emptyList<CityFullInfo>()) {
            result.first()
        } else {
            null
        }
    }
}
