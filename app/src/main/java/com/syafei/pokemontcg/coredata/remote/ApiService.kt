package com.syafei.pokemontcg.coredata.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("cards")
    suspend fun getPokemonData(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("pageSize") size: Int
    ): DataResponse
}