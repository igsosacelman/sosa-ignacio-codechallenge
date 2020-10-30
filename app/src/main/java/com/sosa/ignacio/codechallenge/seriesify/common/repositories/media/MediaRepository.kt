package com.sosa.ignacio.codechallenge.seriesify.common.repositories.media

import com.sosa.ignacio.codechallenge.seriesify.common.model.Media

interface MediaRepository {

    fun getPopular(onSuccess: (List<Media>) -> Unit?, onFailure: () -> Unit)

    fun getSelectedMedia() : Media?

    fun saveSelectedMedia(item: Media)
}