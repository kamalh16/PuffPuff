package com.base.hamoud.chronictrack.ui.tokelog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.base.hamoud.chronictrack.R
import com.base.hamoud.chronictrack.data.entity.User
import com.base.hamoud.chronictrack.ui.drawer.HitFormBottomDrawerFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import timber.log.Timber

class TokeLogScreen : Fragment() {

    private var loggedInUser: User? = null
    private lateinit var viewModel: TokeLogViewModel

    private lateinit var hitFormBottomDrawerFragment: HitFormBottomDrawerFragment
    private var hitListRecyclerView: RecyclerView? = null

    private lateinit var adapter: TokeLogListAdapter

    companion object {
        fun newInstance(): TokeLogScreen = TokeLogScreen()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_toke_log, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TokeLogViewModel::class.java)

        // prepare ui
        prepareHitFormBottomSheet()
        prepareHitsRecyclerView()
        prepareHitBtn()

        // observe
        observeOnUserLoggedInLive()
        observeOnGetUserHitsListLive()

        // trigger
        viewModel.refreshHitsList()
    }

    private fun observeOnUserLoggedInLive() {
        viewModel.loggedInUserLive.observe(this, Observer {
            if (it != null) {
                Timber.i("logged in user: ${it.username}, ${it.id}")
                loggedInUser = it
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

    private fun prepareHitsRecyclerView() {
        hitListRecyclerView = view?.findViewById(R.id.toke_log_recycler_view)
        adapter = TokeLogListAdapter(context!!)

        // setup RecyclerView
        hitListRecyclerView?.adapter = adapter
        hitListRecyclerView?.layoutManager = LinearLayoutManager(context) as RecyclerView.LayoutManager?
    }

    private fun prepareHitBtn() {
        val hitBtn = view?.findViewById<FloatingActionButton>(R.id.toke_log_add_hit_btn)
        hitBtn?.setOnClickListener {
            hitFormBottomDrawerFragment.show(fragmentManager!!, HitFormBottomDrawerFragment::javaClass.name)
        }
    }

    private fun prepareHitFormBottomSheet() {
        hitFormBottomDrawerFragment = HitFormBottomDrawerFragment()
        hitFormBottomDrawerFragment.saveHit.observe(this, androidx.lifecycle.Observer { hit ->
            if (hit != null) {
                loggedInUser?.let {
                    hit.userId = it.id
                    viewModel.insertHit(hit)
                    viewModel.refreshHitsList()
                    hitListRecyclerView?.scrollToPosition(0)
                }
                hitFormBottomDrawerFragment.dismiss()
            }
        })
    }
}