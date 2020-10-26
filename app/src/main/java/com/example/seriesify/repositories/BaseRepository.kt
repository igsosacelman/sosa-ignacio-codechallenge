package com.example.seriesify.repositories

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

abstract class BaseRepository {

    protected val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    companion object{
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val OK_HTTP = 200
    }
}