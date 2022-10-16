package com.example.weatherapp2.model

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.core.content.ContextCompat.getSystemService

object NetworkMonitor {

    var myNetwork: Boolean = false
    lateinit var connectivityManager: ConnectivityManager

    fun init(context: Context) {
        createInternetMonitor()
        connectivityManager =
            getSystemService(context, ConnectivityManager::class.java) as ConnectivityManager
    }


    private fun createInternetMonitor() {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                myNetwork = true
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                myNetwork = false
            }
        }
        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }
}