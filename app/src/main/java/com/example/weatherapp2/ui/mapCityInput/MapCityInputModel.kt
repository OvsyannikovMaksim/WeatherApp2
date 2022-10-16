package com.example.weatherapp2.ui.mapCityInput

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp2.model.common.CityFullInfo
import com.example.weatherapp2.model.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class MapCityInputModel @Inject constructor(private val repository: Repository) : ViewModel() {

    var resultOfSearch: MutableLiveData<CityFullInfo> = MutableLiveData()

    fun getCitiesFromLine(lat: String, lon: String) {
        viewModelScope.launch {
            resultOfSearch.postValue(repository.getCityByCoordinates(lat, lon))
        }
    }

    fun getChosenMapId(): Int {
        return repository.getChosenMapId()
    }
}
