package com.huntdai.hungariantraindelays.ui.prediction.timetable.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.huntdai.hungariantraindelays.databinding.TrainDepartureItemBinding
import com.huntdai.hungariantraindelays.ui.prediction.timetable.models.TrainDeparture

class TimetableAdapter() :
    ListAdapter<TrainDeparture, TimetableAdapter.ViewHolder>(RouteComparator) {
    private lateinit var binding: TrainDepartureItemBinding
    private var trainDepartureList = emptyList<TrainDeparture>()

    private var itemClickListener: ((trainDeparture: TrainDeparture) -> Unit)? = null

    fun setOnItemClickListener(listener: ((trainDeparture: TrainDeparture) -> Unit)?) {
        itemClickListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = TrainDepartureItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val trainDeparture = trainDepartureList[position]

        holder.trainDeparture = trainDeparture
        holder.departureTime.text = trainDeparture.departureTime
        holder.arrivalTime.text = trainDeparture.arrivalTime
        holder.duration.text = trainDeparture.duration
        holder.trainNumber.text = trainDeparture.trainNumber
    }


    fun replaceList(newList: List<TrainDeparture>) {
        trainDepartureList = newList
        submitList(trainDepartureList)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val departureTime: TextView = binding.departuretimeText
        val arrivalTime: TextView = binding.arrivaltimeText
        val duration: TextView = binding.durationText
        val trainNumber: TextView = binding.trainnumberText
        val predictButton: Button = binding.predictButton
        var trainDeparture: TrainDeparture? = null

        init {
            predictButton.setOnClickListener {
                trainDeparture?.let { itemClickListener?.let { it1 -> it1(it) } }
            }
        }
    }
}