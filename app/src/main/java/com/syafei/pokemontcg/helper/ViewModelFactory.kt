package com.syafei.pokemontcg.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.syafei.pokemontcg.di.AppInjection
import com.syafei.pokemontcg.repository.PokemonRepository
import com.syafei.pokemontcg.ui.details.DetailsViewModel
import com.syafei.pokemontcg.ui.main.MainViewModel

class ViewModelFactory (
    private val pokemonRepository: PokemonRepository): ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (instance == null) {
                synchronized(ViewModelFactory::class.java) {
                    instance = ViewModelFactory(
                        AppInjection.provideRepository(context)
                    )
                }
            }

            return instance as ViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(pokemonRepository) as T
            modelClass.isAssignableFrom(DetailsViewModel::class.java) -> DetailsViewModel(pokemonRepository) as T
            else -> throw IllegalArgumentException("Unrecognized ViewModel class: " + modelClass.name)
        }
    }
}