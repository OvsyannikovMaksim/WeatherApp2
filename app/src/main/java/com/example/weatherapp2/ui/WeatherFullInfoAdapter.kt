package com.example.weatherapp2.ui

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp2.databinding.CityWeatherBinding
import com.example.weatherapp2.model.common.openWeatherApi.CityWeatherFullInfo
import com.squareup.picasso.Picasso
import kotlin.math.roundToInt

class WeatherFullInfoAdapter :
    ListAdapter<CityWeatherFullInfo, WeatherFullInfoAdapter.WeatherVH>(DiffCallback) {

    private lateinit var cityWeatherBinding: CityWeatherBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherVH {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        cityWeatherBinding = CityWeatherBinding.inflate(inflater, parent, false)
        return WeatherVH(cityWeatherBinding)
    }

    override fun onBindViewHolder(holder: WeatherVH, position: Int) {
        holder.bind(currentList[position])
    }

    class WeatherVH(private val cityWeatherBinding: CityWeatherBinding) : RecyclerView.ViewHolder(
        cityWeatherBinding.root
    ) {

        fun bind(cityWeather: CityWeatherFullInfo) {
            cityWeatherBinding.cityName.text = cityWeather.timezone
            val weatherPictureUri = Uri.parse(
                "http://openweathermap.org/img/wn/${cityWeather.current.weather.first().icon}@2x.png"
            )
            Picasso.with(itemView.context).load(weatherPictureUri).into(
                cityWeatherBinding.weatherImage
            )
            cityWeatherBinding.cityTemperature.text = cityWeather.current.temp.roundToInt().toString() + "\u00B0 С"
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<CityWeatherFullInfo>() {
        override fun areItemsTheSame(oldItem: CityWeatherFullInfo, newItem: CityWeatherFullInfo): Boolean {
            if (oldItem.lat == newItem.lat && oldItem.lon == newItem.lon) {
                return true
            }
            return false
        }

        override fun areContentsTheSame(oldItem: CityWeatherFullInfo, newItem: CityWeatherFullInfo): Boolean {
            if (oldItem.current.temp == newItem.current.temp && oldItem.current.weather == newItem.current.weather) {
                return true
            }
            return false
        }
    }
}
