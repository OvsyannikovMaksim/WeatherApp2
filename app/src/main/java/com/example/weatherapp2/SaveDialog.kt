package com.example.weatherapp2

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class SaveDialog: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.save_button_text)
                .setMessage(R.string.dialog_text)
                .setPositiveButton(R.string.save_button_text
                ) { dialog, id ->
                }
                .setNegativeButton(R.string.cancel_button_text
                ) { dialog, id ->
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}