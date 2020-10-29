package com.sosa.ignacio.codechallenge.seriesify.common.model

import android.content.Context
import com.google.gson.Gson
import com.sosa.ignacio.codechallenge.seriesify.common.utils.MediaSharedPreferences

class SubscriptionManager(context: Context) {

    private val subscriptionHelper = SubscriptionHelper(context)

    private var _subscriptions: MutableList<Media>? = null
    val subscriptions : List<Media>?
        get() = _subscriptions

    init {
        subscriptionHelper.getSavedSubscriptions()?.let {
            _subscriptions = if(it.isNotEmpty()) {
                it.toMutableList()
            } else {
                emptyList<Media>().toMutableList()
            }
        } ?: run {
            _subscriptions = emptyList<Media>().toMutableList()
        }
    }

    fun handleSubscription(media: Media) {
        _subscriptions?.let {
            if(it.contains(media)) {
                unsubscribe(media)
            } else {
                subscribe(media)
            }
        }
    }

    fun isSubscribed(media: Media) = _subscriptions != null && _subscriptions!!.contains(media)

    private fun subscribe(media: Media) {
        _subscriptions!!.add(media)
        commit()
    }

    private fun unsubscribe(media: Media) {
        _subscriptions!!.remove(media)
        commit()
    }

    private fun commit() {
        _subscriptions?.let {
            subscriptionHelper.saveSubscriptions(it.toList())
        }
    }
}

class SubscriptionHelper(context: Context) {

    private val mediaSharedPreferences = MediaSharedPreferences(context)

    fun saveSubscriptions(subscriptions: List<Media>) {
        val subscriptionsToStringArray = subscriptions.map { Gson().toJson(it) }.toTypedArray()
        saveListAsJsonString(subscriptionsToStringArray)
    }

    fun getSavedSubscriptions() =
        getListFromJsonString()?.map {
            Gson().fromJson(it, Media::class.java)
        }

    private fun saveListAsJsonString(listToString : Array<String>) {
        mediaSharedPreferences.subscribedMedia = Gson().toJson(listToString,Array<String>::class.java)
    }

    private fun getListFromJsonString() : Array<String>? {
        return mediaSharedPreferences.subscribedMedia?.let {
            Gson().fromJson(it, Array<String>::class.java)
        }
    }
}