package com.sosa.ignacio.codechallenge.seriesify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sosa.ignacio.codechallenge.seriesify.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        setObservers()
    }

    private fun setObservers() {
        viewModel.initConfigurationReady.observe(this, Observer { ready ->
            if(ready)
                init()
        })
    }

    private fun init() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, MainFragment.newInstance())
            .commitNow()
    }
}