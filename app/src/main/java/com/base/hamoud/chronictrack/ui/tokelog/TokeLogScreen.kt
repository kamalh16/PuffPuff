package com.base.hamoud.chronictrack.ui.tokelog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.base.hamoud.chronictrack.R
import com.base.hamoud.chronictrack.data.entity.User
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.floatingactionbutton.FloatingActionButton
import timber.log.Timber

class TokeLogScreen : Fragment() {

    private var loggedInUser: User? = null
    private lateinit var viewModel: TokeLogViewModel

    private var tokeEmptyListMsgView: TextView? = null
    private var hitListRecyclerView: RecyclerView? = null
    private lateinit var adapter: TokeLogListAdapter
    private lateinit var todaysTokesLineGraph: LineChart

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_toke_log, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TokeLogViewModel::class.java)

        // prepare ui
        prepareTokeRvList()
        prepareTokeEmptyListMsgView()
        prepareAddTokeBtn()
        // prepare todaysTokesLineGraph todo
        todaysTokesLineGraph = view.findViewById(R.id.home_screen_todays_tokes_trend)
        todaysTokesLineGraph.apply {
            setBackgroundColor(resources.getColor(R.color.colorPrimary))
            setDrawGridBackground(false)
        }

        // observe
        observeOnUserLoggedInLive()
        observeOnGetUserTokeListLive()

        viewModel.todayTokesData.observe(this, Observer {
            Timber.i("TodaysTokes: $it")
            if (!it.isNullOrEmpty()) {
                // todo
                val dataSet = LineDataSet(it, "Todays Tokes")
                Timber.i("DataSet: ${dataSet.toSimpleString()}")
                dataSet.color = R.color.colorAccent
                val lineData = LineData(dataSet)
                todaysTokesLineGraph.data = lineData
                todaysTokesLineGraph.invalidate()
            }
        })

        // trigger
        viewModel.refreshTokeList()
        viewModel.getTodaysTokesData()
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
                if (it.isEmpty()) {
                    showTokeEmptyListMsgView()
                }
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
        hitListRecyclerView?.setHasFixedSize(true)
        hitListRecyclerView?.setItemViewCacheSize(10)
    }

    private fun prepareTokeEmptyListMsgView() {
        tokeEmptyListMsgView = view?.findViewById(R.id.toke_log_screen_empty_list_msg)
        hideTokeEmptyListMsgView()
    }

    private fun showTokeEmptyListMsgView() {
        tokeEmptyListMsgView?.visibility = View.VISIBLE
    }

    private fun hideTokeEmptyListMsgView() {
        tokeEmptyListMsgView?.visibility = View.INVISIBLE
    }

    private fun prepareAddTokeBtn() {
        val addTokeBtn = view?.findViewById<FloatingActionButton>(R.id.toke_log_add_toke_btn)
        addTokeBtn?.setOnClickListener {
            findNavController().navigate(R.id.action_toke_log_screen_to_add_toke_screen)
        }
    }

}