package com.huntdai.hungariantraindelays.ui.stats.monthly_total

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.huntdai.hungariantraindelays.R
import com.huntdai.hungariantraindelays.data.network.stats.models.Delay
import com.huntdai.hungariantraindelays.databinding.FragmentMonthlyMeanBinding
import com.huntdai.hungariantraindelays.databinding.FragmentMonthlyTotalBinding
import com.huntdai.hungariantraindelays.ui.stats.monthly_mean.MonthlyMeanUIState
import com.huntdai.hungariantraindelays.ui.stats.monthly_mean.MonthlyMeanViewModel
import com.huntdai.hungariantraindelays.utils.getCurrentTextcolor
import com.huntdai.hungariantraindelays.utils.getMonthNameFromTimestamp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MonthlyTotalFragment : Fragment() {
    private val viewModel: MonthlyTotalViewModel by viewModels()
    private lateinit var chart: BarChart
    private lateinit var loadProgressBar: ProgressBar
    private lateinit var errorText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMonthlyTotalBinding.inflate(layoutInflater)
        chart = binding.chart
        loadProgressBar = binding.loadProgressbar
        errorText = binding.errorText
        binding.title.text = "Monthly total delays"
        setupChart()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.uiState.collect {
                render(it)
            }
        }
        viewModel.initUiState()
    }

    private fun render(uiState: MonthlyTotalUIState) {
        when (uiState) {
            is MonthlyTotalUIState.Initial -> {
                chart.visibility = View.GONE
                loadProgressBar.visibility = View.GONE
                errorText.visibility = View.GONE
            }

            is MonthlyTotalUIState.Loaded -> {
                chart.visibility = View.VISIBLE
                loadProgressBar.visibility = View.GONE
                errorText.visibility = View.GONE
                populateChart(uiState.delays)
            }

            is MonthlyTotalUIState.Loading -> {
                chart.visibility = View.GONE
                loadProgressBar.visibility = View.VISIBLE
                errorText.visibility = View.GONE
            }

            is MonthlyTotalUIState.Error -> {
                chart.visibility = View.GONE
                loadProgressBar.visibility = View.GONE
                errorText.visibility = View.VISIBLE
            }
        }
    }

    private fun populateChart(delays: List<Delay>) {
        val entries = ArrayList<BarEntry>()
        val labels = mutableListOf<String>()

        for (i in delays.indices) {
            val barEntry = delays[i].delay?.let { BarEntry(i.toFloat(), it.toFloat()) }
            if (barEntry != null) {
                entries.add(barEntry)
                val monthName = delays[i].timestamp?.let { getMonthNameFromTimestamp(it) }
                if (monthName != null) {
                    labels.add(monthName)
                }
            }
        }

        val barDataSet = BarDataSet(entries, "Minutes")
        barDataSet.color = ContextCompat.getColor(requireContext(), R.color.primary);
        barDataSet.valueTextColor = getCurrentTextcolor(requireContext())

        val description = Description()
        description.isEnabled = false
        chart.description = description

        val data = BarData(barDataSet)
        chart.data = data

        // Customize X-axis with month names as labels
        val xAxis = chart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true

        chart.invalidate()
    }

    private fun setupChart() {
        val rightAxis = chart.axisRight
        rightAxis.setDrawLabels(false)

        val textColor = getCurrentTextcolor(requireContext())

        val xAxis = chart.xAxis
        xAxis.textColor = textColor

        val leftAxis = chart.axisLeft
        leftAxis.textColor = textColor

        chart.description.textColor = textColor
        chart.legend.textColor = textColor

        chart.animateY(1000);
        chart.animateX(1000);
    }
}