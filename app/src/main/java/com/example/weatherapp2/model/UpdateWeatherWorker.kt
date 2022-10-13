package com.example.weatherapp2.model

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.weatherapp2.model.repository.OpenWeatherRepositoryImpl
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class UpdateWeatherWorker(
    context: Context,
    workerParams: WorkerParameters
) :
    Worker(context, workerParams) {

    @Inject
    lateinit var repositoryImpl: OpenWeatherRepositoryImpl

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
