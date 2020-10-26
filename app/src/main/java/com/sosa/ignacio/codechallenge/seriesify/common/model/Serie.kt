package com.sosa.ignacio.codechallenge.seriesify.common.model

import com.google.gson.annotations.SerializedName

data class Serie(
    private val id: Int,
    private val name: String,
    private val overview: String,
    @SerializedName("genre_ids") private val genreIds: List<Int>,
    @SerializedName("backdrop_path") private val backdropPath: String,
    @SerializedName("poster_path") private val posterPath: String
)
