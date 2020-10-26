package com.sosa.ignacio.codechallenge.seriesify.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sosa.ignacio.codechallenge.seriesify.R
import com.sosa.ignacio.codechallenge.seriesify.common.utils.togglePresence
import com.sosa.ignacio.codechallenge.seriesify.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    private lateinit var binding: MainFragmentBinding
    private lateinit var viewModel: MainViewModel

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
        setListeners()
    }

    private fun setListeners() {
        binding.button.setOnClickListener {
            viewModel.onButtonClicked()
        }
    }

    private fun setObservers() {
        viewModel.message.observe(this, Observer { message ->
            binding.message.text = message
        })

        viewModel.loading.observe(this, Observer { loading ->
            binding.loader.togglePresence(loading)
        })
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}