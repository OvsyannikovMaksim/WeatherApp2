package com.example.weatherapp2.model.repository

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

object LocalDataCache {

    private lateinit var preferences: SharedPreferences
    private lateinit var applicationInfo: ApplicationInfo
    private const val SharedPreferencesTag = "SharedPreferencesTag"
    private const val ServiceUpdateTimeTag = "ServiceUpdateTimeTag"
    private const val ServiceStateTag = "ServiceStateTag"
    private const val ChosenMapIdTag = "ChosenMapIdTag"
    private const val LastCityInNotification = "LastCityInNotificationId"
    private const val InternetIsOn = "InternetIsOnId"
    private const val DefaultChosenMap = 0

    fun init(context: Context) {
        preferences = context.getSharedPreferences(
            SharedPreferencesTag,
            Context.MODE_PRIVATE
        )
        applicationInfo = context.packageManager.getApplicationInfo(
            context.packageName,
            PackageManager.GET_META_DATA
        )
    }

    fun getMetaData(name: String): String {
        return applicationInfo.metaData[name].toString()
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

    fun getLastCityInNotification(): Int {
        return preferences.getInt(LastCityInNotification, 0)
    }

    fun putLastCityInNotification(cityId: Int) {
        preferences.edit()
            .putInt(LastCityInNotification, cityId)
            .apply()
    }

    fun getInternetAccess(): Boolean {
        return preferences.getBoolean(InternetIsOn, false)
    }

    fun setInternetAccess(isOn: Boolean) {
        preferences.edit()
            .putBoolean(InternetIsOn, isOn)
            .apply()
    }
}
