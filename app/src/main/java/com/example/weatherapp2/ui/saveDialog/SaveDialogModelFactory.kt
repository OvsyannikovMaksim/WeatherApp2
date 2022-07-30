package com.example.weatherapp2.ui.saveDialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp2.model.repository.LocalRepo

class SaveDialogModelFactory(
    private val localRepo: LocalRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SaveDialogModel::class.java)) {
            return SaveDialogModel(localRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
