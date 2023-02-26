package com.syafei.pokemontcg.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.syafei.pokemontcg.coredata.model.PokemonData
import com.syafei.pokemontcg.repository.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainViewModel(private val pokemonRepository: PokemonRepository) : ViewModel() {

    fun getPokemonList(name: String): LiveData<PagingData<PokemonData>> =
        pokemonRepository.getListPokemon(name).cachedIn(viewModelScope)

    fun clearPokemonData() {
        viewModelScope.launch(Dispatchers.IO) {
            pokemonRepository.clearListPokemon()
        }
    }

    suspend fun emptyStatePoke(): LiveData<Boolean> {
        val emptyState = viewModelScope.async(Dispatchers.IO) {
            pokemonRepository.emptyStatePokemonData()
        }

        return emptyState.await()
    }

}