package com.example.weatherapp2.ui.inputCities

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp2.databinding.FragmentInputCitiesBinding
import com.example.weatherapp2.model.api.OpenWeatherApiRetrofit
import com.example.weatherapp2.model.common.CityInfo
import com.example.weatherapp2.model.repository.CityWeatherRepoImpl
import com.example.weatherapp2.ui.CityInfoAdapter

class InputCitiesFragment : Fragment() {

    private lateinit var fragmentInputCitiesBinding: FragmentInputCitiesBinding
    private lateinit var inputCitiesModel: InputCitiesModel
    private lateinit var inputCitiesModelFactory: InputCitiesModelFactory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val cityWeatherRepoImpl = CityWeatherRepoImpl(OpenWeatherApiRetrofit.openWeatherApi)
        inputCitiesModelFactory = InputCitiesModelFactory(cityWeatherRepoImpl)
        inputCitiesModel = ViewModelProvider(this, inputCitiesModelFactory)[InputCitiesModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentInputCitiesBinding = FragmentInputCitiesBinding.inflate(inflater, container, false)
        return fragmentInputCitiesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = setupRecyclerView()
        val cityInfoAdapter = CityInfoAdapter()
        val resultOfSearch: LiveData<List<CityInfo>> = inputCitiesModel.resultOfSearch
        fragmentInputCitiesBinding.inputCityNameEditText.setOnKeyListener { view, _, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                fragmentInputCitiesBinding.inputCityNameEditText.clearFocus()
                hideKeyboard(requireContext(), view)
                inputCitiesModel.getCitiesFromLine(
                    fragmentInputCitiesBinding.inputCityNameEditText.text.toString()
                )
                resultOfSearch.observe(viewLifecycleOwner) {
                    cityInfoAdapter.submitList(it)
                    recyclerView.adapter = cityInfoAdapter
                }
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }

    private fun hideKeyboard(context: Context, view: View) {
        val imm: InputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun setupRecyclerView(): RecyclerView {
        val mLayout = GridLayoutManager(
            activity,
            1,
            LinearLayoutManager.VERTICAL,
            false
        )
        val recyclerView = fragmentInputCitiesBinding.citiesRecyclerView
        recyclerView.layoutManager = mLayout
        return recyclerView
    }
}
