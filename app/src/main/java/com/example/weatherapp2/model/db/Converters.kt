package com.example.weatherapp2.model.db

import androidx.room.TypeConverter
import com.example.weatherapp2.model.common.openWeatherApi.Weather
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun listToJsonString(value: List<Weather>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonStringToList(value: String) = Gson().fromJson(value, Array<Weather>::class.java).toList()
}
