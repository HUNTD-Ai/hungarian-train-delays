package com.huntdai.hungariantraindelays.ui.prediction.timetable.models

import com.huntdai.hungariantraindelays.ui.models.Route

data class TrainDeparture(
    val route: String,
    val departureTime : String,
    val arrivalTime : String,
    val duration : String,
    val trainNumber : String
)
