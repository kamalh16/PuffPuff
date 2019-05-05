package com.base.hamoud.puffpuff.ui.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.base.hamoud.puffpuff.utils.CustomDecimalFormatter
import com.base.hamoud.puffpuff.R
import com.base.hamoud.puffpuff.data.model.Tools
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import timber.log.Timber

class StatsScreen : Fragment() {

    private lateinit var viewModel: StatsViewModel

    private lateinit var weeklyTokesBarChart: BarChart
    private lateinit var weeklyToolsUsedPieChart: PieChart

    private lateinit var pieChartColorsList: IntArray

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
        pieChartColorsList = intArrayOf(R.color.pieChart1, R.color.pieChart2,
            R.color.pieChart3, R.color.pieChart4, R.color.pieChart5)
        // prepare ui
        prepareView()
        prepareWeeklyTokesChart()
        prepareWeeklyToolsUsedPieChart()
        // observe
        observeOnChartData()
        observeOnWeekTokesCount()
        observeOnWeeksToolsUsedLive()
        // triggers
        viewModel.getThisWeeksTokesData()
    }

    private fun prepareWeeklyToolsUsedPieChart() {
        // todo
        weeklyToolsUsedPieChart = view?.findViewById(R.id.stats_screen_weekly_tools_pie_chart)!!
        // chart description
        val blankDescription = Description().also { it.isEnabled = false }
        val colorPrimaryText = ContextCompat.getColor(context!!, R.color.colorPrimaryText)
        val colorPrimary = ContextCompat.getColor(context!!, R.color.colorPrimary)

        weeklyToolsUsedPieChart.apply {
            this.legend.isEnabled = false
            // chart
            this.description = blankDescription
            this.setBackgroundColor(colorPrimary)
            this.holeRadius = 20f
            this.transparentCircleRadius = 35f
            this.setCenterTextColor(colorPrimaryText)
            this.setEntryLabelColor(colorPrimaryText)
        }

    }

    private fun observeOnWeeksToolsUsedLive() {
        // todo
        viewModel.weeksToolsUsedLive.observe(this, Observer { toolsCountArr ->
            Timber.i("Weeks Tokes: $toolsCountArr")
            if (toolsCountArr.isNotEmpty()) {
                val colorAccent = ContextCompat.getColor(context!!, R.color.colorAccent)
                val colorPrimaryText = ContextCompat.getColor(context!!, R.color.colorPrimaryText)
                val toolsEntries = arrayListOf<PieEntry>()
                toolsCountArr.forEachIndexed { index, count ->
                    if (count != 0) {
                        val pieEntry = PieEntry(count.toFloat(), Tools.values()[index].name)
                        toolsEntries.add(pieEntry)
                    }
                }
                val dataSet = PieDataSet(toolsEntries, "Weeks Tools Used")
                dataSet.apply {
                    this.valueTextColor = colorPrimaryText
                    this.valueFormatter = CustomDecimalFormatter()
                    this.valueTextSize = 10f
                    this.colors = pieChartColorsList.toMutableList()
                }
                Timber.i("DataSet: ${dataSet.entryCount}")
                val chartData = PieData(dataSet)
                weeklyToolsUsedPieChart.data = chartData
                weeklyToolsUsedPieChart.invalidate()
            }
        })
    }

    private fun prepareWeeklyTokesChart() {
        weeklyTokesBarChart = view?.findViewById(R.id.stats_screen_weekly_tokes_bar_chart)!!

        // chart description
        val blankDescription = Description().also { it.isEnabled = false }
        // x-axis value formatter
        val daysArr = arrayListOf("Mon", "Tue", "Wed", "Thu",
            "Fri", "Sat", "Sun")
        val xAxisFormatter = IndexAxisValueFormatter(daysArr)

        val colorPrimaryText = ContextCompat.getColor(context!!, R.color.colorPrimaryText)
        val colorPrimary = ContextCompat.getColor(context!!, R.color.colorPrimary)

        weeklyTokesBarChart.apply {
            this.legend.isEnabled = false
            // yAxis
            this.axisRight?.isEnabled = false
            this.axisLeft?.setDrawLabels(false)
            this.axisLeft?.setDrawAxisLine(false)
            this.axisLeft?.setDrawGridLines(false)
            // chart
            this.description = blankDescription
            this.setBackgroundColor(colorPrimary)
            this.setDrawGridBackground(false)
            this.setDrawValueAboveBar(true)
            this.setFitBars(true)
            // xAxis
            this.xAxis?.position = XAxis.XAxisPosition.BOTTOM
            this.xAxis?.textColor = colorPrimaryText
            this.xAxis?.axisLineColor = colorPrimaryText
            this.xAxis?.granularity = 1f
            this.xAxis?.valueFormatter = xAxisFormatter as ValueFormatter?
            this.xAxis?.setDrawGridLines(false)
            this.xAxis?.setDrawAxisLine(false)
        }
    }

    private fun observeOnChartData() {
        viewModel.weeksTokesDataLive.observe(viewLifecycleOwner, Observer {
            Timber.i("Weeks Tokes: $it")
            if (!it.isNullOrEmpty()) {
                val colorAccent = ContextCompat.getColor(context!!, R.color.colorAccent)
                val colorPrimaryText = ContextCompat.getColor(context!!, R.color.colorPrimaryText)
                val dataSet = BarDataSet(it, "Weeks Tokes")

                dataSet.apply {
                    this.color = colorAccent
                    this.barBorderColor = colorPrimaryText
                    this.valueTextColor = colorPrimaryText
                    this.barBorderWidth = 0.9f
                    this.valueFormatter = CustomDecimalFormatter()
                    this.valueTextSize = 10f
                }
                Timber.i("DataSet: ${dataSet.entryCount}")
                setWeekTokesCountTextView(dataSet.entryCount)
                val barChartData = BarData(dataSet)
                weeklyTokesBarChart.data = barChartData
                weeklyTokesBarChart.invalidate()
            }
        })
    }

    private fun observeOnWeekTokesCount() {
        viewModel.weeksTokesCountLive.observe(viewLifecycleOwner, Observer {
            setWeekTokesCountTextView(it)
        })
    }
    private fun prepareView() {
        prepareAddTokeBtn()
    }

    private fun setWeekTokesCountTextView(count: Int) {
        val weekTokesCountTv = view?.findViewById<TextView>(R.id.stats_screen_weeks_toke_count)
        weekTokesCountTv?.text = count.toString()
    }

    private fun prepareAddTokeBtn() {
        val addTokeBtn = view?.findViewById<FloatingActionButton>(R.id.stats_screen_add_toke_btn)
        addTokeBtn?.setOnClickListener {
            findNavController().navigate(R.id.action_home_screen_to_add_toke_screen)
        }
    }
}