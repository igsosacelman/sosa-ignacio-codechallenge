package com.sosa.ignacio.codechallenge.seriesify.common.service

import com.sosa.ignacio.codechallenge.seriesify.common.model.Configuration
import com.sosa.ignacio.codechallenge.seriesify.common.model.GenreResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ConfigurationService {

    @GET("/$SERVER/configuration")
    fun getConfiguration(@Query("api_key") apiKey: String): Call<Configuration>

    @GET("/$SERVER/genre/$MEDIA_TV/list")
    fun getGenres(@Query("api_key") apiKey: String): Call<GenreResponse>

    companion object {
        private const val SERVER = 3
        private const val MEDIA_TV = "tv"
        private const val MEDIA_MOVIE = "movie"
    }
}