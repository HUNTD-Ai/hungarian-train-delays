package com.huntdai.hungariantraindelays.ui.stats.monthly_total

import com.huntdai.hungariantraindelays.data.network.stats.models.Delay

sealed class MonthlyTotalUIState  {
    object Initial : MonthlyTotalUIState()
    data class Loaded(val delays: List<Delay>) : MonthlyTotalUIState()
    object Loading : MonthlyTotalUIState()
    object Error : MonthlyTotalUIState()
}