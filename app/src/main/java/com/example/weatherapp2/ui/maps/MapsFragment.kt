package com.example.weatherapp2.ui.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weatherapp2.R
import com.example.weatherapp2.databinding.FragmentMapBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mapBinding: FragmentMapBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mapBinding = FragmentMapBinding.inflate(inflater, container, false)
        val q = childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
        q.getMapAsync(this)
        return mapBinding.root
    }

    override fun onMapReady(p0: GoogleMap) {
    }
}
