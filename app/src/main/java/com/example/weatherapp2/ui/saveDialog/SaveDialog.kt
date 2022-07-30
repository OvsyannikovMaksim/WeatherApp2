package com.example.weatherapp2.ui.saveDialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.weatherapp2.R
import com.example.weatherapp2.model.common.CityCoordinate
import com.example.weatherapp2.model.db.DataBase
import com.example.weatherapp2.model.repository.LocalRepoImpl

class SaveDialog : DialogFragment() {

    private lateinit var saveDialogModelFactory: SaveDialogModelFactory
    private lateinit var saveDialogModel: SaveDialogModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val dataBase = DataBase.getDataBase(this.context!!)!!
        val localDao = dataBase.localDao()
        val localRepo = LocalRepoImpl(localDao)
        saveDialogModelFactory = SaveDialogModelFactory(localRepo)
        saveDialogModel = ViewModelProvider(this, saveDialogModelFactory).get(
            SaveDialogModel::class.java
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
                    arguments!!.getParcelable<CityCoordinate>("CityKey")
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
