package com.example.weatherapp2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.work.*
import com.example.weatherapp2.databinding.ActivitySettingsBinding
import com.example.weatherapp2.model.UpdateWeatherWorker
import com.example.weatherapp2.model.WeatherNotificationService
import com.example.weatherapp2.model.repository.LocalDataCache
import com.example.weatherapp2.ui.dialogs.SaveSettingsDialog
import java.util.concurrent.TimeUnit

class SettingsActivity : AppCompatActivity(), SaveSettingsDialog.NoticeDialogListener {

    private lateinit var binding: ActivitySettingsBinding
    private val updateWeatherWorkerTag = "UpdateWeatherWorkerTag"
    private lateinit var getLocationPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = getString(R.string.setting_activity_name)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        getLocationPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                SaveSettingsDialog().show(supportFragmentManager, "SaveDialog")
            } else {
                Toast.makeText(this, "We need your location for live weather", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.mapsSpinner.setSelection(LocalDataCache.getChosenMapId())
        binding.switchService.isChecked = LocalDataCache.getServiceState()
        if (binding.switchService.isChecked) {
            binding.timeText.visibility = View.VISIBLE
            binding.textMinutes.visibility = View.VISIBLE
            binding.timeText.setText(
                LocalDataCache.getServiceUpdateTime().toString()
            )
        }

        binding.switchService.setOnCheckedChangeListener { _, isViewsVisible ->
            syncPeriodViewsVisibility(isViewsVisible)
        }

        binding.saveButton.setOnClickListener {
            if (binding.switchService.isChecked) {
                getLocationPermissionLauncher.launch(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } else {
                SaveSettingsDialog().show(supportFragmentManager, "SaveDialog")
            }
        }
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        val updateWeatherWorker = PeriodicWorkRequestBuilder<UpdateWeatherWorker>(
            binding.timeText.text.toString().toLong() + 1L,
            TimeUnit.MINUTES,
            binding.timeText.text.toString().toLong(),
            TimeUnit.MINUTES
        )
            .addTag(updateWeatherWorkerTag)
            .setConstraints(createConstraints())
            .build()
        if (binding.switchService.isChecked && !isWorkerOn(updateWeatherWorkerTag)) {
            WorkManager.getInstance(applicationContext).enqueue(updateWeatherWorker)
            // updateWeatherWorker.id
        } else if (!binding.switchService.isChecked && isWorkerOn(updateWeatherWorkerTag)) {
            WorkManager.getInstance(applicationContext).cancelAllWorkByTag(updateWeatherWorkerTag)
        } else if (binding.switchService.isChecked && isWorkerOn(updateWeatherWorkerTag) &&
            isTimeChanged(binding.timeText.text.toString().toLong())
        ) {
            WorkManager.getInstance(applicationContext).cancelAllWorkByTag(updateWeatherWorkerTag)
            WorkManager.getInstance(applicationContext).enqueue(updateWeatherWorker)
        }
        syncNotification()
        LocalDataCache.setServiceUpdateTime(binding.timeText.text.toString().toInt())
        LocalDataCache.setServiceState(binding.switchService.isChecked)
        LocalDataCache.setChosenMapId(binding.mapsSpinner.selectedItemPosition)
        finish()
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
    }

    private fun syncPeriodViewsVisibility(isViewsVisible: Boolean) {
        if (isViewsVisible) {
            binding.timeText.visibility = View.VISIBLE
            binding.textMinutes.visibility = View.VISIBLE
        } else {
            binding.timeText.visibility = View.INVISIBLE
            binding.textMinutes.visibility = View.INVISIBLE
        }
    }

    private fun createConstraints(): Constraints {
        return Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
    }

    private fun isWorkerOn(tag: String): Boolean {
        val worker = WorkManager.getInstance(applicationContext).getWorkInfosByTag(tag)
        if (worker.get().size != 0) {
            return worker.get()[0].state == WorkInfo.State.ENQUEUED
        }
        return false
    }

    private fun isTimeChanged(newTime: Long): Boolean {
        val oldTime = LocalDataCache.getServiceUpdateTime().toLong()
        if (newTime != oldTime) {
            return true
        }
        return false
    }

    private fun syncNotification() {
        if (binding.switchService.isChecked && binding.switchService.isChecked != LocalDataCache.getServiceState()) {
            startService(Intent(this, WeatherNotificationService::class.java))
        } else if (!binding.switchService.isChecked && binding.switchService.isChecked != LocalDataCache.getServiceState()) {
            stopService(Intent(this, WeatherNotificationService::class.java))
        }
    }
}
