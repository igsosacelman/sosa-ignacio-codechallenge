package com.sosa.ignacio.codechallenge.seriesify.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sosa.ignacio.codechallenge.seriesify.R
import com.sosa.ignacio.codechallenge.seriesify.common.model.ImageConfiguration
import com.sosa.ignacio.codechallenge.seriesify.common.model.Media
import com.sosa.ignacio.codechallenge.seriesify.common.model.MediaHelper
import com.sosa.ignacio.codechallenge.seriesify.common.model.SubscriptionManager
import com.sosa.ignacio.codechallenge.seriesify.common.utils.loadFromUrlWithRoundedCorners
import com.sosa.ignacio.codechallenge.seriesify.databinding.ItemSearchListBinding

class SearchListAdapter(
    private val mediaHelper: MediaHelper,
    private val subscriptionManager: SubscriptionManager,
    private val onMediaItemClicked: OnMediaItemClicked? = null
) : ListAdapter<Media, SearchListViewHolder>(MediaDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchListViewHolder {
        val binding = ItemSearchListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchListViewHolder, position: Int) {
        if (position == 0) holder.setPadding()
        holder.populate(getItem(position), onMediaItemClicked, mediaHelper,subscriptionManager)
    }
}

class SearchListViewHolder(binding: ItemSearchListBinding) : RecyclerView.ViewHolder(binding.root) {

    private val poster = binding.poster
    private val name = binding.name
    private val genre = binding.genre
    private val subscription = binding.subscription

    fun setPadding() {
        with(itemView.layoutParams as ViewGroup.MarginLayoutParams) {
            topMargin = itemView.context.resources.getDimensionPixelSize(R.dimen.item_search_list_first_item_top_margin)
        }
    }

    fun populate(
        item: Media?,
        onMediaItemClicked: ((Media) -> Unit)?,
        mediaHelper: MediaHelper,
        subscriptionManager: SubscriptionManager
    ) {

        item?.let { media ->

            val isSubscribed = subscriptionManager.isSubscribed(media)
            val firstGenre = mediaHelper.getGenreById(media.genreIds.first())
            val fullImageUrl = mediaHelper.fullPosterPathUrlFrom(media.posterPath, ImageConfiguration.DEFAULT_BACKDROP_SIZE_INDEX)
            fullImageUrl?.let {

                poster.loadFromUrlWithRoundedCorners(itemView.context,fullImageUrl,20)

                name.text = media.name

                firstGenre?.let {
                    genre.text = it.name
                }

                with(subscription) {
                    isSelected = isSubscribed
                    text = subscribeButtonText(isSelected)

                    setOnClickListener {
                        isSelected = !isSelected
                        text = subscribeButtonText(isSelected)
                        subscriptionManager.handleSubscription(media)
                    }
                }

                onMediaItemClicked?.let {
                    itemView.setOnClickListener { onMediaItemClicked.invoke(media) }
                }
            }
        }
    }

    fun subscribeButtonText(isSubscribed: Boolean) = if(isSubscribed) itemView.context.resources.getString(R.string.item_search_is_subscribed) else itemView.context.resources.getString(R.string.item_search_is_not_subscribed)
}