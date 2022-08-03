package com.example.weatherapp2.ui.editCity

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.weatherapp2.R
import com.example.weatherapp2.databinding.FragmentEditCityBinding
import com.example.weatherapp2.model.common.CityCoordinate
import com.example.weatherapp2.model.common.CityInfo
import com.squareup.picasso.Picasso

class EditCityFragment : Fragment() {

    private lateinit var fragmentEditCityBinding: FragmentEditCityBinding
    private var cityInfo: CityInfo? = null
    private var cityCoordinate: CityCoordinate? = null
    private lateinit var getPictureFromGalleryLauncher: ActivityResultLauncher<Intent>
    private var picUri: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getPictureFromGalleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            getPictureFromGallery(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentEditCityBinding = FragmentEditCityBinding.inflate(inflater, container, false)
        cityInfo = requireArguments().getParcelable("CityInfoKey")
        cityCoordinate = requireArguments().getParcelable("CityCoordinateKey")
        return fragmentEditCityBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (cityInfo == null && cityCoordinate != null) {
            createBinding(cityCoordinate!!)
        } else if (cityInfo != null && cityCoordinate == null) {
            createBinding(cityInfo!!)
        }
    }

    private fun createBinding(cityInfo: CityInfo) {
        fragmentEditCityBinding.cityName.text = createFullCityName(cityInfo)
        fragmentEditCityBinding.addPictureButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            getPictureFromGalleryLauncher.launch(intent)
        }
        fragmentEditCityBinding.saveCityButton.setOnClickListener {
            val city = CityCoordinate(
                cityInfo.name,
                cityInfo.state,
                cityInfo.country,
                cityInfo.lat,
                cityInfo.lon,
                fragmentEditCityBinding.inputCityCommentEditText.text.toString(),
                picUri
            )
            val bundle = Bundle()
            bundle.putParcelable("CityKey", city)
            it.findNavController().navigate(R.id.action_navigation_edit_city_to_save_dialog, bundle)
        }
    }

    private fun createBinding(cityCoordinate: CityCoordinate) {
        fragmentEditCityBinding.cityName.text = createFullCityName(cityCoordinate)
        fragmentEditCityBinding.inputCityCommentEditText.setText(cityCoordinate.comment)
        if (cityCoordinate.pic_uri != null) {
            Picasso.with(context).load(Uri.parse(cityCoordinate.pic_uri))
                .into(fragmentEditCityBinding.cityPic)
        }

        fragmentEditCityBinding.addPictureButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            getPictureFromGalleryLauncher.launch(intent)
        }

        fragmentEditCityBinding.saveCityButton.setOnClickListener {
            val city = CityCoordinate(
                cityCoordinate.name,
                cityCoordinate.state,
                cityCoordinate.country,
                cityCoordinate.lat,
                cityCoordinate.lon,
                fragmentEditCityBinding.inputCityCommentEditText.text.toString(),
                picUri
            )
            val bundle = Bundle()
            bundle.putParcelable("CityKey", city)
            it.findNavController().navigate(R.id.action_navigation_edit_city_to_save_dialog, bundle)
        }
    }

    private fun createFullCityName(cityInfo: CityInfo): String {
        return if (cityInfo.state != null) {
            "${cityInfo.name}, ${cityInfo.state}, ${cityInfo.country}"
        } else {
            "${cityInfo.name}, ${cityInfo.country}"
        }
    }

    private fun createFullCityName(cityCoordinate: CityCoordinate): String {
        return if (cityCoordinate.state != null) {
            "${cityCoordinate.name}, ${cityCoordinate.state}, ${cityCoordinate.country}"
        } else {
            "${cityCoordinate.name}, ${cityCoordinate.country}"
        }
    }

    private fun getPictureFromGallery(result: ActivityResult) {
        if (result.resultCode == RESULT_OK && result.data != null) {
            picUri = result.data!!.data.toString()
            Picasso.with(context).load(result.data!!.data).into(fragmentEditCityBinding.cityPic)
        }
    }
}
