package com.example.weatherapp2.ui.saveDialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp2.model.common.CityFullInfo
import com.example.weatherapp2.model.repository.Repository
import kotlinx.coroutines.launch

class SaveDialogModel(
    private val repository: Repository
) : ViewModel() {

    fun putCityToRepo(cityFullInfo: CityFullInfo) {
        viewModelScope.launch {
            repository.putCityToRepo(cityFullInfo)
        }
    }
}
