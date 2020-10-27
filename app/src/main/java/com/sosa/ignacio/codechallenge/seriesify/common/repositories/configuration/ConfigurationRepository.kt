package com.sosa.ignacio.codechallenge.seriesify.common.repositories.configuration

interface ConfigurationRepository {

    fun getConfiguration(onSuccess: () -> Unit?, onFailure: () -> Unit)

    fun getGenres(onSuccess: () -> Unit?, onFailure: () -> Unit)
}