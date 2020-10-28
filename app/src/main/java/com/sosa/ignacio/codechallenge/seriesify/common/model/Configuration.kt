package com.sosa.ignacio.codechallenge.seriesify.common.model

import com.google.gson.annotations.SerializedName

data class Configuration(
    val images: ImageConfiguration
)

data class ImageConfiguration(
    @SerializedName("base_url") val baseUrl: String,
    @SerializedName("secure_base_url") val secureBaseUrl: String,
    @SerializedName("backdrop_sizes") val backdropSizes: List<String>,
    @SerializedName("poster_sizes") val posterSizes: List<String>
) {

    companion object {
        val DEFAULT_POSTER_SIZE_INDEX = PosterSizes.W500.ordinal
        val DEFAULT_BACKDROP_SIZE_INDEX = BackdropSizes.W300.ordinal
        enum class BackdropSizes { W300, W780, W1280, ORIGINAL }
        enum class PosterSizes { W92, W154, W185, W342, W500, W780, ORIGINAL }
    }
}