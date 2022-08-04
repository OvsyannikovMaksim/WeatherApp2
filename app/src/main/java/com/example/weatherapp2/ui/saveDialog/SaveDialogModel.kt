package com.example.weatherapp2.ui.saveDialog

import androidx.lifecycle.ViewModel
import com.example.weatherapp2.model.common.CityFullInfo
import com.example.weatherapp2.model.repository.LocalRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class SaveDialogModel(
    private val localRepo: LocalRepo
) : ViewModel() {

    fun putCityToRepo(cityFullInfo: CityFullInfo) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            if (localRepo.getOneCityFullInfo(cityFullInfo.lat, cityFullInfo.lon) != null) {
                localRepo.updateCityFullInfo(cityFullInfo)
            } else {
                localRepo.insertCityFullInfo(cityFullInfo)
            }
        }
    }
}
