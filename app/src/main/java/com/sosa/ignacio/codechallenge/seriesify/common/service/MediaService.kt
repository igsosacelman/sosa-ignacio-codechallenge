package com.sosa.ignacio.codechallenge.seriesify.common.service

import com.sosa.ignacio.codechallenge.seriesify.common.model.Media
import com.sosa.ignacio.codechallenge.seriesify.common.model.Page
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MediaService {

    @GET("/$SERVER/$MEDIA_TV/popular")
    fun getPopular(@Query("api_key") apiKey: String, @Query("page") page: String) : Call<Page<Media>>

    companion object {
        private const val SERVER = 3
        private const val MEDIA_TV = "tv"
        private const val MEDIA_MOVIE = "movie"
    }
}