package com.base.hamoud.chronictrack.ui.tokelog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.base.hamoud.chronictrack.R
import com.base.hamoud.chronictrack.data.entity.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import timber.log.Timber

class TokeLogScreen : Fragment() {

    private var loggedInUser: User? = null
    private lateinit var viewModel: TokeLogViewModel

    private var hitListRecyclerView: RecyclerView? = null
    private lateinit var adapter: TokeLogListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_toke_log, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TokeLogViewModel::class.java)

        // prepare ui
        prepareTokeRvList()
        prepareAddTokeBtn()

        // observe
        observeOnUserLoggedInLive()
        observeOnGetUserTokeListLive()

        // trigger
        viewModel.refreshTokeList()
    }

    private fun observeOnUserLoggedInLive() {
        viewModel.loggedInUserLive.observe(this, Observer {
            if (it != null) {
                Timber.i("logged in user: ${it.username}, ${it.id}")
                loggedInUser = it
            }
        })
    }

    private fun observeOnGetUserTokeListLive() {
        viewModel.userTokeListLive.observe(this, Observer {
            if (it != null) {
                adapter.setTokeList(it)
            }
        })
    }

    private fun prepareTokeRvList() {
        hitListRecyclerView = view?.findViewById(R.id.toke_log_recycler_view)
        adapter = TokeLogListAdapter(context!!)

        // setup RecyclerView
        hitListRecyclerView?.adapter = adapter
        hitListRecyclerView?.layoutManager = LinearLayoutManager(context)
    }

    private fun prepareAddTokeBtn() {
        val addTokeBtn = view?.findViewById<FloatingActionButton>(R.id.toke_log_add_toke_btn)
        addTokeBtn?.setOnClickListener {
            findNavController().navigate(R.id.action_toke_log_screen_to_add_toke_screen)
        }
    }

}