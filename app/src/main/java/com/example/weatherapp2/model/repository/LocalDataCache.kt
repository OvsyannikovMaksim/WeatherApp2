package com.example.weatherapp2.model.repository

import android.content.Context
import android.content.SharedPreferences

object LocalDataCache {

    private lateinit var preferences: SharedPreferences
    private const val SharedPreferencesTag = "SharedPreferencesTag"
    private const val ServiceUpdateTimeTag = "ServiceUpdateTimeTag"
    private const val ServiceStateTag = "ServiceStateTag"
    private const val ChosenMapIdTag = "ChosenMapIdTag"
    private const val DefaultChosenMap = 0

    fun init(context: Context) {
        preferences = context.getSharedPreferences(
            SharedPreferencesTag,
            Context.MODE_PRIVATE
        )
    }

    fun getServiceUpdateTime(): Int {
        return preferences.getInt(ServiceUpdateTimeTag, 30)
    }

    fun setServiceUpdateTime(updateTime: Int) {
        preferences.edit()
            .putInt(ServiceUpdateTimeTag, updateTime)
            .apply()
    }

    fun getServiceState(): Boolean {
        return preferences.getBoolean(ServiceStateTag, false)
    }

    fun setServiceState(isServiceOn: Boolean) {
        preferences.edit()
            .putBoolean(ServiceStateTag, isServiceOn)
            .apply()
    }

    fun getChosenMapId(): Int {
        return preferences.getInt(ChosenMapIdTag, DefaultChosenMap)
    }

    fun setChosenMapId(chosenMapId: Int) {
        preferences.edit()
            .putInt(ChosenMapIdTag, chosenMapId)
            .apply()
    }
}
