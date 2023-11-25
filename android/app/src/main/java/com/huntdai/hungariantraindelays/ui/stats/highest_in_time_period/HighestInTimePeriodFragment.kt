package com.huntdai.hungariantraindelays.ui.stats.highest_in_time_period

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.huntdai.hungariantraindelays.R
import com.huntdai.hungariantraindelays.data.network.models.Delay
import com.huntdai.hungariantraindelays.databinding.FragmentHighestInTimePeriodBinding
import com.huntdai.hungariantraindelays.ui.stats.highest_in_time_period.models.TimePeriod
import com.huntdai.hungariantraindelays.utils.fromInt
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HighestInTimePeriodFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private val viewModel: HighestInTimePeriodViewModel by viewModels()
    private lateinit var chart : BarChart
    private lateinit var spinner : Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHighestInTimePeriodBinding.inflate(layoutInflater)
        chart = binding.chart
        spinner = binding.timePeriodSpinner
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
            is HighestInTimePeriodUIState.Initial -> {}
            is HighestInTimePeriodUIState.Loaded -> {
                populateChart(uiState.delays)
            }
            is HighestInTimePeriodUIState.Loading -> {}
            is HighestInTimePeriodUIState.Error -> {}
        }
    }

    private fun populateChart(delays: List<Delay>){
        val entries = ArrayList<BarEntry>()
        val title = "Title"

        for (i in delays.indices) {
            val barEntry = delays[i].delay?.let { BarEntry(i.toFloat(), it.toFloat()) }
            if (barEntry != null) {
                entries.add(barEntry)
            }
        }

        val barDataSet = BarDataSet(entries, title)

        val data = BarData(barDataSet)
        chart.data = data
        chart.invalidate()
    }

    private fun setupChart(){
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false)
        chart.setDrawBorders(false)

        val description =  Description()
        description.isEnabled = false
        chart.description = description

        chart.animateY(1000);
        chart.animateX(1000);
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
            TimePeriod.WEEK -> { viewModel.timePeriodChanged(selectedTimePeriod)}
            TimePeriod.MONTH -> { viewModel.timePeriodChanged(selectedTimePeriod)}
            TimePeriod.SIX_MONTHS -> { viewModel.timePeriodChanged(selectedTimePeriod)}
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}

