package com.example.submissionkedua

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionkedua.adapter.SearchDataAdapter
import com.example.submissionkedua.data.remote.ItemsItem
import com.example.submissionkedua.databinding.FragmentSearchBinding
import com.example.submissionkedua.util.SettingsActivity
import com.example.submissionkedua.util.SettingsPreferences
import com.example.submissionkedua.viewmodel.SearchViewModel
import com.example.submissionkedua.viewmodel.SearchViewModelFactory

class SearchFragment: Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var searchUser: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)
        binding.rvSearchList.setHasFixedSize(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchUser = SearchFragmentArgs.fromBundle(arguments as Bundle).username

        val pref = SettingsPreferences.getInstance(context?.dataStore as DataStore<Preferences>)
        val searchViewModel: SearchViewModel by viewModels { SearchViewModelFactory(pref, searchUser.toString()) }

        searchViewModel.getThemeSettings().observe(requireActivity())
        {   isDarkModeActive: Boolean ->
            if(isDarkModeActive){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val layoutManager = LinearLayoutManager(activity)
        binding.rvSearchList.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(activity, layoutManager.orientation)
        binding.rvSearchList.addItemDecoration(itemDecoration)

        searchViewModel.itemsItem.observe(requireActivity()){
            setSearchResultData(it)
        }

        searchViewModel.isLoading.observe(requireActivity()){
            showLoading(it)
        }

        searchViewModel.errorMessage.observe(requireActivity()){

            it?.getContentIfNotHandled().let { toastText ->
                if (toastText != null) {
                    Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
                }
            }

        }

    }


    private fun setSearchResultData(items: List<ItemsItem>) {

        if (items.isEmpty()){

            binding.rvSearchList.visibility = View.GONE

            binding.SearchFragmentNoDataImage.visibility = View.VISIBLE
            binding.SearchFragmentNoDataTitle.visibility = View.VISIBLE
            binding.SearchFragmentNoDataSubtitle.visibility = View.VISIBLE
        }else{

            binding.SearchFragmentNoDataImage.visibility = View.GONE
            binding.SearchFragmentNoDataTitle.visibility = View.GONE
            binding.SearchFragmentNoDataSubtitle.visibility = View.GONE

            binding.rvSearchList.visibility = View.VISIBLE

            val adapter = SearchDataAdapter(items)
            binding.rvSearchList.adapter = adapter

            adapter.setOnItemClickCallback(object: SearchDataAdapter.OnItemClickCallback{
                override fun onItemClicked(username: String) {
                    val toDetailActivity = SearchFragmentDirections.actionSearchFragmentToDetailActivity()
                    toDetailActivity.username = username

                    binding.root.findNavController().navigate(toDetailActivity)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))

        if (searchUser != null){
            searchView.onActionViewExpanded()
            searchView.setQuery(searchUser, false)
            searchView.clearFocus()
        }else{
            searchView.queryHint = resources.getString(R.string.search_hint)
        }

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                searchView.clearFocus()

                val toSearchFragment = SearchFragmentDirections.actionSearchFragmentSelf()
                toSearchFragment.username = query.toString()

                binding.root.findNavController().navigate(toSearchFragment)

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favorite_menu -> {
                val intent = Intent(context, FavouritesActivity::class.java)
                startActivity(intent)
            }
            R.id.setting_menu -> {
                val intent = Intent(context, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}