package com.syafei.pokemontcg.coredata.remote

import com.syafei.pokemontcg.coredata.model.PokemonData

data class DataResponse(
    val count: Int,
    val data: List<PokemonData>,
    val page: Int,
    val pageSize: Int,
    val totalCount: Int
)