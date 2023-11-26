package com.huntdai.hungariantraindelays.data.network.stats.models.body

import com.squareup.moshi.Json

data class TimetableBody(
    var route : String,
    @Json(name = "depart_date") var departureDate  : String
)
