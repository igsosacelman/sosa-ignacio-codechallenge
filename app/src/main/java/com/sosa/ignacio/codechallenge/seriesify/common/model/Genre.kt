package com.sosa.ignacio.codechallenge.seriesify.common.model

data class Genre (
    val id: Int,
    val name: String
)

data class GenreResponse(
    val genres: List<Genre>
)