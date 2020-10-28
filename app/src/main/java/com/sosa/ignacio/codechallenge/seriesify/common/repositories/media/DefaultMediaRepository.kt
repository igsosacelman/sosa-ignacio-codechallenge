package com.sosa.ignacio.codechallenge.seriesify.common.repositories.media

import com.sosa.ignacio.codechallenge.seriesify.common.model.Media
import com.sosa.ignacio.codechallenge.seriesify.common.model.Page
import com.sosa.ignacio.codechallenge.seriesify.common.repositories.BaseRepository
import com.sosa.ignacio.codechallenge.seriesify.common.service.MediaService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object DefaultMediaRepository : BaseRepository(), MediaRepository {

    private var currentMedia: Media? = null

    private var currentMediaList: List<Media>? = null

    private val service = retrofit.create(MediaService::class.java)

    override fun getPopular(onSuccess: (Page<Media>) -> Unit?, onFailure: () -> Unit) {
        service
            .getPopular(apiKey)
            .enqueue(object : Callback<Page<Media>> {

                override fun onResponse(call: Call<Page<Media>>, response: Response<Page<Media>>) {
                    if (response.body() != null && response.code() == OK_HTTP) {
                        with(response.body() as Page<Media>) {
                            currentMediaList = this.items
                            onSuccess.invoke(this)
                        }
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

    override fun getSelectedMedia(): Media? = currentMedia

    override fun saveSelectedMedia(item: Media) {
        currentMedia = item
    }
}