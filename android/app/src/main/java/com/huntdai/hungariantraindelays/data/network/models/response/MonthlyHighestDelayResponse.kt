package com.huntdai.hungariantraindelays.data.network.models.response

import com.huntdai.hungariantraindelays.data.network.models.Delay

data class MonthlyHighestDelayResponse(
    val delays : List<Delay>
)