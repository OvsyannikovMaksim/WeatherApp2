package com.example.weatherapp2.model

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.weatherapp2.R
import com.example.weatherapp2.model.common.CityFullInfo
import com.example.weatherapp2.model.db.DataBase
import com.example.weatherapp2.model.repository.LocalDataCache
import com.example.weatherapp2.model.repository.LocalRepo
import com.example.weatherapp2.model.repository.LocalRepoImpl
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.*
import kotlin.math.pow
import kotlin.math.sqrt

class WeatherNotificationService : Service() {

    private lateinit var notificationManager: NotificationManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val localRepo: LocalRepo = LocalRepoImpl(DataBase.getDataBase(this)!!.localDao())
    private val job = SupervisorJob()
    private val CHANNEL_ID = "WeatherApp2Channel"

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        getCityForNotificationAndLaunchNotification()
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

    private fun createNotification(cityFullInfo: CityFullInfo?) {
        if (cityFullInfo != null) {
            val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_location_on_24)
                .setContentTitle("Weather in ${cityFullInfo.name}, ${cityFullInfo.country}")
                .setContentText(getString(R.string.celsius, cityFullInfo.current!!.temp.toInt()))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true)
                .setAutoCancel(false)
                .setSilent(true)
            val notification = builder.build()
            notificationManager.notify(1, notification)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCityForNotificationAndLaunchNotification(){
        var result: CityFullInfo?
        fusedLocationClient.lastLocation.addOnCompleteListener {
            CoroutineScope(Dispatchers.IO + job).launch {
                if (it.isSuccessful) {
                    result = findNearest(it.result.latitude, it.result.longitude)
                    LocalDataCache.putLastCityInNotification(result!!.id!!)
                } else if (LocalDataCache.getLastCityInNotification() != 0) {
                    result = findLastInNotification(LocalDataCache.getLastCityInNotification())
                } else {
                    result = findLast()
                }
                createNotification(result)
            }
        }
    }

    private suspend fun findNearest(lat: Double, lon: Double) = withContext(Dispatchers.IO + job) {
        val allCities = localRepo.getAllCityFullInfo()
        val distance = mutableListOf<Double>()
        allCities.forEach { distance.add(findMinDistance(it.lon, it.lat, lon, lat)) }
        if(distance.minOrNull()!=null) {
            distance.indexOf(distance.minOrNull())
            return@withContext allCities[distance.indexOf(distance.minOrNull())]
        } else {
            return@withContext null
        }
    }

    private suspend fun findLastInNotification(cityId: Int) = withContext(Dispatchers.IO + job) {
        return@withContext localRepo.getOneCityFullInfo(cityId)
    }

    private suspend fun findLast() = withContext(Dispatchers.IO + job) {
        if(localRepo.getAllCityFullInfo().isNotEmpty()) {
            return@withContext localRepo.getAllCityFullInfo().last()
        } else {
            return@withContext null
        }
    }

    private fun findMinDistance(cityLon: Double, cityLat: Double, Lon: Double, Lat: Double): Double {
        return sqrt(((cityLon - Lon).pow(2) + (cityLat - Lat).pow(2)))
    }
}
