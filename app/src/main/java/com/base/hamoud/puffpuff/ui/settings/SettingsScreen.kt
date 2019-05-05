package com.base.hamoud.puffpuff.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.base.hamoud.puffpuff.R


class SettingsScreen : Fragment() {

    private lateinit var viewModel: SettingsViewModel

    private var settingsRvList: RecyclerView? = null
    private var settingsListAdapter: SettingsListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.screen_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)

        // prepare ui
        prepareSettingsRvList()

        // observe
        observeSettingsLive()
        observeSettingsOptionsLive()
    }

    /**
     * Initialize and prepare [settingsRvList] and then
     * initialize and attach [settingsListAdapter] to it. Finally pass in
     * the constructed [itemListDarkTheme] to the adapter to set the list items.
     *
     * The [viewModel] is passed into [settingsListAdapter] in order to persist
     * changes to the user's settings and affect background changes.
     */
    private fun prepareSettingsRvList() {
        settingsRvList = view?.findViewById(R.id.settings_screen_options_recyclerview)
        settingsRvList?.let {
            // setup rv
            it.layoutManager = LinearLayoutManager(activity)
            it.setHasFixedSize(true)

        }
    }

    private fun observeSettingsLive() {
        viewModel.settingsLive.observe(viewLifecycleOwner, Observer {
            // init and attach rv adapter w/ settings
            settingsListAdapter = SettingsListAdapter(viewModel, it)
            settingsRvList?.adapter = settingsListAdapter
        })
    }

    private fun observeSettingsOptionsLive() {
        viewModel.settingsOptionsLive.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                // set data
                settingsListAdapter?.setData(it)
            }
        })
    }
}