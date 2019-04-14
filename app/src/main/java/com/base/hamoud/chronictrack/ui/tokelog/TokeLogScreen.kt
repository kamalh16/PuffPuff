package com.base.hamoud.chronictrack.ui.tokelog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.TextView
import androidx.core.content.ContextCompat
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

    private var tokeLogListView: RecyclerView? = null
    private lateinit var tokeLogListAdapter: TokeLogListAdapter
    private var tokeEmptyListMsgView: TextView? = null
    private var todaysTokesLineGraph: LineChart? = null
    private var todaysTotalTokeCountView: TextView? = null
    private var lastTokeTimeChronometer: Chronometer? = null
    private var nextTokeTimeChronometer: Chronometer? = null // TODO - need to implement

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.screen_toke_log, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TokeLogViewModel::class.java)

        // prepare ui
        prepareView()

        // observe
        observeOnUserLoggedInLive()
        observeOnGetUserTokeListLive()
        observeOnGetUserTokesCountLive()
        observeOnUserLastTokeTodayLive()

        viewModel.todayTokesDataLive.observe(this, Observer {
            Timber.i("TodaysTokes: $it")
            if (!it.isNullOrEmpty()) {
                // todo
                val dataSet = LineDataSet(it, "Todays Tokes")
                Timber.i("DataSet: ${dataSet.toSimpleString()}")
                val colorPrimaryText = ContextCompat.getColor(context!!, R.color.colorPrimaryText)
                val colorAccent = ContextCompat.getColor(context!!, R.color.colorAccent)
                dataSet.color = colorPrimaryText
                dataSet.setCircleColor(colorAccent)
                dataSet.valueTextColor = colorAccent
                val lineData = LineData(dataSet)
                todaysTokesLineGraph?.data = lineData
                todaysTokesLineGraph?.invalidate()
            }
        })

        // trigger
        viewModel.refreshTokeList()
        viewModel.refreshTokesTotalCount()
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
                tokeLogListAdapter.setTokeList(it)
            }
        })
    }

    private fun observeOnUserLastTokeTodayLive() {
        viewModel.userLastTokeTodayLive.observe(this, Observer {
            lastTokeTimeChronometer?.base = it
            lastTokeTimeChronometer?.start()
        })
    }

    private fun observeOnGetUserTokesCountLive() {
        viewModel.userTokesCountLive.observe(this, Observer {
            if (it != null) {
                todaysTotalTokeCountView?.text = it.toString()
            }
        })
    }

    private fun prepareView() {
        prepareTodaysTokeCountView()
        prepareTodaysTokesLineGraph()
        prepareTokeRvList()
        prepareTokeEmptyListMsgView()
        prepareAddTokeBtn()
    }

    private fun prepareTodaysTokeCountView() {
        todaysTotalTokeCountView = view?.findViewById(R.id.toke_log_screen_todays_toke_count)
        lastTokeTimeChronometer = view?.findViewById(R.id.toke_log_screen_last_toke_chronometer)
    }

    private fun prepareTodaysTokesLineGraph() {
        todaysTokesLineGraph = view?.findViewById(R.id.toke_log_screen_todays_tokes_trend)
        val textColor = ContextCompat.getColor(context!!, R.color.colorPrimaryText)
        todaysTokesLineGraph?.apply {
            setBackgroundColor(ContextCompat.getColor(activity!!, R.color.colorPrimary))
            setDrawGridBackground(false)
            axisLeft?.textColor = textColor // left y-axis
            xAxis?.textColor = textColor
            legend?.textColor = textColor
        }
    }

    private fun prepareTokeRvList() {
        tokeLogListView = view?.findViewById(R.id.toke_log_recycler_view)
        tokeLogListAdapter = TokeLogListAdapter(context!!)

        // setup RecyclerView
        tokeLogListView?.adapter = tokeLogListAdapter
        tokeLogListView?.layoutManager = LinearLayoutManager(context)
        tokeLogListView?.setHasFixedSize(true)
        tokeLogListView?.setItemViewCacheSize(10)
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
        val addTokeBtn = view?.findViewById<FloatingActionButton>(R.id.toke_log_screen_add_toke_btn)
        addTokeBtn?.setOnClickListener {
            findNavController().navigate(R.id.action_toke_log_screen_to_add_toke_screen)
        }
    }

}