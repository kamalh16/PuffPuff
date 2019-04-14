package com.base.hamoud.chronictrack.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.base.hamoud.chronictrack.R
import com.base.hamoud.chronictrack.data.entity.User
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.floatingactionbutton.FloatingActionButton
import timber.log.Timber

class HomeScreen : Fragment() {

    private var loggedInUser: User? = null
    private lateinit var viewModel: HomeViewModel

    private lateinit var weeklyTokesLineChart: LineChart

    override fun onCreateView(
          inflater: LayoutInflater,
          container: ViewGroup?,
          savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.screen_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        // prepare ui
        prepareView()
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
        observeOnChartData()

        // triggers
        viewModel.getThisWeeksTokesData()

    }

    private fun observeOnUserLoggedInLive() {
        viewModel.loggedInUserLive.observe(this, Observer {
            if (it != null) {
//                Timber.i("logged in user: ${it.username}, ${it.id}")
                loggedInUser = it
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
                val weekNames = arrayOf(
                      " ",
                      "Monday",
                      "Tuesday",
                      "Wednesday",
                      "Thursday",
                      "Friday",
                      "Saturday",
                      "Sunday"
                )

                dataSet.color = R.color.colorAccent
                val lineData = LineData(dataSet)
                weeklyTokesLineChart.data = lineData
                weeklyTokesLineChart.invalidate()
            }
        })

    }

    private fun prepareView() {
        prepareAddTokeBtn()
    }

    private fun prepareAddTokeBtn() {
        val addTokeBtn = view?.findViewById<FloatingActionButton>(R.id.home_screen_add_toke_btn)
        addTokeBtn?.setOnClickListener {
            findNavController().navigate(R.id.action_home_screen_to_add_toke_screen)
        }
    }

}