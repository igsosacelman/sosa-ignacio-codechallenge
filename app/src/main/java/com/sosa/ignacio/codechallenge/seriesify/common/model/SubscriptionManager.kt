package com.sosa.ignacio.codechallenge.seriesify.common.model

class SubscriptionManager() {

    private var subscriptions: MutableList<Media>? = null

    fun handleSubscription(media: Media) {
        subscriptions?.let {
            if(it.contains(media)) {
                unsubscribe(media)
            } else {
                subscribe(media)
            }
        } ?: run {
            createNewList(media)
        }
    }

    private fun createNewList(media: Media) {
        subscriptions = listOf(media).toMutableList()
    }

    private fun subscribe(media: Media) {
        subscriptions!!.add(media)
    }

    private fun unsubscribe(media: Media) {
        subscriptions!!.remove(media)
    }

    fun getAllSubscriptions() : List<Media>? = subscriptions
}