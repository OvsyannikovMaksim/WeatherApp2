package com.example.weatherapp2.di

import android.content.Context
import com.example.weatherapp2.model.api.OpenWeatherApiRetrofit
import com.example.weatherapp2.model.db.DataBase
import com.example.weatherapp2.model.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideRepository(dao: Dao, api: Api, sharedPref: MySharedPreference): Repository =
        OpenWeatherRepositoryImpl(dao, api, sharedPref)

    @Provides
    fun provideApi(): Api = OpenWeatherApiRetrofit.openWeatherApi

    @Provides
    fun provideDao(@ApplicationContext context: Context): Dao = DataBase.getDataBase(context)!!.localDao()

    @Provides
    fun provideSharedPref(@ApplicationContext context: Context): MySharedPreference = LocalCache(
        context
    )
}
