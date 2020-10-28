package com.sosa.ignacio.codechallenge.seriesify.common.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <A, B> combine(a: LiveData<A>, b: LiveData<B>): LiveData<Pair<A, B>> {
    return MediatorLiveData<Pair<A, B>>().apply {
        fun combine() {
            val aValue = a.value
            val bValue = b.value
            if (aValue != null && bValue != null) {
                postValue(Pair(aValue, bValue))
            }
        }

        addSource(a) { combine() }
        addSource(b) { combine() }

        combine()
    }
}

fun combineBooleans(a: LiveData<Boolean>, b: LiveData<Boolean>): LiveData<Boolean> {
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