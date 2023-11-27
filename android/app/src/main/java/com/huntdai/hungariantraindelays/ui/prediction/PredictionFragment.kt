package com.huntdai.hungariantraindelays.ui.prediction

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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.huntdai.hungariantraindelays.R
import com.huntdai.hungariantraindelays.databinding.FragmentPredictionBinding
import com.huntdai.hungariantraindelays.ui.models.RouteDestinationMap
import com.huntdai.hungariantraindelays.ui.prediction.date_picker.DatePickerFragment
import com.huntdai.hungariantraindelays.utils.combineRouteEnds
import com.huntdai.hungariantraindelays.utils.getTodaysDate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.properties.Delegates

@AndroidEntryPoint
class PredictionFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private val viewModel: PredictionViewModel by viewModels()
    private lateinit var startDestinationSpinner: Spinner
    private lateinit var endDestinationSpinner: Spinner
    private lateinit var selectedDate: TextView
    private lateinit var selectDateButton: Button
    private lateinit var selectTrainToPredictButton: Button

    private lateinit var loadProgressBar: ProgressBar
    private lateinit var errorText: TextView

    private lateinit var fromGroup: LinearLayout
    private lateinit var toGroup: LinearLayout
    private lateinit var dateGroup: LinearLayout

    private var yearSelected by Delegates.notNull<Int>()
    private var monthSelected by Delegates.notNull<Int>()
    private var daySelected by Delegates.notNull<Int>()


    private lateinit var routeDestinationMap: RouteDestinationMap

    companion object {
        const val DATE_SELECTED_KEY = "date_selected"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPredictionBinding.inflate(layoutInflater)
        startDestinationSpinner = binding.startdestinationSpinner
        endDestinationSpinner = binding.enddestinationSpinner
        selectDateButton = binding.selectDateButton
        selectedDate = binding.selectedDateText
        selectTrainToPredictButton = binding.selectTrainToPredictButton
        loadProgressBar = binding.loadProgressbar
        errorText = binding.errorText
        fromGroup = binding.fromLayout
        toGroup = binding.toLayout
        dateGroup = binding.dateLayout

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val todaysDate = getTodaysDate()
        yearSelected = todaysDate.get(Calendar.YEAR)
        monthSelected = todaysDate.get(Calendar.MONTH)
        daySelected = todaysDate.get(Calendar.DAY_OF_MONTH)
        val newSelectedDate = getDateString()
        selectedDate.text = newSelectedDate

        selectDateButton.setOnClickListener {
            Log.d("DEMO", "NAV SELECT DATE")
            findNavController().navigate(R.id.action_predictionFragment_to_datePickerFragment)
        }

        selectTrainToPredictButton.setOnClickListener {
            Log.d("DEMO", "nav timetable")
            val startDestination = startDestinationSpinner.selectedItem.toString()
            val endDestination = endDestinationSpinner.selectedItem.toString()
            val date = getTodaysDate()
            date.set(Calendar.YEAR, yearSelected)
            date.set(Calendar.MONTH, monthSelected)
            date.set(Calendar.DAY_OF_MONTH, daySelected)
            val timeInMillis = date.timeInMillis.toString()
            val action = PredictionFragmentDirections.actionPredictionFragmentToTimetableFragment(
                route = combineRouteEnds(
                    start = startDestination,
                    end = endDestination
                ), dateInMillis = timeInMillis
            )
            findNavController().navigate(action)
        }

        findNavController()
            .currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<DatePickerFragment.DatePickerResult>(DATE_SELECTED_KEY)
            ?.observe(viewLifecycleOwner) {
                Log.d("DEMO", "DATUM VALSZTVA" + it.toString())
                yearSelected = it.year
                monthSelected = it.month
                daySelected = it.dayOfMonth

                val newSelectedDate = getDateString()
                selectedDate.text = newSelectedDate
            }

        lifecycleScope.launch {
            viewModel.uiState.collect {
                render(it)
            }
        }
        viewModel.initUiState()
    }

    private fun hideInputElements() {
        selectTrainToPredictButton.visibility = View.GONE
        fromGroup.visibility = View.GONE
        toGroup.visibility = View.GONE
        dateGroup.visibility = View.GONE
    }

    private fun showInputElements() {
        selectTrainToPredictButton.visibility = View.VISIBLE
        fromGroup.visibility = View.VISIBLE
        toGroup.visibility = View.VISIBLE
        dateGroup.visibility = View.VISIBLE
    }

    private fun render(uiState: PredictionUIState) {
        Log.d("DEMO", uiState.toString())
        when (uiState) {
            is PredictionUIState.Initial -> {
                hideInputElements()
                loadProgressBar.visibility = View.GONE
                errorText.visibility = View.GONE
            }
            is PredictionUIState.Loading -> {
                hideInputElements()
                loadProgressBar.visibility = View.VISIBLE
                errorText.visibility = View.GONE
            }
            is PredictionUIState.Loaded -> {
                showInputElements()
                loadProgressBar.visibility = View.GONE
                errorText.visibility = View.GONE
                uiState.routeDestinationMap?.let { setStartDestinationValues(routeDestinationMap = it) }
            }

            is PredictionUIState.Error -> {
                hideInputElements()
                loadProgressBar.visibility = View.GONE
                errorText.visibility = View.VISIBLE
            }
        }
    }

    private fun setStartDestinationValues(routeDestinationMap: RouteDestinationMap) {
        Log.d("DEMO", "setStartDestinationValues: " + routeDestinationMap.toString())
        this.routeDestinationMap = routeDestinationMap
        val startdestinations = routeDestinationMap.startDestinations.keys.toMutableList()
        startdestinations.sort()
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

    private fun getDateString() : String{
        return yearSelected.toString() + "-" + (monthSelected + 1).toString() + "-" + daySelected.toString()
    }


}