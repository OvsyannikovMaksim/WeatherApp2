package com.example.weatherapp2.ui.weatherFullInfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp2.model.repository.LocalRepo

class WeatherFullInfoModelFactory(
    private val localRepo: LocalRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherFullInfoModel::class.java)) {
            return WeatherFullInfoModel(localRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
