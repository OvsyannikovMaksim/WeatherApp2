package com.example.weatherapp2.model.repository

import com.example.weatherapp2.model.api.OpenWeatherApi
import com.example.weatherapp2.model.common.CityFullInfo

class CityWeatherRepoImpl(private val openWeatherApi: OpenWeatherApi) : CityWeatherRepo {

    private val apiKeyOpenWeather: String = "4eddd7394f54a0dd81465aa802a837f5"
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
        return result
    }

    override suspend fun getCityCoordinateByName(cityName: String): List<CityFullInfo> {
        return openWeatherApi.getCityCoordinateByName(cityName, "5", apiKeyOpenWeather)
    }
}
