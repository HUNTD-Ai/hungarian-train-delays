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
import android.widget.Spinner
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.huntdai.hungariantraindelays.data.network.stats.models.Delay
import com.huntdai.hungariantraindelays.databinding.FragmentMeanPerRouteBinding
import com.huntdai.hungariantraindelays.ui.models.RouteDestinationMap
import com.huntdai.hungariantraindelays.ui.stats.highest_in_time_period.models.TimePeriod
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MeanPerRouteFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private val viewModel: MeanPerRouteViewModel by viewModels()
    private lateinit var startDestinationSpinner : Spinner
    private lateinit var endDestinationSpinner : Spinner
    private lateinit var showDelaysButton: Button
    private lateinit var chart : BarChart

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
        setupChart()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showDelaysButton.setOnClickListener {
            val selectedStartDestination = startDestinationSpinner.selectedItem.toString()
            val selectedEndDestination = endDestinationSpinner.selectedItem.toString()
            viewModel.loadDelays(startDestination = selectedStartDestination, endDestination = selectedEndDestination)
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
            is MeanPerRouteUIState.Initial -> {}
            is MeanPerRouteUIState.Loading -> {}
            is MeanPerRouteUIState.RoutesLoaded -> {
                uiState.routeDestinationMap?.let { setStartDestinationValues(routeDestinationMap = it) }
            }
            is MeanPerRouteUIState.DelaysLoaded -> {
                populateChart(delays = uiState.delays)
            }
            is MeanPerRouteUIState.Error -> {}
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

    private fun setStartDestinationValues(routeDestinationMap: RouteDestinationMap){
        Log.d("DEMO", "setStartDestinationValues: " + routeDestinationMap.toString())
        this.routeDestinationMap = routeDestinationMap
        val startdestinations = routeDestinationMap.startDestinations.keys.toList()
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

    private fun setEndDestinationValues(selectedStartDestination: String){
        Log.d("DEMO", "setEndDestinationValues: " + selectedStartDestination.toString())
        val endDestinations = routeDestinationMap.startDestinations.getOrDefault(selectedStartDestination, listOf())
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
        //enddestinationSpinner.onItemSelectedListener = this
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selectedStartDestination = parent?.getItemAtPosition(position).toString()
        Log.d("DEMO", "Lefutott: " + selectedStartDestination)
        setEndDestinationValues(selectedStartDestination)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

//    private class startDestinationSpinnerListener() : AdapterView.OnItemSelectedListener
//    {
//        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//        }
//
//        override fun onNothingSelected(parent: AdapterView<*>?) {}
//
//    }

}