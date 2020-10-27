package com.sosa.ignacio.codechallenge.seriesify.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sosa.ignacio.codechallenge.seriesify.common.model.Configuration
import com.sosa.ignacio.codechallenge.seriesify.common.model.ImageConfiguration
import com.sosa.ignacio.codechallenge.seriesify.common.model.Media
import com.sosa.ignacio.codechallenge.seriesify.common.repositories.configuration.DefaultConfigurationRepository
import com.sosa.ignacio.codechallenge.seriesify.common.utils.loadFromUrl
import com.sosa.ignacio.codechallenge.seriesify.common.utils.toGrayScale
import com.sosa.ignacio.codechallenge.seriesify.databinding.ItemMediaListBinding

typealias OnMediaItemClicked = (Media) -> Unit

class MediaListAdapter(private val onMediaItemClicked: OnMediaItemClicked) : ListAdapter<Media, MediaListViewHolder>(MediaDiffCallback()) {

    private val currentConfiguration = DefaultConfigurationRepository.currentConfiguration

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaListViewHolder {
        val binding = ItemMediaListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MediaListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MediaListViewHolder, position: Int) {
        holder.populate(getItem(position), onMediaItemClicked, currentConfiguration)
    }
}

class MediaListViewHolder(binding: ItemMediaListBinding) : RecyclerView.ViewHolder(binding.root) {

    private val name = binding.name
    private val genre = binding.genre
    private val background = binding.background

    fun populate(
        item: Media?,
        onMediaItemClicked: (Media) -> Unit,
        currentConfiguration: Configuration?
    ) {

        item?.let { media ->

            val fullImageUrl = fullImagePathUrlFrom(currentConfiguration,media.backdropPath,ImageConfiguration.DEFAULT_BACKDROP_SIZE_INDEX)
            fullImageUrl?.let {

                name.text = media.name
                background
                    .toGrayScale()
                    .loadFromUrl(context = itemView.context, source = fullImageUrl)

                itemView.setOnClickListener { onMediaItemClicked.invoke(media) }
            }
        }
    }

    private fun fullImagePathUrlFrom(configuration: Configuration?, path: String, sizeIndex: Int) =
        configuration?.let {
            it.images.secureBaseUrl + it.images.backdropSizes[sizeIndex] + path
        }
}

class MediaDiffCallback : DiffUtil.ItemCallback<Media>() {

    override fun areItemsTheSame(oldItem: Media, newItem: Media) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Media, newItem: Media): Boolean {
        return oldItem.name == newItem.name
    }
}