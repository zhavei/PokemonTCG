package com.syafei.pokemontcg.coredata.local

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.syafei.pokemontcg.coredata.model.PokemonData

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun storePokemon(quote: List<PokemonData>)

    @Query("SELECT * FROM pokemon")
    fun getData(): PagingSource<Int, PokemonData>

    @Query("SELECT (SELECT COUNT(*) FROM pokemon) == 0")
    fun emptyStatePokemon(): LiveData<Boolean>

    @Query("DELETE FROM pokemon")
    suspend fun deleteAll()

}