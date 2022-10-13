package com.example.weatherapp2.ui.weatherFullInfo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp2.model.common.CityFullInfo
import com.example.weatherapp2.model.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class WeatherFullInfoModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    var cityCoordinate: MutableLiveData<CityFullInfo> = MutableLiveData()

    fun getCityFromRepo(lat: Double, lon: Double) {
        viewModelScope.launch {
            cityCoordinate.postValue(repository.getOneCityFullInfo(lat, lon))
        }
    }
}
