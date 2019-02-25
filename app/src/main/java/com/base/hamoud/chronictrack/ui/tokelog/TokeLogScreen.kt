package com.base.hamoud.chronictrack.ui.tokelog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.base.hamoud.chronictrack.R
import com.base.hamoud.chronictrack.data.entity.User
import com.base.hamoud.chronictrack.ui.drawer.HitFormBottomDrawerFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TokeLogScreen : Fragment() {

    private lateinit var viewModel: TokeLogViewModel

    private lateinit var loggedInUser: User

    private var hitListRecyclerView: RecyclerView? = null
    private var hitCountTextView: TextView? = null

    private lateinit var adapter: TokeLogListAdapter

    companion object {
        fun newInstance(): TokeLogScreen = TokeLogScreen()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_toke_log, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(TokeLogViewModel::class.java)

        prepareTodaysHitCountView()
        prepareHitsRecyclerView()
        prepareHitBtn()

        // observe
        observeOnUserLoggedIn()
        observeOnGetUserHitsCountLive()
        observeOnGetUserHitsListLive()

        // trigger
        viewModel.refreshHitsTotalCount()
        viewModel.refreshHitsList()
    }

    private fun observeOnUserLoggedIn() {
        viewModel.loggedInUserLive.observe(this, Observer {
            if (it != null) {
                // set user, set profile, etc.
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

    private fun observeOnGetUserHitsListLive() {
        viewModel.userHitsListLive.observe(this, Observer {
            if (it != null) {
                adapter.setHits(it)
            }
        })
    }

    private fun prepareTodaysHitCountView() {
        hitCountTextView = view?.findViewById(R.id.todays_hit_count)
    }

    private fun prepareHitsRecyclerView() {
        hitListRecyclerView = view?.findViewById(R.id.toke_log_recycler_view)
        adapter = TokeLogListAdapter(context!!)

        // setup RecyclerView
        hitListRecyclerView?.adapter = adapter
        hitListRecyclerView?.layoutManager = LinearLayoutManager(context) as RecyclerView.LayoutManager?

        // add swipe-to-delete support
//        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(adapter))
//        itemTouchHelper.attachToRecyclerView(hitListRecyclerView)
    }

    private fun prepareHitBtn() {
        val hitBtn = view?.findViewById<FloatingActionButton>(R.id.toke_log_add_hit_btn)
        hitBtn?.setOnClickListener {
            HitFormBottomDrawerFragment().show(fragmentManager!!, HitFormBottomDrawerFragment::javaClass.name)
        }
    }
}