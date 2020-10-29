package com.sosa.ignacio.codechallenge.seriesify.common.model

data class MediaHelper(
    private val configuration: Configuration,
    private val genres: List<Genre>
) {

    fun fullBackdropPathUrlFrom(path: String?, sizeIndex: Int) = path?.let {
        configuration.images.secureBaseUrl + configuration.images.backdropSizes[sizeIndex] + path
    }

    fun fullPosterPathUrlFrom(path: String?, sizeIndex: Int) = path?.let {
        configuration.images.secureBaseUrl + configuration.images.posterSizes[sizeIndex] + path
    }

    fun getGenreById(id: Int) = genres.find { it.id == id }
}