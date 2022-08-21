package com.example.weatherapp2.ui.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp2.model.repository.LocalRepo

class MapsViewModelFactory(
    private val localRepo: LocalRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = modelClass
        .getConstructor(LocalRepo::class.java)
        .newInstance(localRepo)
}