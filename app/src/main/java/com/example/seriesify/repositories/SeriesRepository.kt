package com.example.seriesify.repositories

import com.example.seriesify.common.model.Page
import com.example.seriesify.common.model.Serie

interface SeriesRepository {

    fun getPopular(onSuccess: (Page<Serie>) -> Unit?, onFailure: () -> Unit)
}