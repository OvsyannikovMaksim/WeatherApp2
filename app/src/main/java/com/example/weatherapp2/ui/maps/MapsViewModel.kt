package com.example.weatherapp2.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp2.model.common.CityFullInfo
import com.example.weatherapp2.model.repository.Repository

class MapsViewModel(repository: Repository) : ViewModel() {
    val citiesFullInfo: LiveData<List<CityFullInfo>> = repository.dbUpdateLiveData()
}
