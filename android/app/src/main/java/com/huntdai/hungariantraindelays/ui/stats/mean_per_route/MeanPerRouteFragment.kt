package com.huntdai.hungariantraindelays.ui.stats.mean_per_route

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Spinner
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
import com.huntdai.hungariantraindelays.databinding.FragmentMeanPerRouteBinding
import com.huntdai.hungariantraindelays.ui.models.RouteDestinationMap
import com.huntdai.hungariantraindelays.ui.stats.highest_in_time_period.models.TimePeriod
import com.huntdai.hungariantraindelays.utils.getCurrentTextcolor
import com.huntdai.hungariantraindelays.utils.getMonthNameFromTimestamp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MeanPerRouteFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private val viewModel: MeanPerRouteViewModel by viewModels()
    private lateinit var startDestinationSpinner: Spinner
    private lateinit var endDestinationSpinner: Spinner
    private lateinit var showDelaysButton: Button
    private lateinit var chart: BarChart
    private lateinit var loadProgressBar: ProgressBar
    private lateinit var errorText: TextView

    private lateinit var fromGroup: LinearLayout
    private lateinit var toGroup: LinearLayout

    private lateinit var routeDestinationMap: RouteDestinationMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMeanPerRouteBinding.inflate(layoutInflater)
        chart = binding.chart
        showDelaysButton = binding.showDelaysButton
        startDestinationSpinner = binding.startdestinationSpinner
        endDestinationSpinner = binding.enddestinationSpinner
        loadProgressBar = binding.loadProgressbar
        errorText = binding.errorText
        fromGroup = binding.fromLayout
        toGroup = binding.toLayout
        //binding.title.text = "Mean delays per route"
        setupChart()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showDelaysButton.setOnClickListener {
            val selectedStartDestination = startDestinationSpinner.selectedItem.toString()
            val selectedEndDestination = endDestinationSpinner.selectedItem.toString()
            viewModel.loadDelays(
                startDestination = selectedStartDestination,
                endDestination = selectedEndDestination
            )
        }

        lifecycleScope.launch {
            viewModel.uiState.collect {
                render(it)
            }
        }
        viewModel.initUiState()
    }

    private fun render(uiState: MeanPerRouteUIState) {
        Log.d("DEMO", uiState.toString())
        when (uiState) {
            is MeanPerRouteUIState.Initial -> {
                hideInputElements()
                chart.visibility = View.GONE
                loadProgressBar.visibility = View.GONE
                errorText.visibility = View.GONE
            }

            is MeanPerRouteUIState.Loading -> {
                hideInputElements()
                chart.visibility = View.GONE
                loadProgressBar.visibility = View.VISIBLE
                errorText.visibility = View.GONE
            }

            is MeanPerRouteUIState.RoutesLoaded -> {
                showInputElements()
                chart.visibility = View.GONE
                loadProgressBar.visibility = View.GONE
                errorText.visibility = View.GONE
                uiState.routeDestinationMap?.let { setStartDestinationValues(routeDestinationMap = it) }
            }

            is MeanPerRouteUIState.DelaysLoaded -> {
                showInputElements()
                chart.visibility = View.VISIBLE
                loadProgressBar.visibility = View.GONE
                errorText.visibility = View.GONE
                populateChart(delays = uiState.delays)
            }

            is MeanPerRouteUIState.Error -> {
                hideInputElements()
                chart.visibility = View.GONE
                loadProgressBar.visibility = View.GONE
                errorText.visibility = View.VISIBLE
            }
        }
    }

    private fun hideInputElements() {
        showDelaysButton.visibility = View.GONE
        fromGroup.visibility = View.GONE
        toGroup.visibility = View.GONE
    }

    private fun showInputElements() {
        showDelaysButton.visibility = View.VISIBLE
        fromGroup.visibility = View.VISIBLE
        toGroup.visibility = View.VISIBLE
    }

    private fun setStartDestinationValues(routeDestinationMap: RouteDestinationMap) {
        Log.d("DEMO", "setStartDestinationValues: " + routeDestinationMap.toString())
        this.routeDestinationMap = routeDestinationMap
        val startdestinations = routeDestinationMap.startDestinations.keys.toMutableList()
        startdestinations.sort()
        //val startdestinationsList = startdestinations.toList()
        context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_item,
                startdestinations,
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                startDestinationSpinner.adapter = adapter
            }
        }
        startDestinationSpinner.onItemSelectedListener = this
    }

    private fun setEndDestinationValues(selectedStartDestination: String) {
        Log.d("DEMO", "setEndDestinationValues: " + selectedStartDestination.toString())
        val endDestinations =
            routeDestinationMap.startDestinations.getOrDefault(selectedStartDestination, listOf())
        context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_item,
                endDestinations,
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                endDestinationSpinner.adapter = adapter
            }
        }
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selectedStartDestination = parent?.getItemAtPosition(position).toString()
        Log.d("DEMO", "Lefutott: " + selectedStartDestination)
        setEndDestinationValues(selectedStartDestination)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    private fun populateChart(delays: List<Delay>) {
        val entries = ArrayList<BarEntry>()
        var labels = mutableListOf<String>()

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

        labels = labels.map { it.take(3) }.toMutableList()

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