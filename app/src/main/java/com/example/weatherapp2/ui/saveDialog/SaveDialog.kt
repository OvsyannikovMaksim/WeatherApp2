package com.example.weatherapp2.ui.saveDialog

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.weatherapp2.R
import com.example.weatherapp2.model.common.CityFullInfo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SaveDialog : DialogFragment() {

    private val saveDialogModel: SaveDialogModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.save_button_text)
                .setMessage(R.string.dialog_text)
                .setPositiveButton(
                    R.string.save_button_text
                ) { _, _ ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        requireArguments().getParcelable("CityKey", CityFullInfo::class.java)
                            ?.let { it1 -> saveDialogModel.putCityToRepo(it1) }
                    } else {
                        requireArguments().getParcelable<CityFullInfo>("CityKey")
                            ?.let { it1 -> saveDialogModel.putCityToRepo(it1) }
                    }
                    findNavController().navigate(
                        R.id.action_navigation_save_dialog_to_navigation_home
                    )
                }
                .setNegativeButton(
                    R.string.cancel_button_text
                ) { _, _ ->
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
