package com.huntdai.hungariantraindelays.data.network.delay_cause.models.body

import com.squareup.moshi.Json

data class DelayCauseBody (
     val route : String,
     @Json(name = "train_number") val trainNumber : String,
     @Json(name = "depart_time") var departureTime   : String
)
