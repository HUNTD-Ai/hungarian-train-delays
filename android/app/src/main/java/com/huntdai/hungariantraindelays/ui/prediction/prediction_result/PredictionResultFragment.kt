package com.huntdai.hungariantraindelays.ui.prediction.prediction_result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.huntdai.hungariantraindelays.databinding.FragmentPredictionResultBinding
import com.huntdai.hungariantraindelays.ui.prediction.timetable.TimetableFragmentArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PredictionResultFragment : Fragment() {

    private lateinit var delay: TextView
    private lateinit var delayCause: TextView
    private lateinit var liveDelay: TextView
    private lateinit var liveCause: TextView
    private lateinit var anotherPredictionButton: Button

    private val viewModel: PredictionResultViewModel by viewModels()

    private val args: PredictionResultFragmentArgs by navArgs()
    private lateinit var route: String
    private lateinit var departureTime: String
    private lateinit var trainNumber: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPredictionResultBinding.inflate(layoutInflater)
        delay = binding.predictedDelayTimeText
        delayCause = binding.predictedDelayTimeText
        liveDelay = binding.liveDelayTimeText
        liveCause = binding.liveDelayCauseText
        anotherPredictionButton = binding.anotherPredictionButton
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        route = args.route
        departureTime = args.departureTime
        trainNumber = args.trainNumber

        anotherPredictionButton.setOnClickListener {
            findNavController().popBackStack()
        }

        lifecycleScope.launch {
            viewModel.uiState.collect {
                render(it)
            }
        }
        viewModel.initUiState(route = route, departureTime = departureTime, trainNumber = trainNumber)
    }

    private fun render(uiState: PredictionResultUIState) {
        when (uiState) {
            is PredictionResultUIState.Initial -> {}
            PredictionResultUIState.Error -> {}
            is PredictionResultUIState.Loaded -> {
                delay.text = uiState.delayTime
                delayCause.text = uiState.delayCause
                liveDelay.text = uiState.liveDelayTime
                liveCause.text = uiState.liveDelayCause
            }
            PredictionResultUIState.Loading -> {}
        }
    }

}