package com.base.hamoud.chronictrack.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.base.hamoud.chronictrack.R
import com.base.hamoud.chronictrack.data.entity.User
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import timber.log.Timber

class HomeScreen : Fragment() {

    private var loggedInUser: User? = null
    private lateinit var viewModel: HomeViewModel

    private var tokeCountView: TextView? = null
    private var tokeTimerChronometer: Chronometer? = null

    private lateinit var weeklyTokesLineChart: LineChart

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        // prepare ui
        prepareTodaysTokeCountView()
        weeklyTokesLineChart = view.findViewById(R.id.home_screen_weekly_tokes_trend)
        weeklyTokesLineChart.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        weeklyTokesLineChart.setDrawGridBackground(false)
        val limitLine: LimitLine = LimitLine(7f, "Days")
        limitLine.lineWidth = 3f
        limitLine.labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM
        limitLine.textSize = 8f
        weeklyTokesLineChart.xAxis.addLimitLine(limitLine)


        // observe
        observeOnUserLoggedInLive()
        observeOnGetUserTokesCountLive()
        observeOnUserLastTokeTodayLive()
        observeOnChartData()

        // triggers
        viewModel.refreshTokesTotalCount()
        viewModel.getThisWeeksTokesData()

    }

    private fun observeOnUserLastTokeTodayLive() {
        viewModel.userLastTokeTodayLive.observe(this, Observer {
            tokeTimerChronometer?.base = it
            tokeTimerChronometer?.start()
        })
    }

    private fun observeOnUserLoggedInLive() {
        viewModel.loggedInUserLive.observe(this, Observer {
            if (it != null) {
//                Timber.i("logged in user: ${it.username}, ${it.id}")
                loggedInUser = it
            }
        })
    }

    private fun observeOnGetUserTokesCountLive() {
        viewModel.userTokesCountLive.observe(this, Observer {
            if (it != null) {
                tokeCountView?.text = it.toString()
            }
        })
    }

    private fun observeOnChartData() {
        viewModel.weeksTokesData.observe(this, Observer {
            Timber.i("Weeks Tokes: $it")
            if (!it.isNullOrEmpty()) {
                // todo
                val dataSet = LineDataSet(it, "Weeks Tokes")
                Timber.i("DataSet: ${dataSet.toSimpleString()}")
                val weekNames = arrayOf(" ","Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

                dataSet.color = R.color.colorAccent
                val lineData = LineData(dataSet)
                weeklyTokesLineChart.data = lineData
                weeklyTokesLineChart.invalidate()
            }
        })

    }

    private fun prepareTodaysTokeCountView() {
        tokeCountView = view?.findViewById(R.id.home_screen_todays_toke_count)
        tokeTimerChronometer = view?.findViewById(R.id.home_screen_last_hit_chronometer)
    }

}