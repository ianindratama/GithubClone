package com.example.submissionkedua.util

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.submissionkedua.dataStore
import com.example.submissionkedua.databinding.ActivitySettingsBinding
import com.example.submissionkedua.viewmodel.SettingsViewModel
import com.example.submissionkedua.viewmodel.ViewModelFactory


class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Settings"

        val pref = SettingsPreferences.getInstance(dataStore)
        val settingsViewModel = obtainViewModel(pref)

        settingsViewModel.getThemeSettings().observe(this)
        {   isDarkModeActive: Boolean ->
            if(isDarkModeActive){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false
            }
        }

        binding.switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingsViewModel.saveThemeSetting(isChecked)
        }

        binding.aboutDeveloper.setOnClickListener {
            val intent = Intent(this@SettingsActivity, ProfileActivity::class.java)
            startActivity(intent)
        }

    }

    private fun obtainViewModel(pref: SettingsPreferences): SettingsViewModel {
        val factory = ViewModelFactory.getInstance(pref)
        return ViewModelProvider(this, factory).get(SettingsViewModel::class.java)
    }
}