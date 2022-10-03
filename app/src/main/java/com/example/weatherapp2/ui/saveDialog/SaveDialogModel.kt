package com.example.weatherapp2.ui.saveDialog

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp2.model.common.CityFullInfo
import com.example.weatherapp2.model.repository.Repository
import kotlin.math.roundToInt
import kotlinx.coroutines.launch

class SaveDialogModel(
    private val repository: Repository
) : ViewModel() {

    fun putCityToRepo(cityFullInfo: CityFullInfo) {
        viewModelScope.launch {
            val lat = (cityFullInfo.lat * 10000).roundToInt() / 10000.0
            val lon = (cityFullInfo.lon * 10000).roundToInt() / 10000.0
            if (repository.getOneCityFullInfo(lat, lon) != null) {
                Log.d("TAG", "update")
                repository.updateCityFullInfo(cityFullInfo)
            } else {
                Log.d("TAG", "insert")
                repository.insertCityFullInfo(cityFullInfo)
            }
        }
    }
}
