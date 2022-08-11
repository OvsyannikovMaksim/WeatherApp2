package com.example.weatherapp2.ui.mapCityInput

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.weatherapp2.R
import com.example.weatherapp2.databinding.FragmentMapBinding
import com.example.weatherapp2.model.api.OpenWeatherApiRetrofit
import com.example.weatherapp2.model.common.CityFullInfo
import com.example.weatherapp2.model.repository.CityWeatherRepoImpl
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapCityInputFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mapBinding: FragmentMapBinding
    private val mapCityInputModel by viewModels<MapCityInputModel> {
        MapCityInputModelFactory(
            CityWeatherRepoImpl(OpenWeatherApiRetrofit.openWeatherApi)
        )
    }
    private var marker: Marker? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mapBinding = FragmentMapBinding.inflate(inflater, container, false)
        val supportMapFragment = childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
        supportMapFragment.getMapAsync(this)
        return mapBinding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.setOnMapClickListener {
            mapCityInputModel.getCitiesFromLine(it.latitude.toString(), it.longitude.toString())
            mapCityInputModel.getResultOfSearch().observe(viewLifecycleOwner) { cityInfo ->
                setMarker(cityInfo, googleMap)
                Toast.makeText(
                    requireContext(),
                    "Please, click on marker one more time to add",
                    Toast.LENGTH_SHORT
                ).show()
                googleMap.setOnMarkerClickListener {
                    val bundle = Bundle()
                    bundle.putParcelable("CityInfoKey", cityInfo)
                    findNavController().navigate(
                        R.id.action_mapCityInputFragment_to_navigation_edit_city,
                        bundle
                    )
                    return@setOnMarkerClickListener false
                }
            }
        }
    }

    private fun setMarker(cityInfo: CityFullInfo, googleMap: GoogleMap) {
        if (marker != null) {
            marker!!.remove()
        }
        val markerOpt = MarkerOptions()
        val latLng = LatLng(cityInfo.lat, cityInfo.lon)
        markerOpt.position(latLng)
        markerOpt.title("${cityInfo.name}, ${cityInfo.country}")
        marker = googleMap.addMarker(markerOpt)
        marker?.showInfoWindow()
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
    }
}
