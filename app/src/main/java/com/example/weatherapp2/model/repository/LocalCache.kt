package com.example.weatherapp2.model.repository

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build

class LocalCache(val context: Context) : MySharedPreference {

    override fun getMetaData(name: String): String {
        return getAppInfo(context).metaData.getString(name)!!
    }

    override fun getServiceUpdateTime(): Int {
        return getSharedPref(context).getInt(ServiceUpdateTimeTag, 30)
    }

    override fun setServiceUpdateTime(updateTime: Int) {
        getSharedPref(context).edit()
            .putInt(ServiceUpdateTimeTag, updateTime)
            .apply()
    }

    override fun getServiceState(): Boolean {
        return getSharedPref(context).getBoolean(ServiceStateTag, false)
    }

    override fun setServiceState(isServiceOn: Boolean) {
        getSharedPref(context).edit()
            .putBoolean(ServiceStateTag, isServiceOn)
            .apply()
    }

    override fun getChosenMapId(): Int {
        return getSharedPref(context).getInt(ChosenMapIdTag, DefaultChosenMap)
    }

    override fun setChosenMapId(chosenMapId: Int) {
        getSharedPref(context).edit()
            .putInt(ChosenMapIdTag, chosenMapId)
            .apply()
    }

    override fun getLastCityInNotification(): Int {
        return getSharedPref(context).getInt(LastCityInNotification, 0)
    }

    override fun putLastCityInNotification(cityId: Int) {
        getSharedPref(context).edit()
            .putInt(LastCityInNotification, cityId)
            .apply()
    }

    companion object {
        private const val SharedPreferencesTag = "SharedPreferencesTag"
        private const val ServiceUpdateTimeTag = "ServiceUpdateTimeTag"
        private const val ServiceStateTag = "ServiceStateTag"
        private const val ChosenMapIdTag = "ChosenMapIdTag"
        private const val LastCityInNotification = "LastCityInNotificationId"
        private const val DefaultChosenMap = 0

        private var preference: SharedPreferences? = null
        private var applicationInfo: ApplicationInfo? = null

        fun getSharedPref(context: Context): SharedPreferences {
            if (preference == null) {
                synchronized(LocalCache::class) {
                    preference = context.getSharedPreferences(
                        SharedPreferencesTag,
                        Context.MODE_PRIVATE
                    )
                }
            }
            return preference!!
        }

        fun getAppInfo(context: Context): ApplicationInfo {
            if (applicationInfo == null) {
                synchronized(LocalCache::class) {
                    applicationInfo = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                        context.packageManager.getApplicationInfo(
                            context.packageName,
                            PackageManager.GET_META_DATA
                        )
                    } else {
                        context.packageManager.getApplicationInfo(
                            context.packageName,
                            PackageManager.ApplicationInfoFlags.of(
                                PackageManager.GET_META_DATA.toLong()
                            )
                        )
                    }
                }
            }
            return applicationInfo!!
        }
    }
}
