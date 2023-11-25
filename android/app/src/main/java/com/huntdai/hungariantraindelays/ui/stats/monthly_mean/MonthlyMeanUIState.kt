package com.huntdai.hungariantraindelays.ui.stats.monthly_mean

import com.huntdai.hungariantraindelays.data.network.models.Delay

sealed class MonthlyMeanUIState  {
    object Initial : MonthlyMeanUIState()
    data class Loaded(val delays: List<Delay>) : MonthlyMeanUIState()
    object Loading : MonthlyMeanUIState()
    object Error : MonthlyMeanUIState()
}