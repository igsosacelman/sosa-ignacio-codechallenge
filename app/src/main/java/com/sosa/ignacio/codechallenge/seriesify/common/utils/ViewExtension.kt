package com.sosa.ignacio.codechallenge.seriesify.common.utils

import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

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
 * Load [ImageView] with an image from [source] and with [cornerRadius] as part of the configuration
 */
fun ImageView.loadFromUrlWithRoundedCorners(context: Context, source: String, cornerRadius: Int) = apply {
    Glide.with(context)
        .load(source)
        .transform(FitCenter(), RoundedCorners(20))
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