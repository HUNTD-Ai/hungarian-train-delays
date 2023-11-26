package com.huntdai.hungariantraindelays.ui.prediction.timetable

import com.huntdai.hungariantraindelays.ui.prediction.timetable.models.TrainDeparture

sealed class TimetableUIState  {
    object Initial : TimetableUIState()
    data class Loaded(val trains: List<TrainDeparture>) : TimetableUIState()
    object Loading : TimetableUIState()
    object Error : TimetableUIState()
}