package com.example.weatherapp2.ui.inputCities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp2.model.repository.LocalRepo

class InputCitiesModelFactory(private val localRepo: LocalRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InputCitiesModel::class.java)) {
            return InputCitiesModel(localRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
