package com.huntdai.hungariantraindelays.ui.stats.monthly_mean

import android.R.attr.data
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.huntdai.hungariantraindelays.data.network.models.Delay
import com.huntdai.hungariantraindelays.databinding.FragmentMonthlyMeanBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MonthlyMeanFragment : Fragment() {
    private val viewModel: MonthlyMeanViewModel by viewModels()
    private lateinit var chart : BarChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMonthlyMeanBinding.inflate(layoutInflater)
        chart = binding.chart
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

    private fun render(uiState: MonthlyMeanUIState) {
        when (uiState) {
            is MonthlyMeanUIState.Initial -> {}
            is MonthlyMeanUIState.Loaded -> {
                populateChart(uiState.delays)
            }
            is MonthlyMeanUIState.Loading -> {}
            is MonthlyMeanUIState.Error -> {}
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

    /*barChart.setDrawGridBackground(false);
    //remove the bar shadow, default false if not set
    barChart.setDrawBarShadow(false);
    //remove border of the chart, default false if not set
    barChart.setDrawBorders(false);

    //remove the description label text located at the lower right corner
    Description description = new Description();
    description.setEnabled(false);
    barChart.setDescription(description);

    //setting animation for y-axis, the bar will pop up from 0 to its value within the time we set
    barChart.animateY(1000);
    //setting animation for x-axis, the bar will pop up separately within the time we set
    barChart.animateX(1000);

    XAxis xAxis = barChart.getXAxis();
    //change the position of x-axis to the bottom
    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    //set the horizontal distance of the grid line
    xAxis.setGranularity(1f);
    //hiding the x-axis line, default true if not set
    xAxis.setDrawAxisLine(false);
    //hiding the vertical grid lines, default true if not set
    xAxis.setDrawGridLines(false);

    YAxis leftAxis = barChart.getAxisLeft();
    //hiding the left y-axis line, default true if not set
    leftAxis.setDrawAxisLine(false);

    YAxis rightAxis = barChart.getAxisRight();
    //hiding the right y-axis line, default true if not set
    rightAxis.setDrawAxisLine(false);

    Legend legend = barChart.getLegend();
    //setting the shape of the legend form to line, default square shape
    legend.setForm(Legend.LegendForm.LINE);
    //setting the text size of the legend
    legend.setTextSize(11f);
    //setting the alignment of legend toward the chart
    legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
    legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
    //setting the stacking direction of legend
    legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
    //setting the location of legend outside the chart, default false if not set
    legend.setDrawInside(false);*/
}