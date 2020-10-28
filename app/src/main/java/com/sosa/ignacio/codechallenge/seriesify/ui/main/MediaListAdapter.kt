package com.sosa.ignacio.codechallenge.seriesify.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sosa.ignacio.codechallenge.seriesify.common.model.*
import com.sosa.ignacio.codechallenge.seriesify.common.utils.loadFromUrlWithRoundedCorners
import com.sosa.ignacio.codechallenge.seriesify.common.utils.toGrayScale
import com.sosa.ignacio.codechallenge.seriesify.databinding.ItemMediaListBinding

typealias OnMediaItemClicked = (Media) -> Unit

class MediaListAdapter(private val mediaHelper: MediaHelper, private val onMediaItemClicked: OnMediaItemClicked) : ListAdapter<Media, MediaListViewHolder>(MediaDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaListViewHolder {
        val binding = ItemMediaListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MediaListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MediaListViewHolder, position: Int) {
        holder.populate(getItem(position), onMediaItemClicked, mediaHelper)
    }
}

class MediaListViewHolder(binding: ItemMediaListBinding) : RecyclerView.ViewHolder(binding.root) {

    private val name = binding.name
    private val genre = binding.genre
    private val background = binding.background

    fun populate(
        item: Media?,
        onMediaItemClicked: (Media) -> Unit,
        mediaHelper: MediaHelper
    ) {

        item?.let { media ->

            val firstGenre = mediaHelper.getGenreById(media.genreIds.first())
            val fullImageUrl = mediaHelper.fullBackdropPathUrlFrom(media.backdropPath,ImageConfiguration.DEFAULT_BACKDROP_SIZE_INDEX)
            fullImageUrl?.let {

                name.text = media.name

                background
                    .loadFromUrlWithRoundedCorners(itemView.context,fullImageUrl,20)
                    .toGrayScale()

                firstGenre?.let {
                    genre.text = it.name
                }

                itemView.setOnClickListener { onMediaItemClicked.invoke(media) }
            }
        }
    }
}

class MediaDiffCallback : DiffUtil.ItemCallback<Media>() {

    override fun areItemsTheSame(oldItem: Media, newItem: Media) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Media, newItem: Media): Boolean {
        return oldItem.name == newItem.name
    }
}