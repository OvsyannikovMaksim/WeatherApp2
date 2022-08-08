package com.example.weatherapp2.ui.saveDialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp2.model.repository.LocalRepo

class SaveDialogModelFactory(
    private val localRepo: LocalRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(SaveDialogModel::class.java)
            .newInstance(localRepo)
    }
}
