package com.example.weatherapp2.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp2.model.common.CityFullInfo
import com.example.weatherapp2.model.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    var cityWeatherList: LiveData<List<CityFullInfo>> = repository.dbUpdateLiveData()

    fun getCitiesInfoAndLoadItToLocalRepo(language: String) {
        viewModelScope.launch {
            repository.getCitiesInfoAndLoadItToLocalRepo(language)
        }
    }
}
