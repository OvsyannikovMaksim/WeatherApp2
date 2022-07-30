package com.example.weatherapp2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp2.R
import com.example.weatherapp2.databinding.CityCoordinateBinding
import com.example.weatherapp2.model.common.CityInfo

class CityInfoAdapter :
    ListAdapter<CityInfo, CityInfoAdapter.CityVH>(DiffCallback) {

    private lateinit var cityCoordinateBinding: CityCoordinateBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityVH {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        cityCoordinateBinding = CityCoordinateBinding.inflate(inflater, parent, false)
        return CityVH(cityCoordinateBinding)
    }

    override fun onBindViewHolder(holder: CityVH, position: Int) {
        holder.bind(currentList[position])
    }

    class CityVH(private val cityCoordinateBinding: CityCoordinateBinding) : RecyclerView.ViewHolder(
        cityCoordinateBinding.root
    ) {

        fun bind(cityInfo: CityInfo) {
            cityCoordinateBinding.cityNameText.text = createFullCityName(cityInfo)
            cityCoordinateBinding.cityCoordinateCard.setOnClickListener {
                val bundle = Bundle()
                bundle.putParcelable("CityInfoKey", cityInfo)
                it.findNavController().navigate(
                    R.id.action_navigation_input_city_to_navigation_edit_city,
                    bundle
                )
            }
        }

        private fun createFullCityName(cityInfo: CityInfo): String {
            return if (cityInfo.state != null) {
                "${cityInfo.name}, ${cityInfo.state}, ${cityInfo.country}"
            } else {
                "${cityInfo.name}, ${cityInfo.country}"
            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<CityInfo>() {

        override fun areItemsTheSame(oldItem: CityInfo, newItem: CityInfo): Boolean {
            if (oldItem.lat == newItem.lat && oldItem.lon == newItem.lon) {
                return true
            }
            return false
        }

        override fun areContentsTheSame(oldItem: CityInfo, newItem: CityInfo): Boolean {
            if (oldItem.name == newItem.name && oldItem.country == newItem.country) {
                return true
            }
            return false
        }
    }
}
