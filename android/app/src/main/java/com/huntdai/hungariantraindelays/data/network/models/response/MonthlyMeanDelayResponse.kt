package com.huntdai.hungariantraindelays.data.network.models.response

import com.huntdai.hungariantraindelays.data.network.models.Delay

data class MonthlyMeanDelayResponse(
    val delays : List<Delay>
)


