package com.huntdai.hungariantraindelays.ui.stats.highest_in_time_period

import com.huntdai.hungariantraindelays.data.network.stats.models.Delay

sealed class HighestInTimePeriodUIState {
    object Initial : HighestInTimePeriodUIState()
    data class Loaded(val delays: List<Delay>) : HighestInTimePeriodUIState()
    object Loading : HighestInTimePeriodUIState()
    object Error : HighestInTimePeriodUIState()
}