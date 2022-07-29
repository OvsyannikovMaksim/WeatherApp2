package com.example.weatherapp2.ui.maps

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weatherapp2.databinding.FragmentMapBinding
import com.example.weatherapp2.model.repository.LocalDataCache

class MapsFragment : Fragment() {

    private lateinit var mapBinding: FragmentMapBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mapBinding = FragmentMapBinding.inflate(inflater, container, false)
        changeMap()

        return mapBinding.root
    }

    override fun onResume() {
        super.onResume()
        changeMap()
    }

    private fun changeMap(){
        if (LocalDataCache.getChosenMapId() == 0
        ) {
            mapBinding.textGoogle.visibility = View.VISIBLE
            mapBinding.textYandex.visibility = View.GONE
        } else if (LocalDataCache.getChosenMapId() == 1
        ) {
            mapBinding.textGoogle.visibility = View.GONE
            mapBinding.textYandex.visibility = View.VISIBLE
        }
    }
}
