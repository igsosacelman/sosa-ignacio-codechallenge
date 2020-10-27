package com.sosa.ignacio.codechallenge.seriesify.ui.main

import androidx.lifecycle.*
import com.sosa.ignacio.codechallenge.seriesify.common.model.Media
import com.sosa.ignacio.codechallenge.seriesify.common.model.Page
import com.sosa.ignacio.codechallenge.seriesify.common.repositories.media.DefaultMediaRepository
import com.sosa.ignacio.codechallenge.seriesify.common.repositories.media.MediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel() : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _mediaList = MutableLiveData<List<Media>>()
    val mediaList: LiveData<List<Media>>
        get() = _mediaList

    private val mediaRepository : MediaRepository = DefaultMediaRepository()

    init {
        retrieveMedia()
    }

    private fun onSuccess(page: Page<Media>) {
        _mediaList.value = page.items
    }

    //TODO: show error message
    private fun onFailure() {}

    private fun retrieveMedia() {
        viewModelScope.launch {
            _loading.value = true
            withContext(Dispatchers.IO) {
                mediaRepository.getPopular({ page -> onSuccess(page)}, {onFailure()})
            }
            _loading.value = false
        }
    }

    //TODO: onClickListener opens item details
    fun onMediaItemClicked(item: Media) {}
}