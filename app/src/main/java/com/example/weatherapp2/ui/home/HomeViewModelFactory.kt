package com.example.weatherapp2.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp2.model.repository.CityWeatherRepoImpl
import com.example.weatherapp2.model.repository.LocalRepo

class HomeViewModelFactory(
    private val cityWeatherRepoImpl: CityWeatherRepoImpl,
    private val localRepo: LocalRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = modelClass
        .getConstructor(CityWeatherRepoImpl::class.java, LocalRepo::class.java)
        .newInstance(cityWeatherRepoImpl, localRepo)
}
