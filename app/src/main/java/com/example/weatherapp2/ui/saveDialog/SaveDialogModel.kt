package com.example.weatherapp2.ui.saveDialog

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.weatherapp2.model.common.CityFullInfo
import com.example.weatherapp2.model.repository.LocalRepo
import kotlin.math.roundToInt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class SaveDialogModel(
    private val localRepo: LocalRepo
) : ViewModel() {

    fun putCityToRepo(cityFullInfo: CityFullInfo) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            val lat = (cityFullInfo.lat * 10000).roundToInt() / 10000.0
            val lon = (cityFullInfo.lon * 10000).roundToInt() / 10000.0
            if (localRepo.getOneCityFullInfo(lat, lon) != null) {
                Log.d("TAG", "update")
                localRepo.updateCityFullInfo(cityFullInfo)
            } else {
                Log.d("TAG", "insert")
                localRepo.insertCityFullInfo(cityFullInfo)
            }
        }
    }
}
