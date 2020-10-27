package com.sosa.ignacio.codechallenge.seriesify.common.repositories.configuration

import com.sosa.ignacio.codechallenge.seriesify.common.model.Configuration
import com.sosa.ignacio.codechallenge.seriesify.common.repositories.BaseRepository
import com.sosa.ignacio.codechallenge.seriesify.common.service.ConfigurationService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object DefaultConfigurationRepository : BaseRepository(), ConfigurationRepository {

    private var configuration: Configuration? = null

    val currentConfiguration
        get() = configuration

    private val service = retrofit.create(ConfigurationService::class.java)

    override fun getConfiguration(onSuccess: () -> Unit?, onFailure: () -> Unit) {
        configuration?.let{
            onSuccess.invoke()
        } ?: run {
            service
                .getConfiguration(apiKey)
                .enqueue(object: Callback<Configuration> {
                    override fun onResponse(call: Call<Configuration>, response: Response<Configuration>) {
                        if (response.body() != null && response.code() == OK_HTTP){
                            configuration = response.body()
                            onSuccess.invoke()
                        } else {
                            onFailure.invoke()
                        }
                    }

                    override fun onFailure(call: Call<Configuration>, t: Throwable) {
                        onFailure.invoke()
                    }
                })
        }
    }
}
