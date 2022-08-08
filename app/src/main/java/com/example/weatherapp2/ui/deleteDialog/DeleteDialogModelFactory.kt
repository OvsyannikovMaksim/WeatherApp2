package com.example.weatherapp2.ui.deleteDialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp2.model.repository.LocalRepo

class DeleteDialogModelFactory(
    private val localRepo: LocalRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeleteDialogModel::class.java)) {
            return DeleteDialogModel(localRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
