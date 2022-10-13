package com.example.weatherapp2.ui.maps

import android.graphics.Color
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
import com.example.weatherapp2.model.repository.LocalDataCache
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.TextStyle
import com.yandex.mapkit.map.UserData
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mapBinding: FragmentMapBinding
    private val mapsViewModel: MapsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        MapKitFactory.initialize(requireContext())
        mapBinding = FragmentMapBinding.inflate(inflater, container, false)
        return mapBinding.root
    }

    override fun onStart() {
        super.onStart()
        if (LocalDataCache.getChosenMapId() == 0) {
            mapBinding.googleMap.visibility = View.VISIBLE
            mapBinding.yandexMap.visibility = View.GONE
            val supportMapFragment =
                childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
            supportMapFragment.getMapAsync(this)
        }
        if (LocalDataCache.getChosenMapId() == 1) {
            mapBinding.googleMap.visibility = View.GONE
            mapBinding.yandexMap.visibility = View.VISIBLE
            mapBinding.yandexMap.onStart()
            MapKitFactory.getInstance().onStart()
            createYandexMapCityMarkers()
        }
    }

    override fun onStop() {
        super.onStop()
        mapBinding.yandexMap.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.d("Map", "onMapReady")
        googleMap.uiSettings.isZoomControlsEnabled = true
        mapsViewModel.citiesFullInfo.observe(viewLifecycleOwner) { citiesFullInfo ->
            val cityMarkers = createGoogleCityMarkers(citiesFullInfo)
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

    private fun createGoogleCityMarkers(citiesFullInfo: List<CityFullInfo>): List<MarkerOptions> {
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

    private fun createYandexMapCityMarkers() {
        val textStyle = TextStyle()
        textStyle.size = 8f
        textStyle.color = Color.BLACK
        textStyle.offset = 1000f
        textStyle.textOptional = true
        mapsViewModel.citiesFullInfo.observe(viewLifecycleOwner) { citiesFullInfo ->
            for (cityInfo in citiesFullInfo) {
                val placeMark = mapBinding.yandexMap.map.mapObjects.addPlacemark(
                    Point(cityInfo.lat, cityInfo.lon)
                )

                val temp = requireContext().getString(
                    R.string.celsius,
                    cityInfo.current!!.temp.roundToInt()
                )
                placeMark.setText("${cityInfo.name}, ${cityInfo.country} $temp", textStyle)
                placeMark.userData = UserData()
                placeMark.addTapListener { _, _ ->
                    val bundle = Bundle()
                    bundle.putDoubleArray(
                        "FullInfoKey",
                        doubleArrayOf(cityInfo.lat, cityInfo.lon)
                    )
                    findNavController().navigate(
                        R.id.action_navigation_maps_to_navigation_weatherFullInfoFragment,
                        bundle
                    )
                    true
                }
            }
        }
    }
}
