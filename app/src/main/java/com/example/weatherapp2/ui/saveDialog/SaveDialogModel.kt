package com.example.weatherapp2.ui.saveDialog

import androidx.lifecycle.ViewModel
import com.example.weatherapp2.model.common.CityCoordinate
import com.example.weatherapp2.model.repository.LocalRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class SaveDialogModel(
    private val localRepo: LocalRepo
) : ViewModel() {

    fun putCityToRepo(city: CityCoordinate) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            localRepo.addCityCoordinate(city)
        }
    }
}
