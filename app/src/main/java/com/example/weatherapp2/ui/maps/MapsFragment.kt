package com.example.weatherapp2.ui.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weatherapp2.R
import com.example.weatherapp2.SharedPreferences
import com.example.weatherapp2.databinding.FragmentMapBinding

class MapsFragment : Fragment() {

    private lateinit var mapBinding: FragmentMapBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mapBinding = FragmentMapBinding.inflate(inflater, container, false)
        if (SharedPreferences.preferences.getString(getString(R.string.map), "Google") == "Google"
        ) {
            mapBinding.textGoogle.visibility = View.VISIBLE
            mapBinding.textYandex.visibility = View.GONE
        } else if (SharedPreferences.preferences.getString(getString(R.string.map), "Google") == "Yandex"
        ) {
            mapBinding.textGoogle.visibility = View.GONE
            mapBinding.textYandex.visibility = View.VISIBLE
        }

        return mapBinding.root
    }
}
