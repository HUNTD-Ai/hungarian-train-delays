package com.huntdai.hungariantraindelays.ui.prediction.prediction_result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.huntdai.hungariantraindelays.R
import com.huntdai.hungariantraindelays.databinding.FragmentPredictionResultBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class PredictionResultFragment : Fragment() {

    private lateinit var delaytext: TextView
    private lateinit var delayCauseLabelText: TextView
    private lateinit var delayCauseText: TextView

    private lateinit var liveInfoAvailableText: TextView
    private lateinit var liveDelay: TextView
    private lateinit var liveCause: TextView

    private lateinit var routeText: TextView
    private lateinit var trainNumberText: TextView

    private lateinit var anotherPredictionButton: Button

    private lateinit var loadProgressBar: ProgressBar
    private lateinit var errorText: TextView

    private lateinit var predictionGroup: LinearLayout
    private lateinit var infoGroup: LinearLayout

    private val viewModel: PredictionResultViewModel by viewModels()

    private val args: PredictionResultFragmentArgs by navArgs()
    private lateinit var route: String
    private lateinit var departureTime: String
    private lateinit var departureDateInMillis: String
    private lateinit var trainNumber: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPredictionResultBinding.inflate(layoutInflater)
        delaytext = binding.delayText
        delayCauseLabelText = binding.delayCauseLabelText
        delayCauseText = binding.delayCauseText

        liveInfoAvailableText = binding.liveInfoAvailable
        liveDelay = binding.liveDelayTimeText
        liveCause = binding.liveDelayCauseText

        routeText = binding.routeText
        trainNumberText = binding.trainnumberText

        loadProgressBar = binding.loadProgressbar
        errorText = binding.errorText
        predictionGroup = binding.predictionLayout
        infoGroup = binding.infoLayout

        anotherPredictionButton = binding.anotherPredictionButton
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        route = args.route
        departureTime = args.departureTime
        trainNumber = args.trainNumber
        departureDateInMillis = args.departureDateInMillis

        anotherPredictionButton.setOnClickListener {
            findNavController().popBackStack()
        }

        lifecycleScope.launch {
            viewModel.uiState.collect {
                render(it)
            }
        }
        viewModel.initUiState(
            route = route,
            departureTime = departureTime,
            trainNumber = trainNumber,
            departureDateInMillis = departureDateInMillis
        )
    }

    private fun render(uiState: PredictionResultUIState) {
        when (uiState) {
            is PredictionResultUIState.Initial -> {
                hideElements()
                loadProgressBar.visibility = View.GONE
                errorText.visibility = View.GONE
            }

            PredictionResultUIState.Error -> {
                hideElements()
                loadProgressBar.visibility = View.GONE
                errorText.visibility = View.VISIBLE
            }

            is PredictionResultUIState.Loaded -> {
                renderTexts(uiState)
                showElements()
                loadProgressBar.visibility = View.GONE
                errorText.visibility = View.GONE
            }

            PredictionResultUIState.Loading -> {
                hideElements()
                loadProgressBar.visibility = View.VISIBLE
                errorText.visibility = View.GONE
            }
        }
    }


    private fun renderTexts(uiState: PredictionResultUIState.Loaded) {
        val delayScore = uiState.delayScore
        val delayLabel = uiState.delayLabel

        val textColor: Int
        var delayCauseVisible = false
        if (delayScore < 0.8f) {
            textColor = ContextCompat.getColor(requireContext(), R.color.orange_pred)
            delayCauseVisible = true

        } else if (delayLabel == 1) {
            textColor = ContextCompat.getColor(requireContext(), R.color.red_pred)
            delayCauseVisible = true
        } else {
            textColor = ContextCompat.getColor(requireContext(), R.color.green_pred)
            delayCauseVisible = false
        }

        val delayText = if (delayLabel == 1) {
            "Likely more than 5 minutes (" + createPercentageString(delayScore) + " confident)"
        } else {
            "Likely less than 5 minutes (" + createPercentageString(delayScore) + " confident)"
        }

        delaytext.text = delayText
        delaytext.setTextColor(textColor)

        if (delayCauseVisible) {
            delayCauseLabelText.visibility = View.VISIBLE
            delayCauseText.visibility = View.VISIBLE

            val text =
                uiState.delayCause + "(" + createPercentageString(uiState.delayCauseScore) + " confident)"
            delayCauseText.text = text
            delayCauseText.setTextColor(textColor)
        } else {
            delayCauseLabelText.visibility = View.GONE
            delayCauseText.visibility = View.GONE
        }

        val liveInfoAvailable = (uiState.liveDelayCause != null) and (uiState.liveDelayTime != null)
        if(liveInfoAvailable){
            val text = "Live data available"
            liveInfoAvailableText.text = text

            liveDelay.visibility = View.VISIBLE
            liveCause.visibility = View.VISIBLE

            liveDelay.text = uiState.liveDelayTime
            liveCause.text = uiState.liveDelayCause
        }
        else{
            val text = "Live data not available"
            liveInfoAvailableText.text = text

            liveDelay.visibility = View.GONE
            liveCause.visibility = View.GONE
        }

        routeText.text = route
        trainNumberText.text = trainNumber
    }

    private fun hideElements() {
        anotherPredictionButton.visibility = View.GONE
        predictionGroup.visibility = View.GONE
        infoGroup.visibility = View.GONE
    }

    private fun showElements() {
        anotherPredictionButton.visibility = View.VISIBLE
        predictionGroup.visibility = View.VISIBLE
        infoGroup.visibility = View.VISIBLE
    }

    private fun createPercentageString(score: Double): String {
        val percentage = score * 100

        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        numberFormat.maximumFractionDigits = 2
        numberFormat.minimumFractionDigits = 2
        val formattedPercentage = numberFormat.format(percentage)
        return "$formattedPercentage %"
    }


}