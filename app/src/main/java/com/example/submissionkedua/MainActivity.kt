package com.example.submissionkedua

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.submissionkedua.databinding.ActivityMainBinding
import com.example.submissionkedua.util.SettingsPreferences
import com.example.submissionkedua.viewmodel.MainViewModel
import com.example.submissionkedua.viewmodel.ViewModelFactory

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pref = SettingsPreferences.getInstance(dataStore)
        val mainViewModel = obtainViewModel(pref)

        mainViewModel.getThemeSettings().observe(this)
        {   isDarkModeActive: Boolean ->
            if(isDarkModeActive){
                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
            } else {
                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Github"

    }

    private fun obtainViewModel(pref: SettingsPreferences): MainViewModel {
        val factory = ViewModelFactory.getInstance(pref)
        return ViewModelProvider(this, factory).get(MainViewModel::class.java)
    }


}