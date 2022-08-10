package com.example.weatherapp2.ui.saveDialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.weatherapp2.R
import com.example.weatherapp2.model.common.CityFullInfo
import com.example.weatherapp2.model.db.DataBase
import com.example.weatherapp2.model.repository.LocalRepoImpl

class SaveDialog : DialogFragment() {

    private val saveDialogModel by viewModels<SaveDialogModel> {
        SaveDialogModelFactory(
            LocalRepoImpl(
                DataBase.getDataBase(this.requireContext())!!
                    .localDao()
            )
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.save_button_text)
                .setMessage(R.string.dialog_text)
                .setPositiveButton(
                    R.string.save_button_text
                ) { _, _ ->
                    requireArguments().getParcelable<CityFullInfo>("CityKey")
                        ?.let { it1 -> saveDialogModel.putCityToRepo(it1) }
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
