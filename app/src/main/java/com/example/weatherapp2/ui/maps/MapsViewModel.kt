package com.example.weatherapp2.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp2.model.common.CityFullInfo
import com.example.weatherapp2.model.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    val citiesFullInfo: LiveData<List<CityFullInfo>> = repository.dbUpdateLiveData()

    fun getChosenMapId(): Int {
        return repository.getChosenMapId()
    }
}
