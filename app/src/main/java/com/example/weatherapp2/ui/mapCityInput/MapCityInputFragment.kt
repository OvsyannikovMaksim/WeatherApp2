package com.example.weatherapp2.ui.mapCityInput

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.example.weatherapp2.R
import com.example.weatherapp2.databinding.FragmentMapBinding
import com.example.weatherapp2.model.api.OpenWeatherApiRetrofit
import com.example.weatherapp2.model.common.CityFullInfo
import com.example.weatherapp2.model.repository.CityWeatherRepoImpl
import com.example.weatherapp2.model.repository.LocalDataCache
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map

class MapCityInputFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mapBinding: FragmentMapBinding
    private val mapCityInputModel by viewModels<MapCityInputModel> {
        MapCityInputModelFactory(
            CityWeatherRepoImpl(OpenWeatherApiRetrofit.openWeatherApi)
        )
    }
    private var nothingToast: Toast? = null
    private var googleMarker: Marker? = null
    private var yandexMarker: PlacemarkMapObject? = null
    private val onMapListener = object : InputListener {
        override fun onMapTap(p0: Map, p1: Point) {
            mapCityInputModel.resultOfSearch = MutableLiveData()
            mapCityInputModel.getCitiesFromLine(p1.latitude.toString(), p1.longitude.toString())
            mapCityInputModel.resultOfSearch.observe(viewLifecycleOwner) { cityInfo ->
                if (cityInfo != null) {
                    cityInfo1 = cityInfo
                    addMarkerYandex(cityInfo)
                } else {
                    nothingToast!!.show()
                }
            }
        }
        override fun onMapLongTap(p0: Map, p1: Point) {
        }
    }
    private var cityInfo1: CityFullInfo? = null
    private val onMarkListener = MapObjectTapListener { _, _ ->
        val bundle = Bundle()
        bundle.putParcelable("CityInfoKey", cityInfo1!!)
        findNavController().navigate(
            R.id.action_mapCityInputFragment_to_navigation_edit_city,
            bundle
        )
        true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mapBinding = FragmentMapBinding.inflate(inflater, container, false)
        nothingToast = Toast.makeText(requireContext(), "Nothing here", Toast.LENGTH_SHORT)
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
            mapBinding.yandexMap.map.addInputListener(onMapListener)
        }
    }

    override fun onStop() {
        super.onStop()
        mapBinding.yandexMap.onStop()
        MapKitFactory.getInstance().onStop()
        yandexMarker = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.setOnMapClickListener {
            mapCityInputModel.resultOfSearch = MutableLiveData()
            mapCityInputModel.getCitiesFromLine(it.latitude.toString(), it.longitude.toString())
            mapCityInputModel.resultOfSearch.observe(viewLifecycleOwner) { cityInfo ->
                if (cityInfo != null) {
                    setMarkerGoogle(cityInfo, googleMap)
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
                } else {
                    nothingToast!!.show()
                }
            }
        }
    }

    private fun setMarkerGoogle(cityInfo: CityFullInfo, googleMap: GoogleMap) {
        if (googleMarker != null) {
            googleMarker!!.remove()
        }
        val markerOpt = MarkerOptions()
        val latLng = LatLng(cityInfo.lat, cityInfo.lon)
        markerOpt.position(latLng)
        markerOpt.title("${cityInfo.name}, ${cityInfo.country}")
        googleMarker = googleMap.addMarker(markerOpt)
        googleMarker?.showInfoWindow()
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    private fun addMarkerYandex(cityInfo: CityFullInfo) {
        val textStyle = TextStyle()
        textStyle.size = 8f
        textStyle.color = Color.BLACK
        textStyle.offset = 1000f
        textStyle.textOptional = true
        Toast.makeText(
            requireContext(),
            "Please, click on marker one more time to add",
            Toast.LENGTH_SHORT
        ).show()
        val point = Point(cityInfo.lat, cityInfo.lon)
        if (yandexMarker != null) {
            yandexMarker!!.setText("${cityInfo.name}, ${cityInfo.country}", textStyle)
            yandexMarker!!.geometry = point
            yandexMarker!!.removeTapListener(onMarkListener)
        } else {
            yandexMarker = mapBinding.yandexMap.map.mapObjects.addPlacemark(point)
            yandexMarker!!.setText("${cityInfo.name}, ${cityInfo.country}", textStyle)
        }
        yandexMarker!!.addTapListener(onMarkListener)
        val currentPos = mapBinding.yandexMap.map.cameraPosition
        mapBinding.yandexMap.map.move(
            CameraPosition(point, currentPos.zoom, currentPos.azimuth, currentPos.tilt),
            Animation(Animation.Type.SMOOTH, 1f),
            null
        )
    }
}
