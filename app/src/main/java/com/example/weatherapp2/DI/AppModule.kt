package com.example.weatherapp2.DI

import android.content.Context
import com.example.weatherapp2.model.api.OpenWeatherApiRetrofit
import com.example.weatherapp2.model.db.DataBase
import com.example.weatherapp2.model.repository.Api
import com.example.weatherapp2.model.repository.Dao
import com.example.weatherapp2.model.repository.OpenWeatherRepositoryImpl
import com.example.weatherapp2.model.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideRepository(dao: Dao, api: Api): Repository = OpenWeatherRepositoryImpl(dao, api)

    @Provides
    fun provideApi(): Api = OpenWeatherApiRetrofit.openWeatherApi

    @Provides
    fun provideDao(@ApplicationContext context: Context): Dao = DataBase.getDataBase(context)!!.localDao()
}