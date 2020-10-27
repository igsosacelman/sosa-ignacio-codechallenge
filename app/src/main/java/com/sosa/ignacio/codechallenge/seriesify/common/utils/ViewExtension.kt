package com.sosa.ignacio.codechallenge.seriesify.common.utils

import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


/**
 * Toggles [View] visibility, according to [present], between [View.VISIBLE] and [View.GONE].
 */
fun View.togglePresence(present: Boolean) {
    visibility = if (present) View.VISIBLE else View.GONE
}

/**
 * Load [ImageView] with an image from [source].
 */
fun ImageView.loadFromUrl(context: Context, source: String) = apply {
    Glide.with(context)
        .load(source)
        .into(this)
}

/**
 * Load [ImageView] with an image from [source] and with [requestOption] as configuration
 */
fun ImageView.loadFromUrl(context: Context, source: String, requestOption: RequestOptions) = apply {
    Glide.with(context)
        .load(source)
        .apply(requestOption)
        .into(this)
}

/**
 * Filter [ImageView] colors to a grayscale
 */
fun ImageView.toGrayScale() = apply{
    val colorMatrix = ColorMatrix()
    colorMatrix.setSaturation(0f)
    val filter = ColorMatrixColorFilter(colorMatrix)
    this.colorFilter = filter
}