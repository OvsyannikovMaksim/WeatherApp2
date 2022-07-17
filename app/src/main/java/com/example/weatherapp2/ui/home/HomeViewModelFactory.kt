package com.example.weatherapp2.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp2.model.repository.CityWeatherRepoImpl

class HomeViewModelFactory(
    private val cityWeatherRepoImpl: CityWeatherRepoImpl
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(cityWeatherRepoImpl) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
