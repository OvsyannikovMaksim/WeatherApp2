package com.example.weatherapp2.ui.maps

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.weatherapp2.R
import com.example.weatherapp2.databinding.FragmentMapBinding
import com.example.weatherapp2.model.common.CityFullInfo
import com.example.weatherapp2.model.db.DataBase
import com.example.weatherapp2.model.repository.LocalRepoImpl
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.math.roundToInt

class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mapBinding: FragmentMapBinding
    private val mapsViewModel by viewModels<MapsViewModel> {
        MapsViewModelFactory(
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
        mapBinding = FragmentMapBinding.inflate(inflater, container, false)
        val supportMapFragment = childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
        supportMapFragment.getMapAsync(this)
        mapsViewModel.loadCitiesFullInfoFromRepo()
        return mapBinding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.d("Map", "onMapReady")
        googleMap.uiSettings.isZoomControlsEnabled = true
        mapsViewModel.citiesFullInfo.observe(viewLifecycleOwner) { citiesFullInfo ->
            val cityMarkers = createCityMarkers(citiesFullInfo)
            cityMarkers.forEach { googleMap.addMarker(it) }
            googleMap.setOnInfoWindowClickListener {
                val bundle = Bundle()
                bundle.putDoubleArray(
                    "FullInfoKey",
                    doubleArrayOf(it.position.latitude, it.position.longitude)
                )
                findNavController().navigate(
                    R.id.action_navigation_maps_to_navigation_weatherFullInfoFragment,
                    bundle
                )
            }
        }
    }

    private fun createCityMarkers(citiesFullInfo: List<CityFullInfo>): List<MarkerOptions> {
        val markers = mutableListOf<MarkerOptions>()
        for (cityInfo in citiesFullInfo) {
            val marker = MarkerOptions()
            if (cityInfo.comment != "") {
                marker.snippet(cityInfo.comment)
            }
            marker.position(LatLng(cityInfo.lat, cityInfo.lon))
            val temp = requireContext().getString(
                R.string.celsius,
                cityInfo.current!!.temp.roundToInt()
            )
            marker.title("${cityInfo.name}, ${cityInfo.country} $temp")
            markers.add(marker)
        }
        return markers
    }
}
