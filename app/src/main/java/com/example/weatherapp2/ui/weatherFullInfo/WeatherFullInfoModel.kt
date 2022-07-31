package com.example.weatherapp2.ui.weatherFullInfo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp2.model.common.CityCoordinate
import com.example.weatherapp2.model.repository.LocalRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class WeatherFullInfoModel(val localRepo: LocalRepo) : ViewModel() {

    var cityCoordinate: MutableLiveData<CityCoordinate> = MutableLiveData()

    fun getCityFromRepo(id: Int) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            cityCoordinate.postValue(localRepo.getOneCity(id))
        }
    }
}
