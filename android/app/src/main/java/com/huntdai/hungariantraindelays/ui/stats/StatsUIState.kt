package com.huntdai.hungariantraindelays.ui.stats

sealed class StatsUIState {
    object Initial : StatsUIState()
    data class Demo(val routes: List<String>) : StatsUIState()
    object MonthlyMeanSelected : StatsUIState()
    object MonthlySumSelected : StatsUIState()
    object MonthlyHighestSelected : StatsUIState()

}

