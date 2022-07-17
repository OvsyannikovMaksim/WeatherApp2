package com.example.weatherapp2.model.repository

import com.example.weatherapp2.model.api.OpenWeatherApi
import com.example.weatherapp2.model.common.openWeatherApi.CityWeatherFullInfo

class CityWeatherRepoImpl(private val openWeatherApi: OpenWeatherApi) : CityWeatherRepo {

    private val API_KEY_OPEN_WEATHER: String = "4eddd7394f54a0dd81465aa802a837f5"
    private val EXCLUDE_FULL_INFO: String = "minutely,hourly,alerts"

    override suspend fun getCityWeatherFullInfo(
        latitude: String,
        longitude: String,
        lang: String
    ): CityWeatherFullInfo {
        return openWeatherApi.getCityWeatherInfo(
            latitude,
            longitude,
            EXCLUDE_FULL_INFO,
            lang,
            "metric",
            API_KEY_OPEN_WEATHER
        )
    }
}
