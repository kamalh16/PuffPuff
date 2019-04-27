package com.base.hamoud.chronictrack.ui.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.base.hamoud.chronictrack.utils.CustomDecimalFormatter
import com.base.hamoud.chronictrack.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import timber.log.Timber

class StatsScreen : Fragment() {

    private lateinit var viewModel: StatsViewModel

    private lateinit var weeklyTokesLineChart: BarChart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.screen_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(StatsViewModel::class.java)

        // prepare ui
        prepareView()
        prepareWeeklyTokesChart(view)

        // observe
        observeOnChartData()

        // triggers
        viewModel.getThisWeeksTokesData()

    }

    private fun prepareWeeklyTokesChart(view: View) {
        weeklyTokesLineChart = view.findViewById(R.id.stats_screen_weekly_tokes_trend)

        // chart description
        val barChatDescription = Description().also { it.isEnabled = false }
        // x-axis value formatter
        val daysArr = arrayListOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        val xAxisFormatter = IndexAxisValueFormatter(daysArr)

        val textColor = ContextCompat.getColor(context!!, R.color.colorPrimaryText)

        weeklyTokesLineChart.apply {
            this.legend.isEnabled = false
            // yAxis
            this.axisRight?.isEnabled = false
            this.axisLeft?.setDrawLabels(false)
            this.axisLeft?.setDrawAxisLine(false)
            this.axisLeft?.setDrawGridLines(false)
            // chart
            this.description = barChatDescription
            this.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.colorPrimary))
            this.setDrawGridBackground(false)
            this.setDrawMarkers(false)
            this.setDrawValueAboveBar(true)
            this.setPinchZoom(false)
            this.setFitBars(true)
            // xAxis
            this.xAxis?.position = XAxis.XAxisPosition.BOTTOM
            this.xAxis?.textColor = textColor
            this.xAxis?.granularity = 1f
            this.xAxis?.valueFormatter = xAxisFormatter
            this.xAxis?.setDrawGridLines(false)
            this.xAxis?.setDrawAxisLine(false)
        }
    }

    private fun observeOnChartData() {
        viewModel.weeksTokesData.observe(viewLifecycleOwner, Observer {
            Timber.i("Weeks Tokes: $it")
            if (!it.isNullOrEmpty()) {
                val colorAccent = ContextCompat.getColor(context!!, R.color.colorAccent)
                val dataSet = BarDataSet(it, "Weeks Tokes")


                dataSet.apply {
                    this.color = colorAccent
                    this.valueTextColor = colorAccent
                    this.barBorderWidth = 0.9f
                    this.valueFormatter = CustomDecimalFormatter()
                }
                Timber.i("DataSet: ${dataSet.toSimpleString()}")

                val barChartData = BarData(dataSet)
                weeklyTokesLineChart.data = barChartData
                weeklyTokesLineChart.invalidate()
            }
        })

    }

    private fun prepareView() {
        prepareAddTokeBtn()
    }

    private fun prepareAddTokeBtn() {
        val addTokeBtn = view?.findViewById<FloatingActionButton>(R.id.stats_screen_add_toke_btn)
        addTokeBtn?.setOnClickListener {
            findNavController().navigate(R.id.action_home_screen_to_add_toke_screen)
        }
    }
}