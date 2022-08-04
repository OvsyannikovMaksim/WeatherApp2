package com.example.weatherapp2.model.repository

import com.example.weatherapp2.model.api.OpenWeatherApi
import com.example.weatherapp2.model.common.CityFullInfo
import com.example.weatherapp2.model.common.CityInfo

class CityWeatherRepoImpl(private val openWeatherApi: OpenWeatherApi) : CityWeatherRepo {

    private val API_KEY_OPEN_WEATHER: String = "4eddd7394f54a0dd81465aa802a837f5"
    private val EXCLUDE_FULL_INFO: String = "minutely,hourly,alerts"

    override suspend fun getCityWeatherFullInfo(
        cityInfo: CityInfo,
        lang: String
    ): CityFullInfo {
        val result = openWeatherApi.getCityWeatherInfo(
            cityInfo.lat.toString(),
            cityInfo.lon.toString(),
            EXCLUDE_FULL_INFO,
            lang,
            "metric",
            API_KEY_OPEN_WEATHER
        )
        result.name = cityInfo.name
        result.comment = cityInfo.comment
        result.state = cityInfo.state
        result.country = cityInfo.country
        result.pic = cityInfo.pic
        result.id = cityInfo.id
        return result
    }

    override suspend fun getCityCoordinateByName(cityName: String): List<CityInfo> {
        return openWeatherApi.getCityCoordinateByName(cityName, "5", API_KEY_OPEN_WEATHER)
    }
}
