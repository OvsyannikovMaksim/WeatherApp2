package com.example.weatherapp2.model.repository

interface MySharedPreference {

    fun getMetaData(name: String): String

    fun getServiceUpdateTime(): Int

    fun setServiceUpdateTime(updateTime: Int)

    fun getServiceState(): Boolean

    fun setServiceState(isServiceOn: Boolean)

    fun getChosenMapId(): Int

    fun setChosenMapId(chosenMapId: Int)

    fun getLastCityInNotification(): Int

    fun putLastCityInNotification(cityId: Int)
}
