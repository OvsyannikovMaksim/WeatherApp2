package com.example.weatherapp2.ui.deleteDialog

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp2.model.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class DeleteDialogModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    fun deleteCity(cityId: Int) {
        viewModelScope.launch {
            Log.d("TAG", cityId.toString())
            repository.getOneCityFullInfo(cityId)?.let { repository.deleteCityFullInfo(it) }
        }
    }
}
