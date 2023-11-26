package com.huntdai.hungariantraindelays.ui.prediction.timetable.adapter

import androidx.recyclerview.widget.DiffUtil
import com.huntdai.hungariantraindelays.ui.prediction.timetable.models.TrainDeparture

object RouteComparator : DiffUtil.ItemCallback<TrainDeparture>() {
    override fun areItemsTheSame(oldItem: TrainDeparture, newItem: TrainDeparture): Boolean {
        return (oldItem.trainNumber == newItem.trainNumber && oldItem.departureTime == newItem.departureTime)
    }

    override fun areContentsTheSame(oldItem: TrainDeparture, newItem: TrainDeparture): Boolean {
        return (oldItem.trainNumber == newItem.trainNumber && oldItem.departureTime == newItem.departureTime)
    }
}