package com.huntdai.hungariantraindelays.ui.stats

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.huntdai.hungariantraindelays.R
import com.huntdai.hungariantraindelays.databinding.FragmentStatsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StatsFragment : Fragment() {

    private lateinit var meanButton: Button
    private lateinit var totalButton: Button
    private lateinit var highestButton: Button
    private lateinit var meanPerRouteButton: Button

    private val viewModel: StatsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentStatsBinding.inflate(layoutInflater)
        meanButton = binding.meanButton
        totalButton = binding.totalButton
        highestButton= binding.highestButton
        meanPerRouteButton = binding.meanPerRouteButton
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        meanButton.setOnClickListener {
            findNavController().navigate(R.id.action_statsFragment_to_monthlyMeanFragment)
        }

        totalButton.setOnClickListener {
            findNavController().navigate(R.id.action_statsFragment_to_monthlySumFragment)
        }

        highestButton.setOnClickListener {
            findNavController().navigate(R.id.action_statsFragment_to_highestInTimePeriodFragment)
        }

        meanPerRouteButton.setOnClickListener {
            findNavController().navigate(R.id.action_statsFragment_to_meanPerRouteFragment)
        }

        lifecycleScope.launch {
            viewModel.uiState.collect {
                render(it)
            }
        }
        viewModel.initUiState()
    }

    private fun render(uiState: StatsUIState) {
        when (uiState) {
            is StatsUIState.Initial -> {}
            is StatsUIState.Demo -> {
                Log.d("DEMO", uiState.routes.toString())
            }
            is StatsUIState.MonthlyHighestSelected -> {}
            is StatsUIState.MonthlyMeanSelected -> {}
            is StatsUIState.MonthlySumSelected -> {}
        }
    }

}