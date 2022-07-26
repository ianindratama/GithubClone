package com.example.submissionkedua.viewmodel

import androidx.lifecycle.*
import com.example.submissionkedua.data.remote.ItemsItem
import com.example.submissionkedua.data.remote.ApiConfig
import com.example.submissionkedua.util.SettingsPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.IllegalArgumentException

class SocialViewModelFactory(private val pref: SettingsPreferences, private val username: String, private val tabOption: Boolean): ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SocialViewModel::class.java)){
            return SocialViewModel(pref, username, tabOption) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}

class SocialViewModel(private val pref: SettingsPreferences, username: String, tabOption: Boolean): ViewModel() {

    companion object{
        private const val TOKEN = "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"
    }

    private val _itemsItem = MutableLiveData<List<ItemsItem>>()
    val itemsItem: LiveData<List<ItemsItem>> = _itemsItem

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage = _errorMessage

    init {
        findSocialUser(username, tabOption)
    }

    private fun findSocialUser(username: String, tabOption: Boolean) {
        _isLoading.value = true

        val client: Call<List<ItemsItem>> = if (!tabOption){
            ApiConfig.getApiService().getUserFollowingDetail(username, TOKEN)
        }else{
            ApiConfig.getApiService().getUserFollowersDetail(username, TOKEN)
        }

        client.enqueue(object: Callback<List<ItemsItem>>{
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null){
                        _itemsItem.value = responseBody!!
                        _errorMessage.value = null
                    }else{
                        _errorMessage.value = "Terjadi kesalahan, Data tidak dapat ditampilkan"
                    }
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Terjadi kesalahan, Data tidak dapat ditampilkan"
            }

        })

    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

}