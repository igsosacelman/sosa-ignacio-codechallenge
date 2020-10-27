package com.sosa.ignacio.codechallenge.seriesify.common.repositories.media

import com.sosa.ignacio.codechallenge.seriesify.common.model.Media
import com.sosa.ignacio.codechallenge.seriesify.common.model.Page

interface MediaRepository {

    fun getPopular(onSuccess: (Page<Media>) -> Unit?, onFailure: () -> Unit)

    fun getDiscover(onSuccess: (Page<Media>) -> Unit?, onFailure: () -> Unit)
}