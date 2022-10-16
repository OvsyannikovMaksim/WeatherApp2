package com.example.weatherapp2.model

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.example.weatherapp2.MainActivity
import com.example.weatherapp2.R
import com.example.weatherapp2.model.common.CityFullInfo
import com.example.weatherapp2.model.repository.OpenWeatherRepositoryImpl
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.pow
import kotlin.math.sqrt
import kotlinx.coroutines.SupervisorJob

@AndroidEntryPoint
class WeatherNotificationService : Service() {

    private lateinit var notificationManager: NotificationManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var cancellationTokenSource = CancellationTokenSource()

    @Inject
    lateinit var repository: OpenWeatherRepositoryImpl
    private val job = SupervisorJob()

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        repository.dbUpdateLiveData().observeForever {
            getCityForNotificationAndLaunchNotification(it)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        notificationManager.cancel(1)
        job.cancel()
    }

    private fun createNotification(cityFullInfo: CityFullInfo) {
        val bundle = Bundle()
        bundle.putDoubleArray(
            "FullInfoKey",
            doubleArrayOf(cityFullInfo.lat, cityFullInfo.lon)
        )
        val pendingIntent = NavDeepLinkBuilder(this)
            .setGraph(R.navigation.mobile_navigation)
            .setDestination(R.id.navigation_weatherFullInfoFragment)
            .setComponentName(MainActivity::class.java)
            .setArguments(bundle)
            .createPendingIntent()
        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_location_on_24)
            .setContentTitle("Weather in ${cityFullInfo.name}, ${cityFullInfo.country}")
            .setContentText(getString(R.string.celsius, cityFullInfo.current!!.temp.toInt()))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOngoing(true)
            .setAutoCancel(false)
            .setSilent(true)
            .setContentIntent(pendingIntent)
        val notification = builder.build()
        notificationManager.notify(1, notification)
    }

    @SuppressLint("MissingPermission")
    private fun getCityForNotificationAndLaunchNotification(citiesWeather: List<CityFullInfo>) {
        var result: CityFullInfo?
        val locationTask = fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_LOW_POWER,
            cancellationTokenSource.token
        )
        locationTask.addOnCompleteListener {
            if (it.isSuccessful && it.result != null) {
                result = findNearest(it.result.latitude, it.result.longitude, citiesWeather)
                if (result != null) {
                    repository.putLastCityInNotification(result!!.id!!)
                }
            } else if (repository.getLastCityInNotification() != 0) {
                result = findLastInNotification(
                    repository.getLastCityInNotification(),
                    citiesWeather
                )
            } else {
                result = findLast(citiesWeather)
            }
            if (result != null && result!!.current != null) {
                createNotification(result!!)
            }
        }
    }

    private fun findNearest(lat: Double, lon: Double, citiesWeather: List<CityFullInfo>): CityFullInfo? {
        val distance = mutableListOf<Double>()
        citiesWeather.forEach { distance.add(findMinDistance(it.lon, it.lat, lon, lat)) }
        return if (distance.minOrNull() != null) {
            distance.indexOf(distance.minOrNull())
            citiesWeather[distance.indexOf(distance.minOrNull())]
        } else {
            null
        }
    }

    private fun findLastInNotification(cityId: Int, citiesWeather: List<CityFullInfo>): CityFullInfo? {
        if (citiesWeather.isNotEmpty()) {
            for (cityWeather in citiesWeather) {
                if (cityWeather.id == cityId) {
                    return cityWeather
                }
            }
        }
        return null
    }

    private fun findLast(citiesWeather: List<CityFullInfo>): CityFullInfo? {
        return if (citiesWeather.isNotEmpty()) {
            citiesWeather.last()
        } else {
            null
        }
    }

    private fun findMinDistance(cityLon: Double, cityLat: Double, Lon: Double, Lat: Double): Double {
        return sqrt(((cityLon - Lon).pow(2) + (cityLat - Lat).pow(2)))
    }

    companion object {
        const val CHANNEL_ID = "WeatherApp2Channel"
    }
}
