package com.example.weatherapp2.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp2.model.common.CityFullInfo
import com.example.weatherapp2.model.repository.LocalRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MapsViewModel(private val localRepo: LocalRepo) : ViewModel() {

   private val citiesFullInfo: MutableLiveData<List<CityFullInfo>> by lazy{
       MutableLiveData<List<CityFullInfo>>().also {
            loadCitiesFullInfoFromRepo()
       }
   }

    fun getCitiesFullInfo(): LiveData<List<CityFullInfo>>{
        return citiesFullInfo
    }

    private fun loadCitiesFullInfoFromRepo(){
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            citiesFullInfo.postValue(localRepo.getAllCityFullInfo())
        }
    }
}
