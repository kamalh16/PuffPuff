package com.base.hamoud.chronictrack.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.base.hamoud.chronictrack.R

class SettingsScreen : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_settings, container, false)
    }

    companion object {
        fun newInstance(): SettingsScreen = SettingsScreen()
    }
}