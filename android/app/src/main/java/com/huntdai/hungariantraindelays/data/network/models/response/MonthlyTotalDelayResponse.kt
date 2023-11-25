package com.huntdai.hungariantraindelays.data.network.models.response

import com.huntdai.hungariantraindelays.data.network.models.Delay

data class MonthlyTotalDelayResponse(
    val delays : List<Delay>
)