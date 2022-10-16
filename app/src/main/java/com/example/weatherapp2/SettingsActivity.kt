package com.example.weatherapp2

import android.content.Intent
import android.os.Build
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
import com.example.weatherapp2.model.repository.OpenWeatherRepositoryImpl
import com.example.weatherapp2.ui.saveSettingsDialog.SaveSettingsDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity(), SaveSettingsDialog.NoticeDialogListener {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var getOnePermissionLauncher: ActivityResultLauncher<String>
    private lateinit var getMultiplePermissionLauncher: ActivityResultLauncher<Array<String>>

    @Inject
    lateinit var repository: OpenWeatherRepositoryImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = getString(R.string.setting_activity_name)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        getOnePermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                SaveSettingsDialog().show(supportFragmentManager, "SaveDialog")
            } else {
                Toast.makeText(this, "We need your location for live weather", Toast.LENGTH_SHORT).show()
            }
        }

        getMultiplePermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val isGranted = checkMultiplePermissions(permissions)
            if (isGranted) {
                SaveSettingsDialog().show(supportFragmentManager, "SaveDialog")
            } else {
                Toast.makeText(
                    this,
                    "We need your location and notification for live weather",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.mapsSpinner.setSelection(repository.getChosenMapId())
        binding.switchService.isChecked = repository.getServiceState()
        if (binding.switchService.isChecked) {
            binding.timeText.visibility = View.VISIBLE
            binding.textMinutes.visibility = View.VISIBLE
            binding.timeText.setText(
                repository.getServiceUpdateTime().toString()
            )
        }

        binding.switchService.setOnCheckedChangeListener { _, isViewsVisible ->
            syncPeriodViewsVisibility(isViewsVisible)
        }

        binding.saveButton.setOnClickListener {
            if (binding.switchService.isChecked) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    getOnePermissionLauncher.launch(
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                } else {
                    getMultiplePermissionLauncher.launch(
                        arrayOf(
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.POST_NOTIFICATIONS
                        )
                    )
                }
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
        } else if (!binding.switchService.isChecked && isWorkerOn(updateWeatherWorkerTag)) {
            WorkManager.getInstance(applicationContext).cancelAllWorkByTag(updateWeatherWorkerTag)
        } else if (binding.switchService.isChecked && isWorkerOn(updateWeatherWorkerTag) &&
            isTimeChanged(binding.timeText.text.toString().toLong())
        ) {
            WorkManager.getInstance(applicationContext).cancelAllWorkByTag(updateWeatherWorkerTag)
            WorkManager.getInstance(applicationContext).enqueue(updateWeatherWorker)
        }
        syncNotification()
        repository.setServiceUpdateTime(binding.timeText.text.toString().toInt())
        repository.setServiceState(binding.switchService.isChecked)
        repository.setChosenMapId(binding.mapsSpinner.selectedItemPosition)
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
        val oldTime = repository.getServiceUpdateTime().toLong()
        if (newTime != oldTime) {
            return true
        }
        return false
    }

    private fun syncNotification() {
        if (binding.switchService.isChecked && binding.switchService.isChecked != repository.getServiceState()) {
            startService(Intent(this, WeatherNotificationService::class.java))
        } else if (!binding.switchService.isChecked && binding.switchService.isChecked != repository.getServiceState()) {
            stopService(Intent(this, WeatherNotificationService::class.java))
        }
    }

    private fun checkMultiplePermissions(permissions: Map<String, Boolean>): Boolean {
        var res = true
        for (entry in permissions.entries) {
            res = res && entry.value
        }
        return res
    }
    companion object {
        const val updateWeatherWorkerTag = "UpdateWeatherWorkerTag"
    }
}
