package com.example.weatherapp2.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.weatherapp2.R
import com.example.weatherapp2.databinding.FragmentHomeBinding
import com.example.weatherapp2.model.api.OpenWeatherApiRetrofit
import com.example.weatherapp2.model.common.CityFullInfo
import com.example.weatherapp2.model.db.DataBase
import com.example.weatherapp2.model.repository.CityWeatherRepoImpl
import com.example.weatherapp2.model.repository.LocalDataCache
import com.example.weatherapp2.model.repository.LocalRepoImpl
import com.example.weatherapp2.ui.WeatherInfoAdapter

class HomeFragment : Fragment() {

    private val updateWeatherWorkerTag = "UpdateWeatherWorkerTag"
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private val homeViewModel by viewModels<HomeViewModel> {
        HomeViewModelFactory(
            CityWeatherRepoImpl(OpenWeatherApiRetrofit.openWeatherApi),
            LocalRepoImpl(
                DataBase.getDataBase(this.requireContext())!!
                    .localDao()
            )
        )
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
        Log.d("HomeFragment", "onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        val mLayout = GridLayoutManager(
            activity,
            1,
            LinearLayoutManager.VERTICAL,
            false
        )
        homeViewModel.getCitiesInfoAndLoadItToLocalRepo("en")
        mRecyclerView = fragmentHomeBinding.cityInfoRecyclerview
        mRecyclerView.layoutManager = mLayout
        val weatherInfoAdapter = WeatherInfoAdapter()
        val citiesWeather: LiveData<List<CityFullInfo>> = homeViewModel.cityWeatherList
        citiesWeather.observe(viewLifecycleOwner) {
            weatherInfoAdapter.submitList(it)
            mRecyclerView.adapter = weatherInfoAdapter
            fragmentHomeBinding.loadingIndicator.visibility = View.GONE
            val lastPosition = LocalDataCache.getAdapterLastPosition()
            mRecyclerView.scrollToPosition(lastPosition)
        }
        fragmentHomeBinding.addCityButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_navigation_home_to_navigation_input_city)
        }
        WorkManager.getInstance(requireContext()).getWorkInfosByTagLiveData(updateWeatherWorkerTag)
            .observe(viewLifecycleOwner){
                if(it[0].state==WorkInfo.State.SUCCEEDED){
                    homeViewModel.getCitiesInfo()
                }
        }
    }

    override fun onPause() {
        super.onPause()
        val mLayoutManager = mRecyclerView.layoutManager as GridLayoutManager
        LocalDataCache.setAdapterLastPosition(mLayoutManager.findFirstCompletelyVisibleItemPosition())
    }
}
