package com.sosa.ignacio.codechallenge.seriesify.common.model

class Page<T>(
    private val results: List<T>,
    val page: Int,
    val totalResults: Int,
    val totalPages: Int
) {

    val items get() = results
}