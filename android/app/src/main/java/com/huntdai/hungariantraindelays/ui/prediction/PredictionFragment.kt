package com.huntdai.hungariantraindelays.ui.prediction

import android.app.DatePickerDialog
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
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.huntdai.hungariantraindelays.R
import com.huntdai.hungariantraindelays.databinding.FragmentPredictionBinding
import com.huntdai.hungariantraindelays.ui.models.RouteDestinationMap
import com.huntdai.hungariantraindelays.ui.prediction.date_picker.DatePickerFragment
import com.huntdai.hungariantraindelays.ui.stats.mean_per_route.MeanPerRouteUIState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PredictionFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private val viewModel: PredictionViewModel by viewModels()
    private lateinit var startDestinationSpinner : Spinner
    private lateinit var endDestinationSpinner : Spinner
    private lateinit var trainNumber : TextView
    private lateinit var selectDateButton: Button
    private lateinit var selectTrainNumberButton: Button
    private lateinit var predictButton: Button


    private lateinit var routeDestinationMap: RouteDestinationMap

    companion object {
        const val DATE_SELECTED_KEY = "date_selected"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPredictionBinding.inflate(layoutInflater)
        selectTrainNumberButton = binding.selectTrainNumberButton
        selectDateButton = binding.selectDateButton
        predictButton = binding.predictButton
        startDestinationSpinner = binding.startdestinationSpinner
        endDestinationSpinner = binding.enddestinationSpinner
        trainNumber = binding.trainnumberText
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        showDelaysButton.setOnClickListener {
//            val selectedStartDestination = startDestinationSpinner.selectedItem.toString()
//            val selectedEndDestination = endDestinationSpinner.selectedItem.toString()
//            viewModel.loadDelays(startDestination = selectedStartDestination, endDestination = selectedEndDestination)
//        }

        selectDateButton.setOnClickListener {
            Log.d("DEMO", "NAV SELECT DATE")
            findNavController().navigate(R.id.action_predictionFragment_to_datePickerFragment)
        }

        selectTrainNumberButton.setOnClickListener {
            Log.d("DEMO", "nav timetable")
            val action = PredictionFragmentDirections.actionPredictionFragmentToTimetableFragment(route ="Szeged - Budapest-Nyugati", date = "2023-11-26")
            findNavController().navigate(action)
        }

        findNavController()
            .currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<DatePickerFragment.DatePickerResult>(DATE_SELECTED_KEY)
            ?.observe(viewLifecycleOwner) {
                Log.d("DEMO", "DATUM VALSZTVA" + it.toString())
            }

        lifecycleScope.launch {
            viewModel.uiState.collect {
                render(it)
            }
        }
        viewModel.initUiState()
    }

    private fun render(uiState: PredictionUIState) {
        Log.d("DEMO", uiState.toString())
        when (uiState) {
            is PredictionUIState.Initial -> {}
            is PredictionUIState.Loading -> {}
            is PredictionUIState.RoutesLoaded -> {
                uiState.routeDestinationMap?.let { setStartDestinationValues(routeDestinationMap = it) }
            }
            is PredictionUIState.TrainNumberSelected -> {
            }
            is PredictionUIState.Error -> {}
        }
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
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selectedStartDestination = parent?.getItemAtPosition(position).toString()
        Log.d("DEMO", "Lefutott: " + selectedStartDestination)
        setEndDestinationValues(selectedStartDestination)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }


}