package com.example.weatherapp2.ui.weatherFullInfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp2.model.repository.Repository

class WeatherFullInfoModelFactory(
    private val repository: Repository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = modelClass
        .getConstructor(Repository::class.java)
        .newInstance(repository)
}
