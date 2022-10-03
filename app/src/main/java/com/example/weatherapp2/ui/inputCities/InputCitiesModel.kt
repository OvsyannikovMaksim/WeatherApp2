package com.example.weatherapp2.ui.inputCities

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp2.model.common.CityFullInfo
import com.example.weatherapp2.model.repository.Repository
import kotlinx.coroutines.launch

class InputCitiesModel(
    private val repository: Repository
) : ViewModel() {

    var resultOfSearch: MutableLiveData<List<CityFullInfo>> = MutableLiveData()

    fun getCitiesFromLine(cityName: String) {
        viewModelScope.launch {
            resultOfSearch.postValue(repository.getCityByName(cityName))
        }
    }
}
