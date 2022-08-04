package com.example.weatherapp2.ui.deleteDialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.weatherapp2.R
import com.example.weatherapp2.model.db.DataBase
import com.example.weatherapp2.model.repository.LocalRepoImpl

class DeleteDialog : DialogFragment() {

    private lateinit var deleteDialogModelFactory: DeleteDialogModelFactory
    private lateinit var deleteDialogModel: DeleteDialogModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val dataBase = DataBase.getDataBase(this.requireContext())!!
        val localDao = dataBase.localDao()
        val localRepo = LocalRepoImpl(localDao)
        deleteDialogModelFactory = DeleteDialogModelFactory(localRepo)
        deleteDialogModel = ViewModelProvider(this, deleteDialogModelFactory)[DeleteDialogModel::class.java]
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.delete_button_text)
                .setMessage(R.string.delete_dialog_text)
                .setPositiveButton(
                    R.string.delete_button_text
                ) { _, _ ->
                    val cityId = requireArguments().getInt("CityIdKey")
                    deleteDialogModel.deleteCity(cityId)
                    findNavController().navigate(
                        R.id.action_navigation_delete_dialog_to_navigation_home
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
