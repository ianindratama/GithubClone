package com.example.submissionkedua.viewmodel

import androidx.lifecycle.*
import com.example.submissionkedua.data.remote.ItemsItem
import com.example.submissionkedua.data.remote.SearchResponse
import com.example.submissionkedua.data.remote.ApiConfig
import com.example.submissionkedua.util.Event
import com.example.submissionkedua.util.SettingsPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.IllegalArgumentException

class SearchViewModelFactory(private val pref: SettingsPreferences, private val searchInput: String): ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)){
            return SearchViewModel(pref, searchInput) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

}

class SearchViewModel(private val pref: SettingsPreferences, searchInput: String): ViewModel() {

    companion object{
        private const val TOKEN = "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"
    }

    private val _itemsItem = MutableLiveData<List<ItemsItem>>()
    val itemsItem: LiveData<List<ItemsItem>> = _itemsItem

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage =  MutableLiveData<Event<String?>?>()
    val errorMessage: LiveData<Event<String?>?> = _errorMessage

    init {
        findSearchUser(searchInput)
    }

    private fun findSearchUser(searchInput: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSearchData(searchInput, TOKEN)
        client.enqueue(object: Callback<SearchResponse>{
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null){
                        _itemsItem.value = responseBody.items
                        _errorMessage.value = null
                    }else{
                        _errorMessage.value = Event("Terjadi kesalahan, Data tidak dapat ditampilkan")
                    }
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = Event("Terjadi kesalahan, Data tidak dapat ditampilkan")
            }
        })
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

}