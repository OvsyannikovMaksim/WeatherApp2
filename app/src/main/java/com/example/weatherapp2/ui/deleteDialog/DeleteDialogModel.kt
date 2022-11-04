package com.example.weatherapp2.ui.deleteDialog

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
            repository.deleteCityFullInfo(cityId)
        }
    }
}
