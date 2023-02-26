package com.syafei.pokemontcg.coredata.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.syafei.pokemontcg.coredata.model.PokemonData
import com.syafei.pokemontcg.helper.DataConverters


@Database(
    entities = [PokemonData::class, RemoteKeys::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(DataConverters::class)
abstract class PokemonDatabase : RoomDatabase() {

    abstract fun pokemonDao(): PokemonDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: PokemonDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): PokemonDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    PokemonDatabase::class.java, "pokemon_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }

}