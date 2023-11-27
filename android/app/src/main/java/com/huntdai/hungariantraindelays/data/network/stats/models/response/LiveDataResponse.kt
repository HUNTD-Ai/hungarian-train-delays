package com.huntdai.hungariantraindelays.data.network.stats.models.response

data class LiveDataResponse(
    var route: String,
    var trainNumber: Int,
    var delay: Int,
    var delayCause: String?
)
