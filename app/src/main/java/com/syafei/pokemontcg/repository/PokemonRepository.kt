package com.syafei.pokemontcg.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.syafei.pokemontcg.coredata.local.DataRemoteMediator
import com.syafei.pokemontcg.coredata.local.PokemonDatabase
import com.syafei.pokemontcg.coredata.model.PokemonData
import com.syafei.pokemontcg.coredata.remote.ApiService

class PokemonRepository private constructor(
    private val apiService: ApiService,
    private val pokeDatabase: PokemonDatabase
) {

    fun getListPokemon(name: String): LiveData<PagingData<PokemonData>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 20,
                prefetchDistance = 5
            ),
            remoteMediator = DataRemoteMediator(
                name,
                pokeDatabase,
                apiService
            ),
            pagingSourceFactory = {
                pokeDatabase.pokemonDao().getData()
            }
        ).liveData

    }

    suspend fun clearListPokemon() {
        pokeDatabase.pokemonDao().deleteAll()
        pokeDatabase.remoteKeysDao().deleteRemoteKeys()
    }

    fun emptyStatePokemonData(): LiveData<Boolean> = pokeDatabase.pokemonDao().emptyStatePokemon()


    companion object {
        @Volatile
        private var instance: PokemonRepository? = null

        fun getInstance(
            apiServices: ApiService,
            database: PokemonDatabase
        ): PokemonRepository {
            if (instance == null) {
                synchronized(this) {
                    instance = PokemonRepository(apiServices, database)
                }
            }
            return instance as PokemonRepository
        }
    }
}