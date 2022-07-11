package com.example.weatherapp2.model.api

object OpenWeatherApiRetrofit {

    val openWeatherApi: OpenWeatherApi
        get() = OpenWeatherApiRetrofitClient.getClient().create(OpenWeatherApi::class.java)
}
