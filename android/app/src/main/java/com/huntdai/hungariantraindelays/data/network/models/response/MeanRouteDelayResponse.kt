package com.huntdai.hungariantraindelays.data.network.models.response

import com.huntdai.hungariantraindelays.data.network.models.Delay

data class MeanRouteDelayResponse(
    val route : String,
    val delays : Delays
)

data class Delays(
    val delays : List<Delay>
)
