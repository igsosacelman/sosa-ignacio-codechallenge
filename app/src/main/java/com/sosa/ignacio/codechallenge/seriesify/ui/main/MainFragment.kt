package com.sosa.ignacio.codechallenge.seriesify.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.TextViewCompat
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
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    private lateinit var binding: MainFragmentBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var subscriptionManager: SubscriptionManager

    private lateinit  var mainMediaListAdapter: MediaListAdapter
    private lateinit  var subscriptionListAdapter: SubscriptionListAdapter
    private lateinit  var searchListAdapter: SearchListAdapter

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
        subscriptionManager = SubscriptionManager(requireContext())
        viewModel.loadSubscriptions(subscriptionManager)
        if(!viewModel.mediaList.value.isNullOrEmpty() && viewModel.mediaHelper.value != null) {
            setup(viewModel.mediaHelper.value!!)
            mainMediaListAdapter.submitList(viewModel.mediaList.value)
            mainMediaListAdapter.notifyDataSetChanged()
            viewModel.getSubscriptions()
        }
        setListeners()
        setSearchBarStyle()
    }

    private fun setup(mediaHelper: MediaHelper) {
        with(binding.mainMediaList) {
            mainMediaListAdapter = MediaListAdapter(mediaHelper) { viewModel.onMediaItemClicked(it) }
            layoutManager = LinearLayoutManager(context)
            ViewCompat.setNestedScrollingEnabled(this,false)
            adapter = mainMediaListAdapter
        }
        with(binding.subscriptionList) {
            subscriptionListAdapter = SubscriptionListAdapter(mediaHelper) { viewModel.onMediaItemClicked(it) }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = subscriptionListAdapter
        }
        with(binding.searchList) {
            searchListAdapter = SearchListAdapter(mediaHelper,subscriptionManager)
            layoutManager = LinearLayoutManager(context)
            adapter = searchListAdapter
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

        viewModel.searchMode.observe(this, Observer { searchMode ->
            if(searchMode) {
                searchList.visibility = View.VISIBLE
                search.visibility = View.VISIBLE
                searchIcon.visibility = View.GONE
                toolbarCancel.visibility = View.VISIBLE
                toolbarTitle.visibility = View.GONE
                searchListAdapter.submitList(viewModel.filteredSearch.value)
                searchListAdapter.notifyDataSetChanged()
            } else {
                searchList.visibility = View.GONE
                search.visibility = View.GONE
                searchIcon.visibility = View.VISIBLE
                toolbarCancel.visibility = View.GONE
                toolbarTitle.visibility = View.VISIBLE
            }
        })

        viewModel.filteredSearch.observe(this, Observer { filteredMediaList ->
            searchListAdapter.submitList(filteredMediaList)
            searchListAdapter.notifyDataSetChanged()
        })
    }

    private fun setListeners() {
       with(binding) {
           searchIcon.setOnClickListener {
               viewModel.onSearchIconClicked()
           }
           toolbarCancel.setOnClickListener {
               viewModel.onCancelSearchClicked()
           }
           search.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
               override fun onQueryTextSubmit(query: String?): Boolean {
                   return false
               }

               override fun onQueryTextChange(newText: String?): Boolean {
                   newText?.let {
                       viewModel.onQueryTextChange(it)
                   }
                   return true
               }
           })
       }
    }

    private fun setSearchBarStyle() {
        binding.search.apply {
            findViewById<ImageView>(R.id.search_close_btn).apply {
                setColorFilter(ContextCompat.getColor(context, R.color.white_opacity_45))
            }
            findViewById<ImageView>(R.id.search_mag_icon).apply {
                translationX = -20f
                translationY = 2f
                ResourcesCompat.getDrawable(resources, R.drawable.ic_search, context.theme);
                setColorFilter(ContextCompat.getColor(context, R.color.grey_search_icon))
            }
            findViewById<EditText>(R.id.search_src_text).apply {
                TextViewCompat.setTextAppearance(this,R.style.SFNSRegular_MediumMore_White)
                setHintTextColor(ContextCompat.getColor(context, R.color.white))
            }
        }
    }

    private fun showMediaDetail() {
        findNavController().navigate(R.id.action_mainFragment_to_detailFragment)
    }
}