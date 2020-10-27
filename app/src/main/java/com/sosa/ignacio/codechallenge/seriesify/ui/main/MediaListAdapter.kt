package com.sosa.ignacio.codechallenge.seriesify.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sosa.ignacio.codechallenge.seriesify.common.model.Configuration
import com.sosa.ignacio.codechallenge.seriesify.common.model.Genre
import com.sosa.ignacio.codechallenge.seriesify.common.model.ImageConfiguration
import com.sosa.ignacio.codechallenge.seriesify.common.model.Media
import com.sosa.ignacio.codechallenge.seriesify.common.utils.loadFromUrl
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
            val fullImageUrl = mediaHelper.fullImagePathUrlFrom(media.backdropPath,ImageConfiguration.DEFAULT_BACKDROP_SIZE_INDEX)
            fullImageUrl?.let {

                name.text = media.name
                background
                    .toGrayScale()
                    .loadFromUrl(context = itemView.context, source = fullImageUrl)

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

data class MediaHelper(
    private val configuration: Configuration,
    private val genres: List<Genre>
) {

    fun fullImagePathUrlFrom(path: String, sizeIndex: Int) =
        configuration.images.secureBaseUrl + configuration.images.backdropSizes[sizeIndex] + path

    fun getGenreById(id: Int) = genres.find { it.id == id }
}