package com.example.weatherapp2.ui.weatherFullInfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp2.model.repository.LocalRepo

class WeatherFullInfoModelFactory(
    private val localRepo: LocalRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(WeatherFullInfoFragment::class.java)
            .newInstance(localRepo)
    }
}
