package com.syafei.pokemontcg.di

import android.content.Context
import com.syafei.pokemontcg.coredata.local.PokemonDatabase
import com.syafei.pokemontcg.coredata.remote.ApiConfig
import com.syafei.pokemontcg.repository.PokemonRepository

object AppInjection {

    fun provideRepository(context: Context): PokemonRepository {
        val apiService = ApiConfig.getApiService()
        val database = PokemonDatabase.getDatabase(context)
        return PokemonRepository.getInstance(
            apiService,
            database
        )
    }

}