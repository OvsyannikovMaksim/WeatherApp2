package com.example.weatherapp2

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.weatherapp2.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity(), SaveDialog.NoticeDialogListener {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = getString(R.string.setting_activity_name)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.switchService.isChecked = SharedPreferences
            .preferences.getBoolean(getString(R.string.service), false)
        if (binding.switchService.isChecked) {
            binding.timeText.visibility = View.VISIBLE
            binding.textMinutes.visibility = View.VISIBLE

            binding.timeText.setText(
                SharedPreferences.preferences
                    .getInt(getString(R.string.update_time), 30).toString()
            )
        }

        binding.switchService.setOnCheckedChangeListener { _, b ->
            checkSwitch(b)
        }

        binding.saveButton.setOnClickListener {
            SaveDialog().show(supportFragmentManager, "SaveDialog")
        }
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        SharedPreferences.preferences.edit()
            .putBoolean(getString(R.string.service), binding.switchService.isChecked)
            .putInt(getString(R.string.update_time), binding.timeText.text.toString().toInt())
            .putString(getString(R.string.map), binding.mapsSpinner.selectedItem.toString())
            .apply()
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
    }

    private fun checkSwitch(bool: Boolean) {
        if (bool) {
            binding.timeText.visibility = View.VISIBLE
            binding.textMinutes.visibility = View.VISIBLE
        } else {
            binding.timeText.visibility = View.INVISIBLE
            binding.textMinutes.visibility = View.INVISIBLE
        }
    }
}
