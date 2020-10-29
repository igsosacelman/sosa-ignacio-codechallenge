package com.sosa.ignacio.codechallenge.seriesify.common.utils

import android.content.Context
import android.content.SharedPreferences

class MediaSharedPreferences constructor(context: Context) {

    private var sharedPreferences: SharedPreferences

    init {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    var subscribedMedia: String?
        get() = getString(SP_SUBSCRIBED_MEDIA)
        set(value) {
            value?.let {
                setString(SP_SUBSCRIBED_MEDIA, value)
            }
        }

    private fun getString(key: String) = sharedPreferences.getString(key, null)

    private fun setString(key: String, text: String) {
        with(sharedPreferences.edit()) {
            putString(key, text)
            apply()
        }
    }

    companion object {
        private const val PREFERENCES_NAME = "MediaSharedPreferences"
        private const val SP_SUBSCRIBED_MEDIA = "subscribed-media"
    }
}