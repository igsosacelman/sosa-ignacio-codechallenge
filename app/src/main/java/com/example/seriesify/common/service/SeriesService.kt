package com.example.seriesify.common.service

import com.example.seriesify.common.model.Page
import com.example.seriesify.common.model.Serie
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SeriesService {

    @GET("/$SERVER/tv/popular")
    fun getPopular(@Query("api_key") apiKey: String) : Call<Page<Serie>>

    companion object {
        private const val SERVER = 3
    }
}