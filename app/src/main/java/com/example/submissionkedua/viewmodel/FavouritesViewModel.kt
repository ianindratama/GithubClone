package com.example.submissionkedua.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.example.submissionkedua.di.Injection
import com.example.submissionkedua.util.Event
import com.example.submissionkedua.util.SettingsPreferences
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class FavouritesViewModelFactory(private val pref: SettingsPreferences, private val context: Context): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavouritesViewModel::class.java)){
            return FavouritesViewModel(pref, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

}


class FavouritesViewModel(private val pref: SettingsPreferences, context: Context): ViewModel(){

    private val _favouritesNotification = MutableLiveData<Event<String?>>()
    val favouritesNotification: LiveData<Event<String?>> = _favouritesNotification

    private val repo = Injection.provideRepository(context)

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun getFavouriteUsers() = repo.getFavouriteUsers()

    fun deleteFromFavourites(username: String){
        _favouritesNotification.value = Event("$username deleted from Favourite Users")
        viewModelScope.launch {
            repo.deleteFavouriteUser(username)
        }
    }

}