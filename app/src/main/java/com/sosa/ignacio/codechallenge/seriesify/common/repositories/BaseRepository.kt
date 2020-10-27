package com.sosa.ignacio.codechallenge.seriesify.common.repositories

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

abstract class BaseRepository {

    protected val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    protected val apiKey
        get() = API_KEY

    companion object{
        private const val API_KEY = "208ca80d1e219453796a7f9792d16776"
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val OK_HTTP = 200
    }
}