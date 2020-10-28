package com.sosa.ignacio.codechallenge.seriesify.common.repositories.configuration

import com.sosa.ignacio.codechallenge.seriesify.common.model.Configuration
import com.sosa.ignacio.codechallenge.seriesify.common.model.Genre
import com.sosa.ignacio.codechallenge.seriesify.common.model.GenreResponse
import com.sosa.ignacio.codechallenge.seriesify.common.model.MediaHelper
import com.sosa.ignacio.codechallenge.seriesify.common.repositories.BaseRepository
import com.sosa.ignacio.codechallenge.seriesify.common.service.ConfigurationService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object DefaultConfigurationRepository : BaseRepository(), ConfigurationRepository {

    private var configuration: Configuration? = null

    private var genres: List<Genre>? = null

    private val mediaHelper: MediaHelper?
        get() = configuration?.let { config ->
            genres?.let { genres ->
                MediaHelper(config, genres)
            }
        }

    private val service = retrofit.create(ConfigurationService::class.java)

    override fun getConfiguration(onSuccess: () -> Unit?, onFailure: () -> Unit) {
        configuration?.let {
            onSuccess.invoke()
        } ?: run {
            service
                .getConfiguration(apiKey)
                .enqueue(object : Callback<Configuration> {
                    override fun onResponse(
                        call: Call<Configuration>,
                        response: Response<Configuration>
                    ) {
                        if (response.body() != null && response.code() == OK_HTTP) {
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

    override fun getGenres(onSuccess: () -> Unit?, onFailure: () -> Unit) {
        genres?.let {
            if (it.isNotEmpty()) {
                onSuccess.invoke()
            }
        } ?: run {
            service
                .getGenres(apiKey)
                .enqueue(object : Callback<GenreResponse> {
                    override fun onResponse(
                        call: Call<GenreResponse>,
                        response: Response<GenreResponse>
                    ) {
                        if (response.body() != null && response.code() == OK_HTTP) {
                            genres = (response.body() as GenreResponse).genres
                            onSuccess.invoke()
                        } else {
                            onFailure.invoke()
                        }
                    }

                    override fun onFailure(call: Call<GenreResponse>, t: Throwable) {
                        onFailure.invoke()
                    }
                })
        }
    }

    override fun getMediaHelper(onSuccess: (MediaHelper) -> Unit?, onFailure: () -> Unit) {
        mediaHelper?.let {
            onSuccess.invoke(it)
        } ?: run(onFailure)
    }
}
