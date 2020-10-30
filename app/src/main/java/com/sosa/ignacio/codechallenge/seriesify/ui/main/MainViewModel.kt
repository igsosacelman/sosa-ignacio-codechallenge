package com.sosa.ignacio.codechallenge.seriesify.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sosa.ignacio.codechallenge.seriesify.common.model.Media
import com.sosa.ignacio.codechallenge.seriesify.common.model.MediaHelper
import com.sosa.ignacio.codechallenge.seriesify.common.model.SubscriptionManager
import com.sosa.ignacio.codechallenge.seriesify.common.repositories.configuration.ConfigurationRepository
import com.sosa.ignacio.codechallenge.seriesify.common.repositories.configuration.DefaultConfigurationRepository
import com.sosa.ignacio.codechallenge.seriesify.common.repositories.media.DefaultMediaRepository
import com.sosa.ignacio.codechallenge.seriesify.common.repositories.media.MediaRepository
import com.sosa.ignacio.codechallenge.seriesify.common.utils.combineBooleans
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainViewModel : ViewModel() {

    private lateinit var subscriptionManager: SubscriptionManager

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

    private val _subscriptions = MutableLiveData<List<Media>>()
    val subscriptions: LiveData<List<Media>>
        get() = _subscriptions

    private val _searchMode = MutableLiveData<Boolean>()
    val searchMode: LiveData<Boolean>
        get() = _searchMode

    private val _filteredSearch = MutableLiveData<List<Media>>()
    val filteredSearch: LiveData<List<Media>>
        get()  = _filteredSearch

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
            configurationRepository.getConfiguration({ onApiConfigurationReady() }, { onFailure() })
            configurationRepository.getGenres({ onGenresReady() }, { onFailure() })
        }
    }

    fun onConfigReady() {
        viewModelScope.launch {
            createMediaHelper()
            getSubscriptions()
        }
    }

    fun getSubscriptions() {
        _subscriptions.value = subscriptionManager.subscriptions
    }

    private suspend fun createMediaHelper() {
        withContext(viewModelScope.coroutineContext) {
            configurationRepository.getMediaHelper({ mediaHelper -> onSuccess(mediaHelper)}, {onFailure()})
        }
    }

    private suspend fun retrieveMedia() {
        withContext(viewModelScope.coroutineContext) {
            mediaRepository.getPopular({ medias -> onSuccess(medias)}, {onFailure()})
        }
    }

    private fun onGenresReady() {
        genresReady.value = true
    }

    private fun onApiConfigurationReady() {
        configurationReady.value = true
    }

    private fun onSuccess(mediaHelper: MediaHelper) {
        _mediaHelper.value = mediaHelper
        viewModelScope.launch {
            retrieveMedia()
        }
    }

    private fun onSuccess(page: List<Media>) {
        _mediaList.value = page
    }

    fun onMediaItemClicked(item: Media) {
        viewModelScope.launch {
            mediaRepository.saveSelectedMedia(item)
            _itemSelected.value = item
        }
    }

    fun onViewCreated(subscriptionManager: SubscriptionManager) {
        this.subscriptionManager = subscriptionManager
        _searchMode.value = false
    }

    fun onSearchIconClicked() {
        if(_searchMode.value != true){
            _searchMode.value = true
        }
    }

    fun onCancelSearchClicked() {
        if(_searchMode.value != false){
            _searchMode.value = false
            commitSubscriptions()
        }
    }

    private fun commitSubscriptions() {
        _subscriptions.value = _subscriptions.value
    }

    fun onQueryTextChange(query: String?) {
        _mediaList.value?.let { media ->
            _filteredSearch.value = if(query.isNullOrEmpty()) {
                media
            } else {
                media.filter { it.name.toLowerCase(Locale.US).contains(query.toLowerCase(Locale.US)) }
            }
        }
    }

    fun onFinishScrolling() {
        viewModelScope.launch {
            retrieveMedia()
        }
    }

    //TODO: show error message
    private fun onFailure() {}
}