package com.sosa.ignacio.codechallenge.seriesify.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sosa.ignacio.codechallenge.seriesify.R
import com.sosa.ignacio.codechallenge.seriesify.common.model.MediaHelper
import com.sosa.ignacio.codechallenge.seriesify.common.model.SubscriptionManager
import com.sosa.ignacio.codechallenge.seriesify.common.utils.togglePresence
import com.sosa.ignacio.codechallenge.seriesify.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    private lateinit var binding: MainFragmentBinding
    private lateinit var viewModel: MainViewModel

    private lateinit  var mainMediaListAdapter: MediaListAdapter
    private lateinit  var subscriptionListAdapter: SubscriptionListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadSubscriptions(SubscriptionManager(requireContext()))
        if(!viewModel.mediaList.value.isNullOrEmpty() && viewModel.mediaHelper.value != null) {
            setup(viewModel.mediaHelper.value!!)
            mainMediaListAdapter.submitList(viewModel.mediaList.value)
            mainMediaListAdapter.notifyDataSetChanged()
            viewModel.getSubscriptions()
        }
    }

    private fun setup(mediaHelper: MediaHelper) {
        with(binding.mainMediaList) {
            mainMediaListAdapter = MediaListAdapter(mediaHelper) { viewModel.onMediaItemClicked(it) }
            layoutManager = LinearLayoutManager(context)
            adapter = mainMediaListAdapter
        }
        with(binding.subscriptionList) {
            subscriptionListAdapter = SubscriptionListAdapter(mediaHelper) { viewModel.onMediaItemClicked(it) }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = subscriptionListAdapter
        }
    }

    private fun setObservers() {
        viewModel.loading.observe(this, Observer { loading ->
            binding.loader.togglePresence(loading)
        })

        viewModel.mediaList.observe(this, Observer { mediaList ->
            mediaList?.let {
                mainMediaListAdapter.submitList(it)
                mainMediaListAdapter.notifyDataSetChanged()
            }
        })

        viewModel.mediaHelper.observe(this, Observer { mediaHelper ->
            mediaHelper?.let {
                setup(mediaHelper)
            }
        })

        viewModel.itemSelected.observe(this, Observer { item ->
            item?.let {
                showMediaDetail()
            }
        })

        viewModel.initConfigurationReady.observe(this, Observer { ready ->
            if(ready)
                viewModel.onConfigReady()
        })

        viewModel.subscriptions.observe(this, Observer { subscriptions ->
            if(subscriptions.isNullOrEmpty()) {
                with(binding) {
                    subscriptionTitle.visibility = View.GONE
                    subscriptionList.visibility = View.GONE
                }
            } else {
                subscriptionListAdapter.submitList(subscriptions)
                subscriptionListAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun showMediaDetail() {
        findNavController().navigate(R.id.action_mainFragment_to_detailFragment)
    }
}