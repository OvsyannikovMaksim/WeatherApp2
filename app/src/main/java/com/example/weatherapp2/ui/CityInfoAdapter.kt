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
import com.example.weatherapp2.model.common.CityFullInfo

class CityInfoAdapter :
    ListAdapter<CityFullInfo, CityInfoAdapter.CityVH>(DiffCallback) {

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

        fun bind(cityFullInfo: CityFullInfo) {
            cityCoordinateBinding.cityNameText.text = createFullCityName(cityFullInfo)
            cityCoordinateBinding.cityCoordinateCard.setOnClickListener {
                val bundle = Bundle()
                bundle.putParcelable("CityInfoKey", cityFullInfo)
                it.findNavController().navigate(
                    R.id.action_navigation_input_city_to_navigation_edit_city,
                    bundle
                )
            }
        }

        private fun createFullCityName(cityFullInfo: CityFullInfo): String {
            return if (cityFullInfo.state != null) {
                "${cityFullInfo.name}, ${cityFullInfo.state}, ${cityFullInfo.country}"
            } else {
                "${cityFullInfo.name}, ${cityFullInfo.country}"
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
            if (oldItem.name == newItem.name && oldItem.country == newItem.country) {
                return true
            }
            return false
        }
    }
}
