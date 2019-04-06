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
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.enums.HoverMode
import com.anychart.enums.MarkerType
import com.anychart.enums.TooltipDisplayMode
import com.anychart.graphics.vector.SolidFill
import com.anychart.graphics.vector.text.HAlign
import com.base.hamoud.chronictrack.R
import com.base.hamoud.chronictrack.data.entity.User
import timber.log.Timber

class HomeScreen : Fragment() {

    private var loggedInUser: User? = null
    private lateinit var viewModel: HomeViewModel

    private var tokeCountView: TextView? = null
    private var tokeTimerChronometer: Chronometer? = null
    private var todaysTokesTrendChartView: AnyChartView? = null

    private lateinit var todaysTokesData: List<DataEntry>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        // prepare ui
        prepareTodaysTokeCountView()

        // observe
        observeOnUserLoggedInLive()
        observeOnGetUserTokesCountLive()
        observeOnUserLastTokeTodayLive()
        observeOnTodayTokesScatterData()

        // triggers
        viewModel.refreshTokesTotalCount()
        viewModel.getTodaysTokesScatterData()

        // prepare chart
        todaysTokesTrendChartView = view.findViewById<AnyChartView>(R.id.home_screen_todays_tokes_trend)
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

    private fun observeOnTodayTokesScatterData() {
        viewModel.todayTokesScatterData.observe(this, Observer {
            Timber.i("$it")
            if (it != null) {
                todaysTokesData = it

                val scatter = AnyChart.scatter()
                scatter.apply {
                    animation(true)
                    title("Todays Tokes")
                    xScale()
                        .minimum(7)
                        .maximum(7)
                    xAxis(0).title("Hour of day")
                    yScale()
                        .minimum(0)
                        .maximum(59)
                    yAxis(0).title("Minute in Hour")
                    interactivity()
                        .hoverMode(HoverMode.BY_SPOT)
                        .spotRadius(20)
                    tooltip()
                        .displayMode(TooltipDisplayMode.UNION)
                }


                val marker = scatter.marker(todaysTokesData)
                marker.apply {
                    type(MarkerType.TRIANGLE_UP)
                        .size(4)
                    hovered()
                        .size(7)
                        .fill(SolidFill("green", 1))
                        .stroke("anychart.color.darken(green)")
                    tooltip()
                        .hAlign(HAlign.START)
                        .format("Toke at: %X:%Y")
                }
                todaysTokesTrendChartView?.setChart(scatter)
            }
        })
    }

    private fun prepareTodaysTokeCountView() {
        tokeCountView = view?.findViewById(R.id.home_screen_todays_toke_count)
        tokeTimerChronometer = view?.findViewById(R.id.home_screen_last_hit_chronometer)
    }

}