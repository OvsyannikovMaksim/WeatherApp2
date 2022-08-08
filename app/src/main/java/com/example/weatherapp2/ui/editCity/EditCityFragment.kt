package com.example.weatherapp2.ui.editCity

import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.format.DateFormat
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
    private lateinit var cityFullInfo: CityFullInfo
    private lateinit var getPictureFromGalleryLauncher: ActivityResultLauncher<Intent>
    private lateinit var getPictureFromCameraLauncher: ActivityResultLauncher<Uri>
    private lateinit var getStoragePermissionLauncher: ActivityResultLauncher<String>
    private lateinit var pic: String
    private lateinit var uri: Uri

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getPictureFromGalleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            getPictureFromGallery(it)
        }

        getPictureFromCameraLauncher = registerForActivityResult(
            ActivityResultContracts.TakePicture()
        ) {
            if (it) {
                pic = uri.toString()
                Picasso.get().load(uri)
                    .into(fragmentEditCityBinding.cityPic)
            }
        }

        getStoragePermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                launchCamera()

            } else {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
                    launchCamera()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentEditCityBinding = FragmentEditCityBinding.inflate(inflater, container, false)
        cityFullInfo = requireArguments().getParcelable("CityInfoKey")!!
        return fragmentEditCityBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentEditCityBinding.cityName.text = createFullCityName(cityFullInfo)
        fragmentEditCityBinding.inputCityCommentEditText.setText(cityFullInfo.comment)
        if (cityFullInfo.pic != null) {
            Picasso.get().load(Uri.parse(cityFullInfo.pic))
                .into(fragmentEditCityBinding.cityPic)
        }
        fragmentEditCityBinding.addPictureGalleryButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            getPictureFromGalleryLauncher.launch(intent)
        }
        fragmentEditCityBinding.addPictureCameraButton.setOnClickListener {
            getStoragePermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        fragmentEditCityBinding.saveCityButton.setOnClickListener {
            cityFullInfo.pic = pic
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
            pic = result.data!!.data.toString()
            Picasso.get().load(result.data!!.data).into(fragmentEditCityBinding.cityPic)
        }
    }
    //using FileProvider
   /* private fun getPhotoUri(): Uri {
        val df = DateFormat.format("yyyyMMdd_HHmmss", System.currentTimeMillis())
        val filename = "$df.jpg"
        val dir = File(Environment.getExternalStoragePublicDirectory("DCIM"), "WeatherApp")
        if (!dir.exists()) dir.mkdir()
        val file = File(dir, filename)
        return FileProvider.getUriForFile(
            requireContext(),
            BuildConfig.APPLICATION_ID + ".provider",
            file
        )
    }*/

    private fun getPhotoUri(): Uri{
        val df = DateFormat.format("yyyyMMdd_HHmmss", System.currentTimeMillis())
        val path = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val contentValues = ContentValues().apply{
            put(MediaStore.Images.Media.DISPLAY_NAME, "$df.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        return requireContext().contentResolver.insert(path, contentValues)!!
    }

    private fun launchCamera(){
        uri = getPhotoUri()
        getPictureFromCameraLauncher.launch(uri)
    }
}
