package com.example.weatherapp2.model

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.weatherapp2.model.repository.OpenWeatherRepositoryImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@HiltWorker
class UpdateWeatherWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    val repositoryImpl: OpenWeatherRepositoryImpl
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        try {
            getCitiesInfoAndLoadItToLocalRepo("en")
        } catch (e: Exception) {
            Log.d("UpdateWeatherWorker.kt", e.toString())
            return Result.failure()
        }
        return Result.success()
    }

    private fun getCitiesInfoAndLoadItToLocalRepo(language: String) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            repositoryImpl.getCitiesInfoAndLoadItToLocalRepo(language)
        }
    }
}
