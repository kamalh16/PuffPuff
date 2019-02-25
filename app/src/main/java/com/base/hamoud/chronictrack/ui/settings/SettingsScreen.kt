package com.base.hamoud.chronictrack.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.base.hamoud.chronictrack.R

class SettingsScreen : Fragment() {

    private lateinit var viewModel: SettingsViewModel

    companion object {
        fun newInstance(): SettingsScreen = SettingsScreen()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)

    }

}