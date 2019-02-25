package com.base.hamoud.chronictrack.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.base.hamoud.chronictrack.R
import com.base.hamoud.chronictrack.data.entity.User
import timber.log.Timber

class HomeScreen : Fragment() {

    private var loggedInUser: User? = null
    private lateinit var viewModel: HomeViewModel

    private var hitCountTextView: TextView? = null

    companion object {
        fun newInstance(): HomeScreen = HomeScreen()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        // prepare ui
        prepareTodaysHitCountView()

        // observe
        observeOnUserLoggedInLive()
        observeOnGetUserHitsCountLive()

        // trigger
        viewModel.refreshHitsTotalCount()
    }

    private fun observeOnUserLoggedInLive() {
        viewModel.loggedInUserLive.observe(this, Observer {
            if (it != null) {
                Timber.i("logged in user: ${it.username}, ${it.id}")
                loggedInUser = it
            }
        })
    }

    private fun observeOnGetUserHitsCountLive() {
        viewModel.userHitsCount.observe(this, Observer {
            if (it != null) {
                hitCountTextView?.text = it.toString()
            }
        })
    }

    private fun prepareTodaysHitCountView() {
        hitCountTextView = view?.findViewById(R.id.home_screen_toke_count_label)
    }

}