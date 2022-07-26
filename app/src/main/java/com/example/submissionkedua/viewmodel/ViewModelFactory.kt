package com.example.submissionkedua.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.submissionkedua.util.SettingsPreferences
import java.lang.IllegalArgumentException

class ViewModelFactory private constructor(
    private val pref: SettingsPreferences?
): ViewModelProvider.NewInstanceFactory(){

    companion object{
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(
            pref: SettingsPreferences?
        ): ViewModelFactory{
            if (INSTANCE == null){
                synchronized(ViewModelFactory::class.java){
                    INSTANCE = ViewModelFactory(
                        pref
                    )
                }
            }
            return INSTANCE as ViewModelFactory
        }

    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return when {
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {

                SettingsViewModel(pref as SettingsPreferences) as T

            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {

                MainViewModel(pref as SettingsPreferences) as T

            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }

    }

}