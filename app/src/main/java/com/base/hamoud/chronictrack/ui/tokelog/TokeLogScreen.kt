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
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.floatingactionbutton.FloatingActionButton
import timber.log.Timber


class TokeLogScreen : Fragment() {

    private lateinit var viewModel: TokeLogViewModel

    private var tokeLogListView: RecyclerView? = null
    private lateinit var tokeLogListAdapter: TokeLogListAdapter
    private var tokeEmptyListMsgView: TextView? = null
    private var tokesLineGraph: LineChart? = null
    private var totalTokeCountView: TextView? = null
    private var lastTokedAtTimeChronometer: Chronometer? = null
    private var nextTokeAtTimeChronometer: Chronometer? = null // TODO - need to implement

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
        observeOnTokeListLive()
        observeOnTotalTokesCountLive()
        observeOnLastTokedAtTimeLive()

        viewModel.todayTokesDataLive.observe(viewLifecycleOwner, Observer {
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
                tokesLineGraph?.data = lineData
                tokesLineGraph?.invalidate()
            }
        })

        // trigger
        viewModel.refreshTokeList()
        viewModel.refreshTokesTotalCount()
        viewModel.refreshLastTokedAtTime()
        viewModel.getTodaysTokesData()
    }

    override fun onResume() {
        super.onResume()

        // trigger
        viewModel.refreshTokeList()
        viewModel.refreshTokesTotalCount()
        viewModel.refreshLastTokedAtTime()
        viewModel.getTodaysTokesData()
    }

    private fun observeOnTokeListLive() {
        viewModel.tokeListLive.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.isEmpty()) {
                    showTokeEmptyListMsgView()
                }
                tokeLogListAdapter.setTokeList(it)
            }
        })
    }

    private fun observeOnLastTokedAtTimeLive() {
        viewModel.lastTokedAtTimeLive.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                lastTokedAtTimeChronometer?.base = it
                lastTokedAtTimeChronometer?.start()
            }
        })
    }

    private fun observeOnTotalTokesCountLive() {
        viewModel.totalTokeCountLive.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                totalTokeCountView?.text = it.toString()
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
        totalTokeCountView = view?.findViewById(R.id.toke_log_screen_todays_toke_count)
        lastTokedAtTimeChronometer = view?.findViewById(R.id.toke_log_screen_last_toke_chronometer)
    }

    private fun prepareTodaysTokesLineGraph() {
        tokesLineGraph = view?.findViewById(R.id.toke_log_screen_todays_tokes_trend)
        val textColor = ContextCompat.getColor(context!!, R.color.colorPrimaryText)
        val des = Description().also {
            it.text = "todays tokes over 24 hour period"
        }

        tokesLineGraph?.apply {
            setBackgroundColor(ContextCompat.getColor(activity!!, R.color.colorPrimary))
            setDrawGridBackground(false)
            legend.isEnabled = false
            description = des
            axisLeft?.textColor = textColor // left y-axis
            axisLeft.axisMinimum = 0f
            axisLeft.granularity = 1f
            axisLeft.labelCount = 4
            axisRight?.textColor = textColor // left y-axis
            axisRight.axisMinimum = 0f
            axisRight.granularity = 1f
            axisRight.labelCount = 4
            xAxis?.textColor = textColor
            xAxis?.position = XAxis.XAxisPosition.BOTTOM
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