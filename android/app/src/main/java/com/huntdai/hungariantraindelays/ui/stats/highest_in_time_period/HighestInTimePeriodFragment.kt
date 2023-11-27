package com.huntdai.hungariantraindelays.ui.stats.highest_in_time_period

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
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
import com.huntdai.hungariantraindelays.databinding.FragmentHighestInTimePeriodBinding
import com.huntdai.hungariantraindelays.ui.stats.highest_in_time_period.models.TimePeriod
import com.huntdai.hungariantraindelays.utils.fromInt
import com.huntdai.hungariantraindelays.utils.getCurrentTextcolor
import com.huntdai.hungariantraindelays.utils.getMonthAndDayNameFromTimestamp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HighestInTimePeriodFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private val viewModel: HighestInTimePeriodViewModel by viewModels()
    private lateinit var chart : BarChart
    private lateinit var spinner : Spinner
    private lateinit var loadProgressBar: ProgressBar
    private lateinit var errorText: TextView

    private lateinit var delays: List<Delay>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHighestInTimePeriodBinding.inflate(layoutInflater)
        chart = binding.chart
        spinner = binding.timePeriodSpinner
        loadProgressBar = binding.loadProgressbar
        errorText = binding.errorText
        //binding.title.text = "Highest delays"
        setupChart()
        setupSpinner()
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

    private fun render(uiState: HighestInTimePeriodUIState) {
        when (uiState) {
            is HighestInTimePeriodUIState.Initial -> {
                spinner.visibility = View.GONE
                chart.visibility = View.GONE
                loadProgressBar.visibility = View.GONE
                errorText.visibility = View.GONE
            }

            is HighestInTimePeriodUIState.Loaded -> {
                spinner.visibility = View.VISIBLE
                chart.visibility = View.VISIBLE
                loadProgressBar.visibility = View.GONE
                errorText.visibility = View.GONE
                delays = uiState.delays
            }

            is HighestInTimePeriodUIState.Loading -> {
                spinner.visibility = View.GONE
                chart.visibility = View.GONE
                loadProgressBar.visibility = View.VISIBLE
                errorText.visibility = View.GONE
            }

            is HighestInTimePeriodUIState.Error -> {
                spinner.visibility = View.GONE
                chart.visibility = View.GONE
                loadProgressBar.visibility = View.GONE
                errorText.visibility = View.VISIBLE
            }
        }
    }

    private fun setupSpinner(){
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.time_period_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }
        }
        spinner.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.d("DEMO", "Lefutott: " + fromInt<TimePeriod>(position).toString())
        when(val selectedTimePeriod = fromInt<TimePeriod>(position)) {
            TimePeriod.PAST_WEEK -> {populateChart(delays = delays, selectedTimePeriod = selectedTimePeriod)}
            TimePeriod.PAST_MONTH -> {populateChart(delays = delays, selectedTimePeriod = selectedTimePeriod)}
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    private fun populateChart(delays: List<Delay>, selectedTimePeriod: TimePeriod) {
        val delaysList = when(selectedTimePeriod){
            TimePeriod.PAST_WEEK -> {delays.takeLast(7)}
            TimePeriod.PAST_MONTH -> {delays}
        }

        val entries = ArrayList<BarEntry>()
        val labels = mutableListOf<String>()

        for (i in delaysList.indices) {
            val barEntry = delaysList[i].delay?.let { BarEntry(i.toFloat(), it.toFloat()) }
            if (barEntry != null) {
                entries.add(barEntry)
                val name = delaysList[i].timestamp?.let { getMonthAndDayNameFromTimestamp(it) }
                if (name != null) {
                    labels.add(name)
                }
            }
        }

        val barDataSet = BarDataSet(entries, "Minutes")
        barDataSet.color = ContextCompat.getColor(requireContext(), R.color.primary)
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

