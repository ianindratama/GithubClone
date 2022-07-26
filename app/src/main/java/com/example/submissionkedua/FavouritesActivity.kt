package com.example.submissionkedua

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionkedua.adapter.FavouritesAdapter
import com.example.submissionkedua.databinding.ActivityFavouritesBinding
import com.example.submissionkedua.util.SettingsActivity
import com.example.submissionkedua.util.SettingsPreferences
import com.example.submissionkedua.viewmodel.FavouritesViewModel
import com.example.submissionkedua.viewmodel.FavouritesViewModelFactory

class FavouritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavouritesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "My Favourite Users"

        val pref = SettingsPreferences.getInstance(dataStore)
        val favouritesViewModel: FavouritesViewModel by viewModels { FavouritesViewModelFactory(pref, this) }

        favouritesViewModel.getThemeSettings().observe(this)
        {   isDarkModeActive: Boolean ->
            if(isDarkModeActive){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val favouritesAdapter = FavouritesAdapter{ username ->
            favouritesViewModel.deleteFromFavourites(username)
        }

        favouritesViewModel.getFavouriteUsers().observe(this){ favouritesUser ->
            if (favouritesUser.isEmpty()){

                binding.rvFavouriteUsers.visibility = View.GONE
                binding.FavouritesActivityNoData.visibility = View.VISIBLE

            }else{

                binding.rvFavouriteUsers.visibility = View.VISIBLE

                favouritesAdapter.submitList(favouritesUser)

                binding.FavouritesActivityNoData.visibility = View.GONE
            }
        }

        favouritesViewModel.favouritesNotification.observe(this){


            it.getContentIfNotHandled().let { toastText ->
                if (toastText != null) {
                    Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
                }
            }

        }

        binding.rvFavouriteUsers.apply {

            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = favouritesAdapter

            favouritesAdapter.setOnItemClickCallback(object: FavouritesAdapter.OnItemClickCallback{
                override fun onItemClicked(username: String) {

                    val intent = Intent(this@FavouritesActivity, DetailActivity::class.java)
                    intent.putExtra("fromFavouritesActivity", username)
                    startActivity(intent)

                }
            })

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