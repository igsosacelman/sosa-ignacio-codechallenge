package com.sosa.ignacio.codechallenge.seriesify.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sosa.ignacio.codechallenge.seriesify.R
import com.sosa.ignacio.codechallenge.seriesify.common.model.ImageConfiguration
import com.sosa.ignacio.codechallenge.seriesify.common.model.SubscriptionManager
import com.sosa.ignacio.codechallenge.seriesify.common.utils.loadFromUrl
import com.sosa.ignacio.codechallenge.seriesify.common.utils.loadFromUrlWithRoundedCorners
import com.sosa.ignacio.codechallenge.seriesify.databinding.DetailFragmentBinding

class DetailFragment : Fragment() {

    private lateinit var binding: DetailFragmentBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var subscriptionManager: SubscriptionManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.detail_fragment, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        subscriptionManager = SubscriptionManager(requireContext())
        setObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setObservers() {
        viewModel.mediaDetails.observe(this, Observer { details ->
            if(details.first != null && details.second != null){
                val media = details.first
                val helper = details.second
                val backdropImageUrl = helper.fullBackdropPathUrlFrom(media.backdropPath,ImageConfiguration.Companion.BackdropSizes.ORIGINAL.ordinal)
                val posterImageUrl = helper.fullPosterPathUrlFrom(media.posterPath, ImageConfiguration.Companion.PosterSizes.ORIGINAL.ordinal)
                val isSubscribed = subscriptionManager.isSubscribed(media)

                with(binding) {

                    posterImageUrl?.let {
                        poster.loadFromUrlWithRoundedCorners(requireContext(),posterImageUrl,20)
                    }

                    backdropImageUrl?.let {
                        backdrop.loadFromUrl(requireContext(),backdropImageUrl)
                    }

                    name.text = media.name
                    year.text = media.year
                    description.text = media.overview

                    subscription.run {
                        isSelected = isSubscribed
                        text = if(isSubscribed) context.resources.getString(R.string.detail_button_is_subscribed) else context.resources.getString(R.string.detail_button_is_not_subscribed)
                    }
                }
            }
        })

        viewModel.subscription.observe(this, Observer { isSubscribed ->
            binding.subscription.run {
                isSelected = isSubscribed
                text = if(isSubscribed) context.resources.getString(R.string.detail_button_is_subscribed) else context.resources.getString(R.string.detail_button_is_not_subscribed)
            }
        })
    }

    private fun setListeners() {
        with(binding){
            backButton.setOnClickListener {
                findNavController().navigateUp()
            }
            subscription.setOnClickListener {
                viewModel.onSubscriptionClicked(subscriptionManager, !subscription.isSelected)
            }
        }
    }
}
