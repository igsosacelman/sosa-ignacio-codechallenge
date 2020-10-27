package com.sosa.ignacio.codechallenge.seriesify.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sosa.ignacio.codechallenge.seriesify.common.model.Media
import com.sosa.ignacio.codechallenge.seriesify.common.model.Page
import com.sosa.ignacio.codechallenge.seriesify.common.repositories.configuration.DefaultConfigurationRepository
import com.sosa.ignacio.codechallenge.seriesify.common.repositories.media.DefaultMediaRepository
import com.sosa.ignacio.codechallenge.seriesify.common.repositories.media.MediaRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel() : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _mediaList = MutableLiveData<List<Media>>()
    val mediaList: LiveData<List<Media>>
        get() = _mediaList

    private val _mediaHelper = MutableLiveData<MediaHelper>()
    val mediaHelper: LiveData<MediaHelper>
        get() = _mediaHelper

    private val mediaRepository : MediaRepository = DefaultMediaRepository()

    init {
        viewModelScope.launch {
            _loading.value = true
            initConfiguration()
            retrieveMedia()
            _loading.value = false
        }
    }

    private suspend fun initConfiguration() {
        withContext(viewModelScope.coroutineContext) {
            DefaultConfigurationRepository.getMediaHelper({ mediaHelper -> onSuccess(mediaHelper)}, {onFailure()})
        }
    }

    private fun retrieveMedia() {
        viewModelScope.launch {
            _loading.value = true
            mediaRepository.getPopular({ page -> onSuccess(page)}, {onFailure()})
            _loading.value = false
        }
    }

    private fun onSuccess(mediaHelper: MediaHelper) {
        _mediaHelper.value = mediaHelper
    }

    private fun onSuccess(page: Page<Media>) {
        _mediaList.value = page.items
    }

    //TODO: show error message
    private fun onFailure() {}

    //TODO: onClickListener opens item details
    fun onMediaItemClicked(item: Media) {}
}