package com.base.hamoud.chronictrack.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.base.hamoud.chronictrack.R
import com.base.hamoud.chronictrack.ui.settings.model.SettingsItem


class SettingsScreen : Fragment() {

    private lateinit var viewModel: SettingsViewModel

    private var settingsRvList: RecyclerView? = null
    private lateinit var settingsListAdapter: SettingsListAdapter

    private val settingsOptionsList =
          arrayListOf(
                SettingsItem.DARK_MODE,
                SettingsItem.CLEAR_DATA,
                SettingsItem.ABOUT
          )

    companion object {
        fun newInstance(): SettingsScreen = SettingsScreen()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)

        // prepare ui
        prepareSettingsRvList()

        // trigger

    }

    private fun prepareSettingsRvList() {
        settingsRvList = view?.findViewById(R.id.settings_screen_options_recyclerview)
        settingsRvList?.let {
            // setup rv
            it.layoutManager = LinearLayoutManager(activity)
            it.setHasFixedSize(true)
            // attach adapter
            settingsListAdapter = SettingsListAdapter(viewModel)
            it.adapter = settingsListAdapter
            settingsListAdapter.setData(settingsOptionsList)
        }

    }

}