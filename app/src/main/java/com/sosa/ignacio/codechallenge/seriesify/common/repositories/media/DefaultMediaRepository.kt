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

    private var currentMediaList: MutableList<Media>? = null

    private var currentPage = 1

    private var totalPages: Int? = null

    private val service = retrofit.create(MediaService::class.java)

    override fun getPopular(onSuccess: (List<Media>) -> Unit?, onFailure: () -> Unit) {
        service
            .getPopular(apiKey, currentPage.toString())
            .enqueue(object : Callback<Page<Media>> {

                override fun onResponse(call: Call<Page<Media>>, response: Response<Page<Media>>) {
                    if (response.body() != null && response.code() == OK_HTTP) {
                        with(response.body() as Page<Media>) {
                            this@DefaultMediaRepository.totalPages = totalPages
                            currentMediaList?.let {
                                it.addAll(this.items)
                            } ?: run {
                                currentMediaList = this.items.toMutableList()
                            }
                            if(currentPage <= totalPages) {
                                currentPage++
                            }
                            onSuccess.invoke(currentMediaList!!.toList())
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

    override fun getSelectedMedia(): Media? = currentMedia

    override fun saveSelectedMedia(item: Media) {
        currentMedia = item
    }
}