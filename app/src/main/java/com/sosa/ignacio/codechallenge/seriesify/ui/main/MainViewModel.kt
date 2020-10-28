package com.sosa.ignacio.codechallenge.seriesify.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sosa.ignacio.codechallenge.seriesify.common.model.Media
import com.sosa.ignacio.codechallenge.seriesify.common.model.MediaHelper
import com.sosa.ignacio.codechallenge.seriesify.common.model.Page
import com.sosa.ignacio.codechallenge.seriesify.common.repositories.configuration.ConfigurationRepository
import com.sosa.ignacio.codechallenge.seriesify.common.repositories.configuration.DefaultConfigurationRepository
import com.sosa.ignacio.codechallenge.seriesify.common.repositories.media.DefaultMediaRepository
import com.sosa.ignacio.codechallenge.seriesify.common.repositories.media.MediaRepository
import com.sosa.ignacio.codechallenge.seriesify.common.utils.combineBooleans
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

    private val _itemSelected = MutableLiveData<Media>()
    val itemSelected: LiveData<Media>
        get() = _itemSelected

    private val genresReady = MutableLiveData<Boolean>()
    private val configurationReady = MutableLiveData<Boolean>()

    val initConfigurationReady = combineBooleans(genresReady,configurationReady)

    private val mediaRepository : MediaRepository = DefaultMediaRepository
    private val configurationRepository : ConfigurationRepository = DefaultConfigurationRepository

    init {
        viewModelScope.launch {
            _loading.value = true
            initConfiguration()
            _loading.value = false
        }
    }

    private suspend fun initConfiguration() {
        withContext(viewModelScope.coroutineContext) {
            configurationRepository.getConfiguration({ onConfigurationReady() }, { onFailure() })
            configurationRepository.getGenres({ onGenresReady() }, { onFailure() })
        }
    }

    fun onConfigReady() {
        viewModelScope.launch {
            createMediaHelper()
        }
    }

    private suspend fun createMediaHelper() {
        withContext(viewModelScope.coroutineContext) {
            configurationRepository.getMediaHelper({ mediaHelper -> onSuccess(mediaHelper)}, {onFailure()})
        }
    }

    private suspend fun retrieveMedia() {
        withContext(viewModelScope.coroutineContext) {
            mediaRepository.getPopular({ page -> onSuccess(page)}, {onFailure()})
        }
    }

    private fun onGenresReady() {
        genresReady.value = true
    }

    private fun onConfigurationReady() {
        configurationReady.value = true
    }

    private fun onSuccess(mediaHelper: MediaHelper) {
        _mediaHelper.value = mediaHelper
        viewModelScope.launch {
            retrieveMedia()
        }
    }

    private fun onSuccess(page: Page<Media>) {
        _mediaList.value = page.items
    }

    //TODO: show error message
    private fun onFailure() {}

    //TODO: onClickListener opens item details
    fun onMediaItemClicked(item: Media) {
        viewModelScope.launch {
            mediaRepository.saveSelectedMedia(item)
            _itemSelected.value = item
        }
    }


}