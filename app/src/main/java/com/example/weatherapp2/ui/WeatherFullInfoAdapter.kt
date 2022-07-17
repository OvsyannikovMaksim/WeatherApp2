package com.example.weatherapp2.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp2.databinding.CityWeatherBinding
import com.example.weatherapp2.model.common.openWeatherApi.CityWeatherFullInfo

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
            cityWeatherBinding.cityTemperature.text = cityWeather.current.temp.toInt().toString()
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<CityWeatherFullInfo>() {
        override fun areItemsTheSame(oldItem: CityWeatherFullInfo, newItem: CityWeatherFullInfo): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: CityWeatherFullInfo, newItem: CityWeatherFullInfo): Boolean {
            return false
        }
    }
}
