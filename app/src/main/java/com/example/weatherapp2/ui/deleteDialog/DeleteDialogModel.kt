package com.example.weatherapp2.ui.deleteDialog

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.weatherapp2.model.repository.LocalRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class DeleteDialogModel(private val localRepo: LocalRepo) : ViewModel() {

    fun deleteCity(cityId: Int) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            Log.d("TAG", cityId.toString())
            localRepo.getOneCityFullInfo(cityId)?.let { localRepo.deleteCityFullInfo(it) }
        }
    }
}
