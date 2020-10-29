package com.sosa.ignacio.codechallenge.seriesify.ui.detail

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
import com.sosa.ignacio.codechallenge.seriesify.common.utils.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailViewModel : ViewModel() {

    private val mediaRepository : MediaRepository = DefaultMediaRepository
    private val configurationRepository : ConfigurationRepository = DefaultConfigurationRepository

    private val _currentMedia = MutableLiveData<Media>()

    private val _mediaHelper = MutableLiveData<MediaHelper>()

    private var _subscription = MutableLiveData<Boolean>()
    val subscription: LiveData<Boolean>
        get() = _subscription

    val mediaDetails = combine(_currentMedia,_mediaHelper)

    init {
        viewModelScope.launch {
            initConfiguration()
            _currentMedia.value = mediaRepository.getSelectedMedia()
        }
    }

    private suspend fun initConfiguration() {
        withContext(viewModelScope.coroutineContext) {
            configurationRepository.getMediaHelper({ mediaHelper -> onSuccess(mediaHelper)}, {onFailure()})
        }
    }

    private fun onSuccess(mediaHelper: MediaHelper) {
        _mediaHelper.value = mediaHelper
    }

    //TODO: show error message
    private fun onFailure() {}

    fun onSubscriptionClicked(
        subscriptionManager: SubscriptionManager,
        newState: Boolean
    ) {
        _subscription.value = newState
        _currentMedia.value?.let {
            subscriptionManager.handleSubscription(it)
        }
    }
}