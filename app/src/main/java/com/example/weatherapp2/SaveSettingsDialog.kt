package com.example.weatherapp2

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class SaveSettingsDialog : DialogFragment() {

    private lateinit var listener: NoticeDialogListener

    interface NoticeDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as NoticeDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                (
                    context.toString() +
                        " must implement NoticeDialogListener"
                    )
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.save_button_text)
                .setMessage(R.string.dialog_text)
                .setPositiveButton(
                    R.string.save_button_text
                ) { _, _ ->
                    listener.onDialogPositiveClick(this)
                }
                .setNegativeButton(
                    R.string.cancel_button_text
                ) { _, _ ->
                    listener.onDialogNegativeClick(this)
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
