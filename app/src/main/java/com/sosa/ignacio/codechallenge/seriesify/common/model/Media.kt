package com.sosa.ignacio.codechallenge.seriesify.common.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Media (
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("genre_ids") val genreIds: List<Int>,
    @SerializedName("backdrop_path") val backdropPath: String,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("first_air_date") val dateRelease: String
) : Serializable {

    val year: String
        get() = dateRelease.split(SEPARATOR).first()

    companion object {
        private const val SEPARATOR = "-"
    }
}
