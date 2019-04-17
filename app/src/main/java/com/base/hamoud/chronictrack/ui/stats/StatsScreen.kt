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
import com.base.hamoud.chronictrack.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.floatingactionbutton.FloatingActionButton
import timber.log.Timber

class StatsScreen : Fragment() {

    private lateinit var viewModel: StatsViewModel

    private lateinit var weeklyTokesLineChart: LineChart

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
        val textColor = ContextCompat.getColor(context!!, R.color.colorPrimaryText)
        val des = Description().also {
            it.text = "this weeks tokes over 7 day period"
        }
        weeklyTokesLineChart = view.findViewById(R.id.stats_screen_weekly_tokes_trend)
        weeklyTokesLineChart.apply {
            setBackgroundColor(ContextCompat.getColor(activity!!, R.color.colorPrimary))
            setDrawGridBackground(false)
            xAxis?.position = XAxis.XAxisPosition.BOTTOM
            xAxis.labelCount
            xAxis?.textColor = textColor
            legend.isEnabled = false
            axisRight?.textColor = textColor
            axisLeft?.textColor = textColor
            description = des
        }
        weeklyTokesLineChart.xAxis.addLimitLine(generateLimitlineFor(0f, "M"))
        weeklyTokesLineChart.xAxis.addLimitLine(generateLimitlineFor(2f, "W"))
        weeklyTokesLineChart.xAxis.addLimitLine(generateLimitlineFor(4f, "F"))
        weeklyTokesLineChart.xAxis.addLimitLine(generateLimitlineFor(6f, "S"))

        // observe
        observeOnChartData()

        // triggers
        viewModel.getThisWeeksTokesData()

    }

    private fun generateLimitlineFor(limit: Float, label: String): LimitLine {
        val textColor = ContextCompat.getColor(context!!, R.color.colorPrimaryText)
        return LimitLine(limit, label).apply {
            this.lineWidth = 1f
            this.lineColor = textColor
            this.labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
            this.textSize = 8f
            this.textColor = textColor
        }
    }

    private fun observeOnChartData() {
        viewModel.weeksTokesData.observe(viewLifecycleOwner, Observer {
            Timber.i("Weeks Tokes: $it")
            if (!it.isNullOrEmpty()) {
                // todo
                val dataSet = LineDataSet(it, "Weeks Tokes")
                Timber.i("DataSet: ${dataSet.toSimpleString()}")
                val colorAccent = ContextCompat.getColor(context!!, R.color.colorAccent)
                dataSet.color = colorAccent
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
        val addTokeBtn = view?.findViewById<FloatingActionButton>(R.id.stats_screen_add_toke_btn)
        addTokeBtn?.setOnClickListener {
            findNavController().navigate(R.id.action_home_screen_to_add_toke_screen)
        }
    }

}