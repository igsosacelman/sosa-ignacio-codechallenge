package com.sosa.ignacio.codechallenge.seriesify.repositories

import com.sosa.ignacio.codechallenge.seriesify.common.model.Page
import com.sosa.ignacio.codechallenge.seriesify.common.model.Serie

interface SeriesRepository {

    fun getPopular(onSuccess: (Page<Serie>) -> Unit?, onFailure: () -> Unit)
}