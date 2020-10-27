package com.sosa.ignacio.codechallenge.seriesify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sosa.ignacio.codechallenge.seriesify.common.repositories.configuration.DefaultConfigurationRepository
import com.sosa.ignacio.codechallenge.seriesify.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            DefaultConfigurationRepository.getConfiguration({ onSuccess()}, {onFailure()})
        }
    }

    private fun onFailure() {
        TODO("We should show an error message, since there are no image to show")
    }

    private fun onSuccess() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, MainFragment.newInstance())
            .commitNow()
    }
}