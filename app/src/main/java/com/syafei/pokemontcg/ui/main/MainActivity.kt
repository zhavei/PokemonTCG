package com.syafei.pokemontcg.ui.main

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.syafei.pokemontcg.databinding.ActivityMainBinding
import com.syafei.pokemontcg.helper.ViewModelFactory
import com.syafei.pokemontcg.ui.adapters.LoadingStateAdapter
import com.syafei.pokemontcg.ui.adapters.MainAdapter
import com.syafei.pokemontcg.ui.details.DetailsActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    private lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupAdapter()
        onSwipeRefresh()
        getSearchData()
        //searchPokemon()
        searchUser()


    }

    private fun setupAdapter() {
        mainAdapter = MainAdapter().apply {
            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    super.onItemRangeInserted(positionStart, itemCount)
                    if (positionStart == 0) {
                        binding.rvPokemonMain.smoothScrollToPosition(0)
                    }
                }
            })
        }


        binding.apply {
            rvPokemonMain.layoutManager = GridLayoutManager(this@MainActivity, 2)
            rvPokemonMain.setHasFixedSize(true)
            rvPokemonMain.adapter = mainAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    mainAdapter.retry()
                }
            )
        }

        mainAdapter.onItemClick = { pokemonData ->
            Intent(this, DetailsActivity::class.java).apply {
                putExtra(DetailsActivity.POKEMON_EXTRA, pokemonData)
                startActivity(this)
            }
        }


        mainAdapter.addLoadStateListener { loadState ->
            // set swipe state
            onFetchLoading(loadState)

            // set error state
            lifecycleScope.launch {
                mainViewModel.emptyStatePoke().observe(
                    this@MainActivity
                ) { isEmpty ->
                    Log.d("main activity", "initAdapter: $isEmpty")
                    if (isEmpty) {
                        onFetchError(loadState)
                    }
                }
            }

            // set empty state
            onFetchEmpty(loadState)
        }

    }

    private fun onSwipeRefresh() {
        binding.swipeLayout.setOnRefreshListener {

            mainViewModel.clearPokemonData()

            //binding.searchEdittext.setText("")

            // get data while refreshing
            getSearchData("")
        }
    }

    /*private fun searchPokemon() {
        binding.searchButton.setOnClickListener {

            mainViewModel.clearPokemonData()

            val query = binding.searchEdittext.text.trim().toString()
            getSearchData(query)
        }
    }*/

    //region Search User
    private fun searchUser() {
        val countrySearch = binding.svMain
        val searchIcon =
            countrySearch.findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon)
        searchIcon.setColorFilter(Color.BLACK)
        val cancelIcon =
            countrySearch.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        cancelIcon.setColorFilter(Color.BLACK)
        val textViewSearch =
            countrySearch.findViewById<TextView>(androidx.appcompat.R.id.search_src_text)
        textViewSearch.setTextColor(Color.BLACK)

        countrySearch.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String): Boolean {
                mainViewModel.clearPokemonData()
                getSearchData(newText)
                return false
            }
        })

    }

    private fun getSearchData(query: String = "") {
        mainViewModel.getPokemonList(query).observe(this) {
            it?.let {
                mainAdapter.submitData(lifecycle, it)
                Log.d("main activity", "getData: ${mainAdapter.itemCount}")
            }
        }
    }

    private fun onFetchLoading(loadState: CombinedLoadStates) {
        if (loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading) {
            binding.swipeLayout.isRefreshing = true
            binding.cvError.visibility = View.GONE
        } else {
            binding.rvPokemonMain.visibility = View.VISIBLE
            binding.swipeLayout.isRefreshing = false

        }
    }

    private fun onFetchError(loadState: CombinedLoadStates) {
        Log.d("main activity", "onFetchError: Should be error")
        binding.cvError.visibility = View.VISIBLE
        binding.rvPokemonMain.visibility = View.GONE
        onFetchEmpty(loadState)
    }

    private fun onFetchEmpty(loadState: CombinedLoadStates) {
        if (loadState.append.endOfPaginationReached) {
            if (mainAdapter.itemCount < 1) {
                Log.d("main activity", "onFetchEmpty: Should be empty")
                binding.cvError.visibility = View.VISIBLE
            }
        }
    }

}