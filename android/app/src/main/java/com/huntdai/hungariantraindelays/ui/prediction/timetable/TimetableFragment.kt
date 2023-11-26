package com.huntdai.hungariantraindelays.ui.prediction.timetable

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.huntdai.hungariantraindelays.databinding.FragmentTimetableBinding
import com.huntdai.hungariantraindelays.ui.prediction.PredictionFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.Serializable
import java.util.Calendar
@AndroidEntryPoint
class TimetableFragment : AppCompatDialogFragment() {
    private lateinit var binding: FragmentTimetableBinding
    //private lateinit var adapter: TimetableAdapter
    private val viewModel: TimetableViewModel by viewModels()
    private val args: TimetableFragmentArgs by navArgs()
    private lateinit var route: String
    private lateinit var date: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, 0)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        binding = FragmentTimetableBinding.inflate(LayoutInflater.from(context))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        route = args.route
        date = args.date

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setupRecyclerView()

        lifecycleScope.launch {
            viewModel.uiState.collect {
                render(it)
            }
        }
        viewModel.initUiState(route = route, departureDate = date)
    }

    private fun render(uiState: TimetableUIState) {
//        when (uiState) {
//            is Initial -> {}
//            is RoutesLoaded -> {
//                adapter.replaceList(uiState.routes)
//            }
//        }
    }


//    private fun setupRecyclerView() {
//        adapter = HomeAdapter()
//        adapter.setOnItemClickListener(this::onItemClick)
//
//        val rv = binding.homeList
//        rv.layoutManager = LinearLayoutManager(context)
//        rv.adapter = adapter
//    }
//
//    private fun onItemClick(route: Route) {
//        val bundle = bundleOf("userRouteId" to route.id.toString())
//        findNavController().navigate(
//            R.id.map,
//            bundle
//        )
//    }

}
