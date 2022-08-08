package com.example.weatherapp2.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp2.R
import com.example.weatherapp2.databinding.FragmentHomeBinding
import com.example.weatherapp2.model.api.OpenWeatherApiRetrofit
import com.example.weatherapp2.model.common.CityFullInfo
import com.example.weatherapp2.model.db.DataBase
import com.example.weatherapp2.model.repository.CityWeatherRepoImpl
import com.example.weatherapp2.model.repository.LocalRepoImpl
import com.example.weatherapp2.ui.WeatherInfoAdapter

class HomeFragment : Fragment() {

    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeViewModelFactory: HomeViewModelFactory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val cityWeatherRepoImpl = CityWeatherRepoImpl(OpenWeatherApiRetrofit.openWeatherApi)
        val dataBase = DataBase.getDataBase(this.requireContext())!!
        val localDao = dataBase.localDao()
        val localRepo = LocalRepoImpl(localDao)
        homeViewModelFactory = HomeViewModelFactory(cityWeatherRepoImpl, localRepo)
        homeViewModel = ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
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
        homeViewModel.getCitiesInfoAndLoadItToLocalRepo("en")
        val recyclerView = fragmentHomeBinding.cityInfoRecyclerview
        recyclerView.layoutManager = mLayout
        val weatherInfoAdapter = WeatherInfoAdapter()
        val citiesWeather: LiveData<List<CityFullInfo>> = homeViewModel.cityWeatherList
        citiesWeather.observe(viewLifecycleOwner) {
            weatherInfoAdapter.submitList(it)
            recyclerView.adapter = weatherInfoAdapter
            fragmentHomeBinding.loadingIndicator.visibility = View.GONE
        }
        fragmentHomeBinding.addCityButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_navigation_home_to_navigation_input_city)
        }
    }
}
