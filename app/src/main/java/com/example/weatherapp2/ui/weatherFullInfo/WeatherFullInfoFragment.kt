package com.example.weatherapp2.ui.weatherFullInfo

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.weatherapp2.R
import com.example.weatherapp2.databinding.FragmentWeatherFullInfoBinding
import com.example.weatherapp2.model.common.CityFullInfo
import com.example.weatherapp2.model.common.CurrentMapper
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherFullInfoFragment : Fragment() {

    private val weatherFullInfoModel: WeatherFullInfoModel by viewModels()
    private lateinit var fragmentWeatherFullInfoBinding: FragmentWeatherFullInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val coordinates = requireArguments().getDoubleArray("FullInfoKey")!!
        weatherFullInfoModel.getCityFromRepo(coordinates[0], coordinates[1])
        fragmentWeatherFullInfoBinding = FragmentWeatherFullInfoBinding.inflate(inflater)
        return fragmentWeatherFullInfoBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherFullInfoModel.cityCoordinate.observe(viewLifecycleOwner) {
            binding(it, view.context)
        }
    }

    private fun binding(cityFullInfo: CityFullInfo, context: Context) {
        if (cityFullInfo.comment != "") {
            fragmentWeatherFullInfoBinding.commentPretext.visibility = View.VISIBLE
            fragmentWeatherFullInfoBinding.commentText.visibility = View.VISIBLE
            fragmentWeatherFullInfoBinding.commentText.text = cityFullInfo.comment
        }
        if (cityFullInfo.pic != null) {
            Picasso.get().load(Uri.parse(cityFullInfo.pic)).into(
                fragmentWeatherFullInfoBinding.cityPic
            )
        }
        val currentMapper = CurrentMapper()
        val currentWeather = currentMapper.map(cityFullInfo.current!!, context)
        fragmentWeatherFullInfoBinding.placeName.text = createFullCityName(cityFullInfo)
        fragmentWeatherFullInfoBinding.humidity.text = currentWeather.humidity
        fragmentWeatherFullInfoBinding.temperature.text = currentWeather.temp
        fragmentWeatherFullInfoBinding.UV.text = currentWeather.uvi
        fragmentWeatherFullInfoBinding.feelTemperature.text = currentWeather.feels_like
        fragmentWeatherFullInfoBinding.pressure.text = currentWeather.pressure
        fragmentWeatherFullInfoBinding.weatherName.text = currentWeather.weatherDescription
        fragmentWeatherFullInfoBinding.wind.text = currentWeather.wind
        Picasso.get().load(currentWeather.weatherPicture).into(
            fragmentWeatherFullInfoBinding.weatherPic
        )
        fragmentWeatherFullInfoBinding.editButton.setOnClickListener { it1 ->
            val bundle = Bundle()
            bundle.putParcelable("CityInfoKey", cityFullInfo)
            it1.findNavController().navigate(
                R.id.action_navigation_weatherFullInfoFragment_to_navigation_edit_city,
                bundle
            )
        }
        fragmentWeatherFullInfoBinding.deleteButton.setOnClickListener { it1 ->
            val bundle = Bundle()
            bundle.putInt("CityIdKey", cityFullInfo.id!!)
            it1.findNavController().navigate(
                R.id.action_navigation_weatherFullInfoFragment_to_navigation_delete_dialog,
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
