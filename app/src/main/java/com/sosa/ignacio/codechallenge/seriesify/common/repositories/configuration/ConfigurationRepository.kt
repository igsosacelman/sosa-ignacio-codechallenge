package com.sosa.ignacio.codechallenge.seriesify.common.repositories.configuration

import com.sosa.ignacio.codechallenge.seriesify.common.model.MediaHelper

interface ConfigurationRepository {

    fun getConfiguration(onSuccess: () -> Unit?, onFailure: () -> Unit)

    fun getGenres(onSuccess: () -> Unit?, onFailure: () -> Unit)

    fun getMediaHelper(onSuccess: (MediaHelper) -> Unit?, onFailure: () -> Unit)
}