package com.sosa.ignacio.codechallenge.seriesify.common.model

import com.google.gson.annotations.SerializedName

class Page<T>(
    @SerializedName("results") private val results: List<T>,
    @SerializedName("page") val page: Int,
    @SerializedName("total_results") val totalResults: Int,
    @SerializedName("total_pages") val totalPages: Int
) {

    val items get() = results
}