package com.sosa.ignacio.codechallenge.seriesify.common.model

import com.google.gson.annotations.SerializedName

data class Media (
    val id: Int,
    val name: String,
    val overview: String,
    @SerializedName("genre_ids") val genreIds: List<Int>,
    @SerializedName("backdrop_path") val backdropPath: String,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("first_air_date") val dateRelease: String
) {

    val year: String
        get() = dateRelease.split(SEPARATOR).first()

    companion object {
        private const val SEPARATOR = "-"
    }
}
