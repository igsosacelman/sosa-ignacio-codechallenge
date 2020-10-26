package com.sosa.ignacio.codechallenge.seriesify.repositories

import com.sosa.ignacio.codechallenge.seriesify.common.model.Page
import com.sosa.ignacio.codechallenge.seriesify.common.model.Serie
import com.sosa.ignacio.codechallenge.seriesify.common.service.SeriesService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DefaultSeriesRepository : BaseRepository(), SeriesRepository {

    override fun getPopular(onSuccess: (Page<Serie>) -> Unit?, onFailure: () -> Unit) {
        val service = retrofit.create(SeriesService::class.java)
        val call = service.getPopular(API_KEY)

        call.enqueue(object: Callback<Page<Serie>>{

            override fun onResponse(call: Call<Page<Serie>>, response: Response<Page<Serie>>) {
                if (response.body() != null && response.code() == OK_HTTP){
                    onSuccess.invoke(response.body() as Page<Serie>)
                } else {
                    onFailure.invoke()
                }
            }

            override fun onFailure(call: Call<Page<Serie>>, t: Throwable) {
                onFailure.invoke()
            }
        })
    }

    companion object {
        private const val API_KEY = "208ca80d1e219453796a7f9792d16776"
    }
}