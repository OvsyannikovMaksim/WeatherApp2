package com.example.weatherapp2.ui.inputCities

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp2.R
import com.example.weatherapp2.databinding.FragmentInputCitiesBinding
import com.example.weatherapp2.model.common.CityFullInfo
import com.example.weatherapp2.model.repository.LocalDataCache
import com.example.weatherapp2.ui.CityInfoAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InputCitiesFragment : Fragment() {

    private lateinit var fragmentInputCitiesBinding: FragmentInputCitiesBinding
    private val mAdapter = CityInfoAdapter()
    private val inputCitiesModel: InputCitiesModel by viewModels()

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
        if (LocalDataCache.getInternetAccess()) {
            fragmentInputCitiesBinding.inputCityName.setEndIconOnClickListener {
                findNavController().navigate(
                    R.id.action_navigation_input_city_to_mapCityInputFragment
                )
            }
        }
        setupRecyclerView()
        val resultOfSearch: LiveData<List<CityFullInfo>> = inputCitiesModel.resultOfSearch
        resultOfSearch.observe(viewLifecycleOwner) {
            mAdapter.submitList(it)
        }
        fragmentInputCitiesBinding.inputCityNameEditText.setOnKeyListener { v, _, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                fragmentInputCitiesBinding.inputCityNameEditText.clearFocus()
                hideKeyboard(requireContext(), v)
                if (LocalDataCache.getInternetAccess()) {
                    inputCitiesModel.getCitiesFromLine(
                        fragmentInputCitiesBinding.inputCityNameEditText.text.toString()
                    )
                } else {
                    Toast.makeText(requireContext(), "No internet", Toast.LENGTH_SHORT).show()
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

    private fun setupRecyclerView() {
        val mLayout = GridLayoutManager(
            activity,
            1,
            LinearLayoutManager.VERTICAL,
            false
        )
        val recyclerView = fragmentInputCitiesBinding.citiesRecyclerView
        recyclerView.layoutManager = mLayout
        recyclerView.adapter = mAdapter
    }
}
