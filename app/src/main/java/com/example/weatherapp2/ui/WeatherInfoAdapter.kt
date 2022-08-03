package com.example.weatherapp2.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp2.R
import com.example.weatherapp2.databinding.CityWeatherBinding
import com.example.weatherapp2.model.common.openWeatherApi.CityWeatherFullInfo
import com.squareup.picasso.Picasso
import kotlin.math.roundToInt

class WeatherInfoAdapter :
    ListAdapter<CityWeatherFullInfo, WeatherInfoAdapter.WeatherVH>(DiffCallback) {

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
            cityWeatherBinding.cityName.text = createFullCityName(cityWeather)
            val weatherPictureUri = Uri.parse(
                "http://openweathermap.org/img/wn/${cityWeather.current.weather.first().icon}@2x.png"
            )
            Picasso.with(itemView.context).load(weatherPictureUri).into(
                cityWeatherBinding.weatherImage
            )
            cityWeatherBinding.cityTemperature.text = "${cityWeather.current.temp.roundToInt()} \u00B0 С"
            cityWeatherBinding.cityWeatherCard.setOnClickListener {
                val bundle = Bundle()
                bundle.putParcelable("FullInfoKey", cityWeather)
                it.findNavController().navigate(
                    R.id.action_navigation_home_to_weatherFullInfoFragment,
                    bundle
                )
            }
        }

        private fun createFullCityName(cityWeather: CityWeatherFullInfo): String {
            return if (cityWeather.state != null) {
                "${cityWeather.name}, ${cityWeather.state}, ${cityWeather.country}"
            } else {
                "${cityWeather.name}, ${cityWeather.country}"
            }
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
