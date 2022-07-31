package com.example.weatherapp2.ui.deleteDialog

import androidx.lifecycle.ViewModel
import com.example.weatherapp2.model.common.CityCoordinate
import com.example.weatherapp2.model.common.openWeatherApi.CityWeatherFullInfo
import com.example.weatherapp2.model.repository.LocalRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class DeleteDialogModel(private val localRepo: LocalRepo) : ViewModel() {

    fun deleteCity(cityCoordinate: CityCoordinate, cityWeatherFullInfo: CityWeatherFullInfo) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            localRepo.deleteCityCoordinate(cityCoordinate)
            localRepo.deleteOneCityWeatherFullInfo(cityWeatherFullInfo)
        }
    }
}
