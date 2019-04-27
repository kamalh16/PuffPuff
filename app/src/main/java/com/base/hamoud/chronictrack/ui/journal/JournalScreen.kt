package com.base.hamoud.chronictrack.ui.journal

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.base.hamoud.chronictrack.R
import com.base.hamoud.chronictrack.ui.drawer.CalendarBottomSheetFragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import timber.log.Timber


class JournalScreen : Fragment() {

    private lateinit var viewModel: JournalViewModel

    private var screenTitleView: TextView? = null
    private var screenSubTitleView: TextView? = null
    private var changeDateBtn: ImageView? = null
    private var tokeListView: RecyclerView? = null
    private lateinit var journalListAdapter: JournalListAdapter
    private var tokeEmptyListMsgView: TextView? = null
    private var todaysTokesGraph: BarChart? = null
    private var totalTokeCountView: TextView? = null
    private var lastTokedAtTimeChronometer: Chronometer? = null
    private var nextTokeAtTimeChronometer: Chronometer? = null // TODO - need to implement

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.screen_journal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(JournalViewModel::class.java)

        // prepare ui
        prepareView()

        // observe
        observeOnJournalDateLive()
        observeOnTokeListLive()
        observeOnTotalTokesCountLive()
        observeOnLastTokedAtTimeLive()
        observeOnTokesDataLive()

    }

    override fun onResume() {
        super.onResume()

        // trigger
        viewModel.refreshTokeList()
        viewModel.refreshTokesTotalCount()
        viewModel.refreshLastTokedAtTime()
        viewModel.getTodaysTokesData()
    }

    private fun observeOnTokesDataLive() {
        viewModel.todayTokesDataLive.observe(viewLifecycleOwner, Observer {
            Timber.i("TodaysTokes: $it")
            if (!it.isNullOrEmpty()) {
                // todo
                val dataSet = BarDataSet(it, "Todays Tokes")
                Timber.i("DataSet: ${dataSet.toSimpleString()}")
                val colorPrimaryText = ContextCompat.getColor(context!!, R.color.colorPrimaryText)
                val colorAccent = ContextCompat.getColor(context!!, R.color.colorAccent)
                dataSet.apply {
                    this.color = colorAccent
                    this.valueTextColor = colorAccent
                    this.barBorderWidth = 0.9f
                }

                val lineData = BarData(dataSet)
                todaysTokesGraph?.data = lineData
                todaysTokesGraph?.invalidate()

                // move to the latest entry
                todaysTokesGraph?.zoomIn()
                todaysTokesGraph?.zoomIn()
            }
        })
    }

    private fun observeOnJournalDateLive() {
        viewModel.journalDateLive.observe(viewLifecycleOwner, Observer {
            if (it != null) {

                // set screen title and subtitle
                screenTitleView?.text = formatTitleDate(it)
                screenSubTitleView?.text = formatSubtitleDate(it)

                // trigger
                viewModel.refreshTokeList()
                viewModel.refreshTokesTotalCount()
                viewModel.refreshLastTokedAtTime()
                viewModel.getTodaysTokesData()
            }
        })
    }

    private fun observeOnTokeListLive() {
        viewModel.tokeListLive.observe(viewLifecycleOwner, Observer {
            if (it != null && it.isNotEmpty()) {
                hideTokeEmptyListMsgView()
                journalListAdapter.setTokeList(it)
            } else {
                showTokeEmptyListMsgView()
                journalListAdapter.clearTokeList()
            }
        })
    }

    private fun observeOnLastTokedAtTimeLive() {
        viewModel.lastTokedAtTimeLive.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                lastTokedAtTimeChronometer?.base = it
                lastTokedAtTimeChronometer?.start()
            } else {
                // reset to 00:00
                lastTokedAtTimeChronometer?.base = SystemClock.elapsedRealtime()
                lastTokedAtTimeChronometer?.stop()
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
        prepareScreenTitleView()
        prepareScreenSubTitleView()
        prepareChangeDateBtn()
        prepareTodaysTokeCountView()
        prepareTodaysTokesGraph()
        prepareTokeRvList()
        prepareTokeEmptyListMsgView()
        prepareAddTokeBtn()
    }

    private fun prepareScreenTitleView() {
        screenTitleView = view?.findViewById(R.id.journal_screen_title)
        screenTitleView?.setText(R.string.label_today)
    }

    private fun prepareScreenSubTitleView() {
        screenSubTitleView = view?.findViewById(R.id.journal_screen_subtitle)
        screenSubTitleView?.text = formatSubtitleDate(DateTime.now())
    }

    private fun prepareChangeDateBtn() {
        changeDateBtn = view?.findViewById(R.id.journal_change_date_btn)
        changeDateBtn?.setOnClickListener {
            val bottomSheetDialog = CalendarBottomSheetFragment.getInstance()
            bottomSheetDialog.setViewModel(viewModel)
            fragmentManager?.let {
                bottomSheetDialog.show(it, CalendarBottomSheetFragment::javaClass.name)
            }
        }
    }

    private fun prepareTodaysTokeCountView() {
        totalTokeCountView = view?.findViewById(R.id.journal_screen_todays_toke_count)
        lastTokedAtTimeChronometer = view?.findViewById(R.id.journal_screen_last_toke_chronometer)
    }

    private fun prepareTodaysTokesGraph() {
        todaysTokesGraph = view?.findViewById(R.id.journal_screen_todays_tokes_chart)
        val textColor = ContextCompat.getColor(context!!, R.color.colorPrimaryText)
        val des = Description().also {
            it.text = "todays tokes over 24 hour period"
        }

        // x-axis value formatter
        val hrsArr = arrayListOf(
            "midnight",
            "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "10",
            "11", "noon",
            "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "10",
            "11")
        val xAxisFormatter = IndexAxisValueFormatter(hrsArr)

        todaysTokesGraph?.apply {
            this.legend.isEnabled = false
            this.axisRight?.isEnabled = false
            this.axisLeft?.isEnabled = false
            this.description = des
            this.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.colorPrimary))
            this.setDrawGridBackground(false)
            this.setDrawMarkers(false)
            this.setDrawValueAboveBar(false)
            this.setFitBars(true)
            // xAxis
            this.xAxis?.position = XAxis.XAxisPosition.BOTTOM
            this.xAxis?.textColor = textColor
            this.xAxis?.granularity = 1f
            this.xAxis?.setDrawGridLines(false)
            this.xAxis?.valueFormatter = xAxisFormatter
        }
    }

    private fun prepareTokeRvList() {
        tokeListView = view?.findViewById(R.id.journal_screen_tokes_recycler_view)
        journalListAdapter = JournalListAdapter(context!!)
        // setup RecyclerView
        tokeListView?.adapter = journalListAdapter
        tokeListView?.layoutManager = LinearLayoutManager(context)
        tokeListView?.setHasFixedSize(true)
        tokeListView?.setItemViewCacheSize(10)
    }

    private fun prepareTokeEmptyListMsgView() {
        tokeEmptyListMsgView = view?.findViewById(R.id.journal_screen_empty_list_msg)
        hideTokeEmptyListMsgView()
    }

    private fun showTokeEmptyListMsgView() {
        tokeEmptyListMsgView?.visibility = View.VISIBLE
    }

    private fun hideTokeEmptyListMsgView() {
        tokeEmptyListMsgView?.visibility = View.INVISIBLE
    }

    private fun prepareAddTokeBtn() {
        val addTokeBtn = view?.findViewById<FloatingActionButton>(R.id.journal_screen_add_toke_btn)
        addTokeBtn?.setOnClickListener {
            findNavController().navigate(R.id.action_toke_log_screen_to_add_toke_screen)
        }
    }

    private fun formatTitleDate(dateTime: DateTime): String {
        return if (dateTime.dayOfMonth == DateTime.now().dayOfMonth) {
            resources.getString(R.string.label_today)
        } else {
            dateTime.dayOfWeek().asText
        }
    }

    private fun formatSubtitleDate(dateTime: DateTime): String {
        return dateTime
            .toString(DateTimeFormat.longDate())
            .split(",")[0]// remove everything after "," to achieve "M, d"
    }

}