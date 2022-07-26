package com.example.submissionkedua.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.example.submissionkedua.data.remote.DetailUserResponse
import com.example.submissionkedua.data.local.entity.FavouriteUserEntity
import com.example.submissionkedua.data.remote.ApiConfig
import com.example.submissionkedua.di.Injection
import com.example.submissionkedua.util.DateHelper
import com.example.submissionkedua.util.Event
import com.example.submissionkedua.util.SettingsPreferences
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.IllegalArgumentException

class DetailViewModelFactory(private val pref: SettingsPreferences, private val username: String, private val context: Context): ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)){
            return DetailViewModel(pref, username, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

}

class DetailViewModel(private val pref: SettingsPreferences, username: String, context: Context): ViewModel(){

    companion object{
        private const val TOKEN = "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"
    }

    private val _detailUser = MutableLiveData<DetailUserResponse>()
    val detailUser: LiveData<DetailUserResponse> = _detailUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage =  MutableLiveData<Event<String?>?>()
    val errorMessage: LiveData<Event<String?>?> = _errorMessage

    private val _isFavourite = MutableLiveData<Boolean>()
    val isFavourite: LiveData<Boolean> = _isFavourite

    private val _favouritesNotification =  MutableLiveData<Event<String?>?>()
    val favouritesNotification: LiveData<Event<String?>?> = _favouritesNotification

    private val repo = Injection.provideRepository(context)

    init {
        findDetailUser(username)
        checkIfFavouriteUser(username)
    }

    private fun findDetailUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEachSearchDataDetail(username, TOKEN)
        client.enqueue(object: Callback<DetailUserResponse>{
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null){
                        _detailUser.value = responseBody.copy()
                        _errorMessage.value = null
                    }else{
                        _errorMessage.value = Event("Terjadi kesalahan, Data tidak dapat ditampilkan")
                    }
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = Event("Terjadi kesalahan, Data tidak dapat ditampilkan")
            }

        })
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    private fun checkIfFavouriteUser(username: String) {

        viewModelScope.launch {
            _isFavourite.value = repo.isFavouriteUser(username)
        }
    }

    fun saveToFavourites(){
        _isFavourite.value = true
        _favouritesNotification.value = Event("${detailUser.value?.username} added to Favourite Users")
        val data = FavouriteUserEntity(
            detailUser.value?.username ?: "",
            detailUser.value?.avatarUrl ?: "",
            detailUser.value?.name ?: "",
            detailUser.value?.repos ?: 0,
            detailUser.value?.following ?: 0,
            detailUser.value?.followers ?: 0,
            detailUser.value?.company ?: "null",
            detailUser.value?.location ?: "null",
            detailUser.value?.followingUrl ?: "null",
            detailUser.value?.followersUrl ?: "null",
            DateHelper.getCurrentDate()
        )
        viewModelScope.launch {
            repo.setFavouriteUsers(data)
        }
    }

    fun deleteFromFavourites(){
        _isFavourite.value = false
        _favouritesNotification.value = Event("${detailUser.value?.username} deleted from Favourite Users")
        viewModelScope.launch {
            repo.deleteFavouriteUser(detailUser.value?.username ?: "")
        }
    }

}