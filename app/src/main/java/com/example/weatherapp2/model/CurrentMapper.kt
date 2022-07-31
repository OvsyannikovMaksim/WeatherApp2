package com.example.weatherapp2.model

import android.content.Context
import com.example.weatherapp2.R
import com.example.weatherapp2.model.common.openWeatherApi.Current
import com.example.weatherapp2.model.common.openWeatherApi.CurrentParsed
import com.example.weatherapp2.model.common.openWeatherApi.Weather
import java.util.*
import kotlin.math.round

class CurrentMapper : Mapper<Current, CurrentParsed> {

    override fun map(src: Current, context: Context): CurrentParsed = CurrentParsed(

        "${round(src.temp).toInt()} ${context.getString(R.string.celsius)}",
        "${context.getString(R.string.feels)} ${round(src.feels_like).toInt()} ${context.getString(
            R.string.celsius
        )}",
        "${src.pressure} ${context.getString(R.string.pressure)}",
        "${context.getString(R.string.humidity)} ${src.humidity}%",
        "${context.getString(R.string.UV)} ${src.uvi}",
        "${context.getString(R.string.wind)} ${"%.1f".format(src.wind_speed)} ${context.getString(
            R.string.ms
        )} ${calculateDegree(src.wind_deg, context)}",
        getWeatherDescription(src.weather[0]),
        getWeatherPictureURL(src.weather[0])
    )

    private fun getWeatherDescription(weather: Weather): String = weather.description.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale.getDefault()
        ) else it.toString()
    }

    private fun getWeatherPictureURL(weather: Weather): String = "https://openweathermap.org/img/wn/${weather.icon}@2x.png"

    private fun calculateDegree(wind_deg: Int, context: Context): String {
        return when (wind_deg.toDouble()) {
            in 0.0..11.25 -> context.getString(R.string.N)
            in 11.26..33.75 -> context.getString(R.string.NNE)
            in 33.76..56.25 -> context.getString(R.string.NE)
            in 56.26..78.75 -> context.getString(R.string.ENE)
            in 78.76..101.25 -> context.getString(R.string.E)
            in 101.26..123.75 -> context.getString(R.string.ESE)
            in 123.76..146.25 -> context.getString(R.string.SE)
            in 146.26..168.75 -> context.getString(R.string.SSE)
            in 168.76..191.25 -> context.getString(R.string.S)
            in 191.26..213.75 -> context.getString(R.string.SSW)
            in 213.76..236.25 -> context.getString(R.string.SW)
            in 236.26..258.75 -> context.getString(R.string.WSW)
            in 258.76..281.25 -> context.getString(R.string.W)
            in 281.26..303.75 -> context.getString(R.string.WNW)
            in 303.76..326.25 -> context.getString(R.string.NW)
            in 326.26..348.75 -> context.getString(R.string.NNW)
            in 348.76..359.9 -> context.getString(R.string.N)
            else -> ""
        }
    }
}
