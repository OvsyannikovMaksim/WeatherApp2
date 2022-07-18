package com.example.weatherapp2.ui.inputCities

import androidx.lifecycle.ViewModel
import com.example.weatherapp2.model.common.CityCoordinate
import com.example.weatherapp2.model.repository.LocalRepo
import kotlinx.coroutines.*

class InputCitiesModel(private val localRepo: LocalRepo) : ViewModel() {

    fun addTwoCities() {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            localRepo.addCityCoordinate(CityCoordinate(55.80282, 37.97836))
            localRepo.addCityCoordinate(CityCoordinate(36.00769, 5.60755))
        }
    }
}
