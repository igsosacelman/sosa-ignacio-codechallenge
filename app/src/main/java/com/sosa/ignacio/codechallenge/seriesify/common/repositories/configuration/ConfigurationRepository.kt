package com.sosa.ignacio.codechallenge.seriesify.common.repositories.configuration

import com.sosa.ignacio.codechallenge.seriesify.common.model.Configuration

interface ConfigurationRepository {

    fun getConfiguration(onSuccess: () -> Unit?, onFailure: () -> Unit)
}