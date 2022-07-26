package com.example.submissionkedua

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.example.submissionkedua.adapter.SectionPagerAdapter
import com.example.submissionkedua.data.remote.DetailUserResponse
import com.example.submissionkedua.databinding.ActivityDetailBinding
import com.example.submissionkedua.util.SettingsActivity
import com.example.submissionkedua.util.SettingsPreferences
import com.example.submissionkedua.viewmodel.DetailViewModel
import com.example.submissionkedua.viewmodel.DetailViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val _username = MutableLiveData<String?>()
    val username: LiveData<String?> = _username

    companion object{
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail Github User"

        _username.value = DetailActivityArgs.fromBundle(intent.extras as Bundle).username

        if ( (username.value == null) or (username.value == "Default Value")){

            if (intent.extras != null){
                _username.value = intent.getStringExtra("fromFavouritesActivity")
            }

        }

        val pref = SettingsPreferences.getInstance(dataStore)
        val detailViewModel: DetailViewModel by viewModels { DetailViewModelFactory(pref, username.value.toString(), this) }

        detailViewModel.getThemeSettings().observe(this)
        {   isDarkModeActive: Boolean ->
            if(isDarkModeActive){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        detailViewModel.detailUser.observe(this){
            setDetailUserData(it)
        }

        detailViewModel.isFavourite.observe(this){
            if (it){
                binding.fabFavourites.setImageResource(R.drawable.ic_baseline_favorite_24)
                binding.fabFavourites.imageTintList = ColorStateList.valueOf(Color.rgb(244, 67, 54))
                binding.fabFavourites.setOnClickListener { detailViewModel.deleteFromFavourites() }
            }else{
                binding.fabFavourites.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                binding.fabFavourites.setOnClickListener { detailViewModel.saveToFavourites() }
            }
        }

        detailViewModel.isLoading.observe(this){
            showLoading(it)
        }

        detailViewModel.errorMessage.observe(this){

            it?.getContentIfNotHandled().let { toastText ->
                if (toastText != null) {
                    Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
                }
            }

        }

        detailViewModel.favouritesNotification.observe(this){

            it?.getContentIfNotHandled().let { toastText ->
                if (toastText != null) {
                    Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
                }
            }

        }

    }

    private fun setDetailUserData(it: DetailUserResponse?) {

        Glide.with(this)
            .load(it?.avatarUrl)
            .circleCrop()
            .into(binding.userImageDetail)

        binding.userNameDetail.text = it?.name ?: it?.username
        binding.userUsernameDetail.text = it?.username

        binding.userReposFollowingFollowersDetail.text = buildString {
            append("${it?.repos} Repos ")
            append(resources.getString(R.string.middle_dot))
            append(" ${it?.following} following ")
            append(resources.getString(R.string.middle_dot))
            append(" ${it?.followers} followers")
        }

        binding.userCompanyDetail.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.domain, 0, 0, 0)
        binding.userCompanyDetail.text = it?.company ?: "null"

        binding.userLocationDetail.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.map, 0, 0, 0)
        binding.userLocationDetail.text = it?.location ?: "null"

        val sectionPagerAdapter = SectionPagerAdapter(this, username.value.toString())

        binding.viewPager.adapter = sectionPagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager){ tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

    }

    private fun showLoading(isLoading: Boolean) {
        if(isLoading){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        val inflater = menuInflater

        inflater.inflate(R.menu.option_menu_detail, menu)

        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.setting_menu_detail -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }

}