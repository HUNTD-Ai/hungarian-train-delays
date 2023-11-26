package com.huntdai.hungariantraindelays.ui.prediction.timetable

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.huntdai.hungariantraindelays.databinding.FragmentTimetableBinding
import com.huntdai.hungariantraindelays.ui.prediction.timetable.adapter.TimetableAdapter
import com.huntdai.hungariantraindelays.ui.prediction.timetable.models.TrainDeparture
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class TimetableFragment : Fragment() {
    private lateinit var binding: FragmentTimetableBinding
    private lateinit var adapter: TimetableAdapter
    private val viewModel: TimetableViewModel by viewModels()
    private val args: TimetableFragmentArgs by navArgs()
    private lateinit var route: String
    private lateinit var dateInMillis: String
    private lateinit var dateString: String //2023-11-26 like format

    override fun onAttach(context: Context) {
        super.onAttach(context)
        binding = FragmentTimetableBinding.inflate(LayoutInflater.from(context))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        route = args.route
        dateInMillis = args.dateInMillis
        val date = Calendar.getInstance()
        date.timeInMillis = dateInMillis.toLong()
        dateString = date.get(Calendar.YEAR).toString() + "-" + date.get(Calendar.MONTH).toString() + "-" + date.get(Calendar.DAY_OF_MONTH).toString()

        Log.d("DEMO", "TIMETABLEINFO ATVEVE" + route + dateString)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        lifecycleScope.launch {
            viewModel.uiState.collect {
                render(it)
            }
        }
        viewModel.initUiState(route = route, departureDate = dateString)
    }

    private fun render(uiState: TimetableUIState) {
        Log.d("DEMO", "TIMETABLEINFO UISTATE" + uiState.toString())
        when (uiState) {
            is TimetableUIState.Initial -> {}
            is TimetableUIState.Loaded -> {
                adapter.replaceList(uiState.trains)
            }
            TimetableUIState.Error -> {}
            TimetableUIState.Loading -> {}
        }
    }


    private fun setupRecyclerView() {
        adapter = TimetableAdapter()
        adapter.setOnItemClickListener(this::onItemClick)

        val rv = binding.trainList
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = adapter
    }

    private fun onItemClick(trainDeparture: TrainDeparture) {
        val action = TimetableFragmentDirections.actionTimetableFragmentToPredictionResultFragment(
            route = trainDeparture.route,
            departureTime = trainDeparture.departureTime,
            trainNumber = trainDeparture.trainNumber,
            departureDateInMillis = dateInMillis
        )
        findNavController().navigate(action)
    }

}
