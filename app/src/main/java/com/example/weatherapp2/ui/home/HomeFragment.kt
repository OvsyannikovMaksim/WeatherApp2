package com.example.weatherapp2.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp2.R
import com.example.weatherapp2.databinding.FragmentHomeBinding
import com.example.weatherapp2.model.NetworkMonitor
import com.example.weatherapp2.model.common.CityFullInfo
import com.example.weatherapp2.ui.WeatherInfoAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var mAdapter = WeatherInfoAdapter()
    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()

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
        val mRecyclerView = fragmentHomeBinding.cityInfoRecyclerview
        mRecyclerView.layoutManager = mLayout
        mRecyclerView.adapter = mAdapter
        val citiesWeather: LiveData<List<CityFullInfo>> = homeViewModel.cityWeatherList
        citiesWeather.observe(viewLifecycleOwner) {
            val listOfValidCities: MutableList<CityFullInfo> = mutableListOf()
            it.forEach { city -> if (city.current != null) { listOfValidCities.add(city) } }
            mAdapter.submitList(listOfValidCities)
            fragmentHomeBinding.loadingIndicator.visibility = View.GONE
        }
        if(NetworkMonitor.myNetwork) {
            homeViewModel.getCitiesInfoAndLoadItToLocalRepo("en")
        } else {
            Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show()
        }
        fragmentHomeBinding.addCityButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_navigation_home_to_navigation_input_city)
        }
    }
}
