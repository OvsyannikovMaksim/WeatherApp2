package com.example.weatherapp2.ui.mapCityInput

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp2.model.repository.CityWeatherRepoImpl

class MapCityInputModelFactory(
    private val cityWeatherRepoImpl: CityWeatherRepoImpl
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = modelClass
        .getConstructor(CityWeatherRepoImpl::class.java)
        .newInstance(cityWeatherRepoImpl)
}