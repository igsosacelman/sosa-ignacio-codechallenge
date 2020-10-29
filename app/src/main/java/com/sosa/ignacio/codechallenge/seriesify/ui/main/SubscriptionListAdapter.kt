package com.sosa.ignacio.codechallenge.seriesify.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sosa.ignacio.codechallenge.seriesify.R
import com.sosa.ignacio.codechallenge.seriesify.common.model.ImageConfiguration
import com.sosa.ignacio.codechallenge.seriesify.common.model.Media
import com.sosa.ignacio.codechallenge.seriesify.common.model.MediaHelper
import com.sosa.ignacio.codechallenge.seriesify.common.utils.loadFromUrlWithRoundedCorners
import com.sosa.ignacio.codechallenge.seriesify.databinding.ItemSubscriptionListBinding

class SubscriptionListAdapter(private val mediaHelper: MediaHelper, private val onMediaItemClicked: OnMediaItemClicked) : ListAdapter<Media, SubscriptionListViewHolder>(MediaDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriptionListViewHolder {
        val binding = ItemSubscriptionListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubscriptionListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubscriptionListViewHolder, position: Int) {
        if (position == 0) holder.setPadding()
        holder.populate(getItem(position), onMediaItemClicked, mediaHelper)
    }
}

class SubscriptionListViewHolder(binding: ItemSubscriptionListBinding) : RecyclerView.ViewHolder(binding.root) {

    private val poster = binding.poster

    fun setPadding() {
        with(itemView.layoutParams as ViewGroup.MarginLayoutParams) {
            marginStart = itemView.context.resources.getDimensionPixelSize(R.dimen.spacing_large)
        }
    }

    fun populate(
        item: Media?,
        onMediaItemClicked: (Media) -> Unit,
        mediaHelper: MediaHelper
    ) {

        item?.let { media ->

            val fullImageUrl = mediaHelper.fullPosterPathUrlFrom(media.posterPath, ImageConfiguration.DEFAULT_BACKDROP_SIZE_INDEX)
            fullImageUrl?.let {

                poster.loadFromUrlWithRoundedCorners(itemView.context,fullImageUrl,20)

                itemView.setOnClickListener { onMediaItemClicked.invoke(media) }
            }
        }
    }
}