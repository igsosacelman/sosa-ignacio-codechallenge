package com.sosa.ignacio.codechallenge.seriesify.common.repositories.media

import com.sosa.ignacio.codechallenge.seriesify.common.model.Media
import com.sosa.ignacio.codechallenge.seriesify.common.model.Page
import com.sosa.ignacio.codechallenge.seriesify.common.repositories.BaseRepository
import com.sosa.ignacio.codechallenge.seriesify.common.service.MediaService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DefaultMediaRepository : BaseRepository(), MediaRepository {

    private val service = retrofit.create(MediaService::class.java)

    override fun getPopular(onSuccess: (Page<Media>) -> Unit?, onFailure: () -> Unit) {
        service
            .getPopular(apiKey)
            .enqueue(object: Callback<Page<Media>>{

            override fun onResponse(call: Call<Page<Media>>, response: Response<Page<Media>>) {
                if (response.body() != null && response.code() == OK_HTTP){
                    onSuccess.invoke(response.body() as Page<Media>)
                } else {
                    onFailure.invoke()
                }
            }

            override fun onFailure(call: Call<Page<Media>>, t: Throwable) {
                onFailure.invoke()
            }
        })
    }

    override fun getDiscover(onSuccess: (Page<Media>) -> Unit?, onFailure: () -> Unit) {}
}