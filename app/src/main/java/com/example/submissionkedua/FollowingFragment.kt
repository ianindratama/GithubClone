package com.example.submissionkedua

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.fragment.app.viewModels

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionkedua.adapter.SearchDataAdapter
import com.example.submissionkedua.data.remote.ItemsItem
import com.example.submissionkedua.databinding.FragmentFollowingBinding
import com.example.submissionkedua.util.SettingsPreferences
import com.example.submissionkedua.viewmodel.SocialViewModel
import com.example.submissionkedua.viewmodel.SocialViewModelFactory

class FollowingFragment : Fragment() {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)

        binding.rvFollowingList.setHasFixedSize(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString("username").toString()

        val pref = SettingsPreferences.getInstance(context?.dataStore as DataStore<Preferences>)
        val socialViewModel: SocialViewModel by viewModels { SocialViewModelFactory(pref, username, false) }

        socialViewModel.getThemeSettings().observe(requireActivity())
        {   isDarkModeActive: Boolean ->
            if(isDarkModeActive){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val layoutManager = LinearLayoutManager(activity)
        binding.rvFollowingList.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(activity, layoutManager.orientation)
        binding.rvFollowingList.addItemDecoration(itemDecoration)

        socialViewModel.itemsItem.observe(requireActivity()){
            setFollowingResultData(username, it)
        }

        socialViewModel.isLoading.observe(requireActivity()){
            showLoading(it)
        }

        socialViewModel.errorMessage.observe(requireActivity()){

            if (it != null){
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun setFollowingResultData(user: String, items: List<ItemsItem>) {

        if (items.isEmpty()){

            binding.rvFollowingList.visibility = View.GONE

            val text = "$user has 0 following"
            binding.FollowingFragmentNoDataTitle.text = text
            binding.FollowingFragmentNoDataTitle.visibility = View.VISIBLE

        } else {

            binding.FollowingFragmentNoDataTitle.visibility = View.GONE

            binding.rvFollowingList.visibility = View.VISIBLE

            val adapter = SearchDataAdapter(items)
            binding.rvFollowingList.adapter = adapter

            adapter.setOnItemClickCallback(object: SearchDataAdapter.OnItemClickCallback{
                override fun onItemClicked(username: String) {
                    val text = "$user is following $username"
                    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                }
            })

        }

    }

    private fun showLoading(isLoading: Boolean) {
        if(isLoading){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}