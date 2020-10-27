package com.sosa.ignacio.codechallenge.seriesify

import androidx.lifecycle.*
import com.sosa.ignacio.codechallenge.seriesify.common.repositories.configuration.DefaultConfigurationRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel : ViewModel() {

    private val genresReady = MutableLiveData<Boolean>()
    private val configurationReady = MutableLiveData<Boolean>()

    val initConfigurationReady = combine(genresReady,configurationReady)

    init {
        viewModelScope.launch {
            initConfiguration()
        }
    }

    private suspend fun initConfiguration() {
        withContext(viewModelScope.coroutineContext) {
            DefaultConfigurationRepository.getConfiguration({ onConfigurationReady() }, { onFailure() })
            DefaultConfigurationRepository.getGenres({ onGenresReady() }, { onFailure() })
        }
    }

    private fun onGenresReady() {
        genresReady.value = true
    }

    private fun onConfigurationReady() {
        configurationReady.value = true
    }

    private fun onFailure() {
        val algo = 1
    }

    private fun combine(a: LiveData<Boolean>, b: LiveData<Boolean>): LiveData<Boolean> {
        return MediatorLiveData<Boolean>().apply {
            fun combine() {
                val aValue = a.value
                val bValue = b.value
                if (aValue != null && bValue != null) {
                    postValue(aValue && bValue)
                }
            }

            addSource(a) { combine() }
            addSource(b) { combine() }

            combine()
        }
    }
}