package com.example.weatherapp2

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.weatherapp2.databinding.ActivitySettingsBinding
import com.example.weatherapp2.model.repository.LocalDataCache
import com.example.weatherapp2.ui.dialogs.SaveSettingsDialog

class SettingsActivity : AppCompatActivity(), SaveSettingsDialog.NoticeDialogListener {
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
        binding.mapsSpinner.setSelection(LocalDataCache.getChosenMapId())
        binding.switchService.isChecked = LocalDataCache.getServiceState()
        if (binding.switchService.isChecked) {
            binding.timeText.visibility = View.VISIBLE
            binding.textMinutes.visibility = View.VISIBLE

            binding.timeText.setText(
                LocalDataCache.getServiceUpdateTime().toString()
            )
        }

        binding.switchService.setOnCheckedChangeListener { _, isViewsVisible ->
            syncPeriodViewsVisibility(isViewsVisible)
        }

        binding.saveButton.setOnClickListener {
            SaveSettingsDialog().show(supportFragmentManager, "SaveDialog")
        }
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        LocalDataCache.setServiceUpdateTime(binding.timeText.text.toString().toInt())
        LocalDataCache.setServiceState(binding.switchService.isChecked)
        LocalDataCache.setChosenMapId(binding.mapsSpinner.selectedItemPosition)
        finish()
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
    }

    private fun syncPeriodViewsVisibility(isViewsVisible: Boolean) {
        if (isViewsVisible) {
            binding.timeText.visibility = View.VISIBLE
            binding.textMinutes.visibility = View.VISIBLE
        } else {
            binding.timeText.visibility = View.INVISIBLE
            binding.textMinutes.visibility = View.INVISIBLE
        }
    }
}
