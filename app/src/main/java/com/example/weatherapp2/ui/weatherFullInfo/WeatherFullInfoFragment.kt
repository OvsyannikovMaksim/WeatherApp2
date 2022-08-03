package com.example.weatherapp2.ui.weatherFullInfo

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.weatherapp2.R
import com.example.weatherapp2.databinding.FragmentWeatherFullInfoBinding
import com.example.weatherapp2.model.CurrentMapper
import com.example.weatherapp2.model.common.CityCoordinate
import com.example.weatherapp2.model.common.openWeatherApi.CityWeatherFullInfo
import com.example.weatherapp2.model.db.DataBase
import com.example.weatherapp2.model.repository.LocalRepoImpl
import com.squareup.picasso.Picasso

class WeatherFullInfoFragment : Fragment() {

    private lateinit var weatherFullInfoModel: WeatherFullInfoModel
    private lateinit var weatherFullInfoModelFactory: WeatherFullInfoModelFactory
    private lateinit var fragmentWeatherFullInfoBinding: FragmentWeatherFullInfoBinding
    lateinit var cityCoordinate: CityCoordinate
    lateinit var cityWeatherFullInfo: CityWeatherFullInfo

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val dataBase = DataBase.getDataBase(this.requireContext())!!
        val localDao = dataBase.localDao()
        val localRepo = LocalRepoImpl(localDao)
        weatherFullInfoModelFactory = WeatherFullInfoModelFactory(localRepo)
        weatherFullInfoModel = ViewModelProvider(this, weatherFullInfoModelFactory)[WeatherFullInfoModel::class.java]
        cityWeatherFullInfo = requireArguments().getParcelable("FullInfoKey")!!
        weatherFullInfoModel.getCityFromRepo(cityWeatherFullInfo.id)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentWeatherFullInfoBinding = FragmentWeatherFullInfoBinding.inflate(inflater)
        return fragmentWeatherFullInfoBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cityCoordinateLivaData: LiveData<CityCoordinate> = weatherFullInfoModel.cityCoordinate
        cityCoordinateLivaData.observe(viewLifecycleOwner) {
            Log.d("TAG", it.toString())
            cityCoordinate = it
            if (it.comment != "") {
                fragmentWeatherFullInfoBinding.commentPretext.visibility = View.VISIBLE
                fragmentWeatherFullInfoBinding.commentText.visibility = View.VISIBLE
                fragmentWeatherFullInfoBinding.commentText.text = it.comment
            }
            if (it.pic_uri != null) {
                Picasso.with(view.context).load(Uri.parse(it.pic_uri)).into(
                    fragmentWeatherFullInfoBinding.cityPic
                )
            }
        }
        val currentMapper = CurrentMapper()
        val currentWeather = currentMapper.map(cityWeatherFullInfo.current, view.context)
        fragmentWeatherFullInfoBinding.placeName.text = createFullCityName(cityWeatherFullInfo)
        fragmentWeatherFullInfoBinding.humidity.text = currentWeather.humidity
        fragmentWeatherFullInfoBinding.temperature.text = currentWeather.temp
        fragmentWeatherFullInfoBinding.UV.text = currentWeather.uvi
        fragmentWeatherFullInfoBinding.feelTemperature.text = currentWeather.feels_like
        fragmentWeatherFullInfoBinding.pressure.text = currentWeather.pressure
        fragmentWeatherFullInfoBinding.weatherName.text = currentWeather.weatherDescription
        fragmentWeatherFullInfoBinding.wind.text = currentWeather.wind
        Picasso.with(view.context).load(currentWeather.weatherPicture).into(
            fragmentWeatherFullInfoBinding.weatherPic
        )
        fragmentWeatherFullInfoBinding.editButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("CityCoordinateKey", cityCoordinate)
            it.findNavController().navigate(
                R.id.action_navigation_weatherFullInfoFragment_to_navigation_edit_city,
                bundle
            )
        }
        fragmentWeatherFullInfoBinding.deleteButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("CityCoordinateKey", cityCoordinate)
            bundle.putParcelable("CityWeatherFullInfoKey", cityWeatherFullInfo)
            it.findNavController().navigate(
                R.id.action_navigation_weatherFullInfoFragment_to_navigation_delete_dialog,
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
