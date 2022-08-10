package com.example.weatherapp2.ui

import android.content.Context
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
import com.example.weatherapp2.model.common.CityFullInfo
import com.squareup.picasso.Picasso
import kotlin.math.roundToInt

class WeatherInfoAdapter :
    ListAdapter<CityFullInfo, WeatherInfoAdapter.WeatherVH>(DiffCallback) {

    private lateinit var cityWeatherBinding: CityWeatherBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherVH {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        cityWeatherBinding = CityWeatherBinding.inflate(inflater, parent, false)
        return WeatherVH(cityWeatherBinding, parent.context)
    }

    override fun onBindViewHolder(holder: WeatherVH, position: Int) {
        holder.bind(currentList[position])
    }

    class WeatherVH(
        private val cityWeatherBinding: CityWeatherBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(
        cityWeatherBinding.root
    ) {

        fun bind(cityWeather: CityFullInfo) {
            cityWeatherBinding.cityName.text = createFullCityName(cityWeather)
            val weatherPictureUri = Uri.parse(
                "https://openweathermap.org/img/wn/${cityWeather.current!!.weather.first().icon}@2x.png"
            )
            Picasso.get().load(weatherPictureUri).into(cityWeatherBinding.weatherImage)
            cityWeatherBinding.cityTemperature.text = context.getString(
                R.string.celsius,
                cityWeather.current.temp.roundToInt()
            )
            cityWeatherBinding.cityWeatherCard.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt("FullInfoKey", cityWeather.id!!)
                it.findNavController().navigate(
                    R.id.action_navigation_home_to_weatherFullInfoFragment,
                    bundle
                )
            }
        }

        private fun createFullCityName(cityInfo: CityFullInfo): String {
            return if (cityInfo.state != null) {
                "${cityInfo.name}, ${cityInfo.state}, ${cityInfo.country}"
            } else {
                "${cityInfo.name}, ${cityInfo.country}"
            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<CityFullInfo>() {
        override fun areItemsTheSame(oldItem: CityFullInfo, newItem: CityFullInfo): Boolean {
            if (oldItem.lat == newItem.lat && oldItem.lon == newItem.lon) {
                return true
            }
            return false
        }

        override fun areContentsTheSame(oldItem: CityFullInfo, newItem: CityFullInfo): Boolean {
            if (oldItem.current!!.temp == newItem.current!!.temp && oldItem.current.weather == newItem.current.weather) {
                return true
            }
            return false
        }
    }
}
