package com.example.weatherapp2.ui.editCity

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.example.weatherapp2.model.common.CityFullInfo
import com.squareup.picasso.Picasso

class EditCityFragment : Fragment() {

    private lateinit var fragmentEditCityBinding: FragmentEditCityBinding
    private var cityFullInfo: CityFullInfo? = null
    private lateinit var getPictureFromGalleryLauncher: ActivityResultLauncher<Intent>
    private lateinit var getGalleryPermissionLauncher: ActivityResultLauncher<String>
    private var picUri: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getPictureFromGalleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            getPictureFromGallery(it)
        }

        getGalleryPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.d("requestPermissionLauncher", "if $isGranted")
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                getPictureFromGalleryLauncher.launch(intent)
            } else {
                Log.d("requestPermissionLauncher", "else $isGranted")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentEditCityBinding = FragmentEditCityBinding.inflate(inflater, container, false)
        cityFullInfo = requireArguments().getParcelable("CityInfoKey")
        return fragmentEditCityBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createBinding(cityFullInfo!!)
    }

    private fun createBinding(cityFullInfo: CityFullInfo) {
        fragmentEditCityBinding.cityName.text = createFullCityName(cityFullInfo)
        fragmentEditCityBinding.inputCityCommentEditText.setText(cityFullInfo.comment)
        if (cityFullInfo.pic != null) {
            Picasso.get().load(Uri.parse(cityFullInfo.pic))
                .into(fragmentEditCityBinding.cityPic)
        }
        fragmentEditCityBinding.addPictureButton.setOnClickListener {
            getGalleryPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        fragmentEditCityBinding.saveCityButton.setOnClickListener {
            cityFullInfo.pic = picUri
            cityFullInfo.comment = fragmentEditCityBinding.inputCityCommentEditText.text.toString()
            val bundle = Bundle()
            bundle.putParcelable("CityKey", cityFullInfo)
            it.findNavController().navigate(R.id.action_navigation_edit_city_to_save_dialog, bundle)
        }
    }

    private fun createFullCityName(cityFullInfo: CityFullInfo): String {
        return if (cityFullInfo.state != null) {
            "${cityFullInfo.name}, ${cityFullInfo.state}, ${cityFullInfo.country}"
        } else {
            "${cityFullInfo.name}, ${cityFullInfo.country}"
        }
    }

    private fun getPictureFromGallery(result: ActivityResult) {
        if (result.resultCode == RESULT_OK && result.data != null) {
            picUri = result.data!!.data.toString()
            Picasso.get().load(result.data!!.data).into(fragmentEditCityBinding.cityPic)
        }
    }
}
