package com.huntdai.hungariantraindelays.ui.stats.highest_in_time_period

import com.huntdai.hungariantraindelays.data.network.models.Delay
import com.huntdai.hungariantraindelays.ui.stats.highest_in_time_period.models.TimePeriod

sealed class HighestInTimePeriodUIState(selectedTimePeriod: TimePeriod)  {
    data class Initial(val selectedTimePeriod: TimePeriod) : HighestInTimePeriodUIState(selectedTimePeriod = selectedTimePeriod)
    data class Loaded(val selectedTimePeriod : TimePeriod, val delays: List<Delay>) : HighestInTimePeriodUIState(selectedTimePeriod = selectedTimePeriod)
    data class Loading(val selectedTimePeriod: TimePeriod) : HighestInTimePeriodUIState(selectedTimePeriod = selectedTimePeriod)
    data class Error(val selectedTimePeriod: TimePeriod) : HighestInTimePeriodUIState(selectedTimePeriod = selectedTimePeriod)
}