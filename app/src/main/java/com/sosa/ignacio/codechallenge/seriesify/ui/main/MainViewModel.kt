package com.sosa.ignacio.codechallenge.seriesify.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sosa.ignacio.codechallenge.seriesify.common.model.Page
import com.sosa.ignacio.codechallenge.seriesify.common.model.Serie
import com.sosa.ignacio.codechallenge.seriesify.repositories.DefaultSeriesRepository
import com.sosa.ignacio.codechallenge.seriesify.repositories.SeriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val repository : SeriesRepository = DefaultSeriesRepository()

    private fun onSuccess(page: Page<Serie>) {
        _message.value = "Salio todo bien"
    }

    private fun onFailure() {
        _message.value = "Salio todo mal"
    }

    fun onButtonClicked() {
        viewModelScope.launch {
            _loading.value = true
            withContext(Dispatchers.IO) {
                repository.getPopular({page -> onSuccess(page)}, {onFailure()})
            }
            _loading.value = false
        }
    }
}