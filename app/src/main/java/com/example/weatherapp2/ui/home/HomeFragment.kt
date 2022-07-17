package com.example.weatherapp2.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp2.databinding.FragmentHomeBinding
import com.example.weatherapp2.model.api.OpenWeatherApiRetrofit
import com.example.weatherapp2.model.common.openWeatherApi.CityWeatherFullInfo
import com.example.weatherapp2.model.db.DataBase
import com.example.weatherapp2.model.repository.CityWeatherRepoImpl
import com.example.weatherapp2.model.repository.LocalRepoImpl
import com.example.weatherapp2.ui.WeatherFullInfoAdapter

class HomeFragment : Fragment() {

    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeViewModelFactory: HomeViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        val cityWeatherRepoImpl = CityWeatherRepoImpl(OpenWeatherApiRetrofit.openWeatherApi)
        val dataBase = DataBase.getDataBase(this.context!!)!!
        val localDao = dataBase.localDao()
        val localRepo = LocalRepoImpl(localDao)
        homeViewModelFactory = HomeViewModelFactory(cityWeatherRepoImpl, localRepo)
        homeViewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)
        return fragmentHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mLayout = GridLayoutManager(
            activity,
            1,
            LinearLayoutManager.VERTICAL,
            false
        )
        homeViewModel.getCitiesInfo("ru")
        val recyclerView = fragmentHomeBinding.cityInfoRecyclerview
        recyclerView.layoutManager = mLayout
        val weatherFullInfoAdapter = WeatherFullInfoAdapter()
        val citiesWeather: LiveData<List<CityWeatherFullInfo>> = homeViewModel.cityWeatherList
        citiesWeather.observe(viewLifecycleOwner) {
            weatherFullInfoAdapter.submitList(it)
            recyclerView.adapter = weatherFullInfoAdapter
        }
    }
}
