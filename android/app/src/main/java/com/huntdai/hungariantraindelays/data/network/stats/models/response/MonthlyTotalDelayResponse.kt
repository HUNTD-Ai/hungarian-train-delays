package com.huntdai.hungariantraindelays.data.network.stats.models.response

import com.huntdai.hungariantraindelays.data.network.stats.models.Delay

data class MonthlyTotalDelayResponse(
    val delays : List<Delay>
)