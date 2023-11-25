package com.huntdai.hungariantraindelays.data.network.prediction.models.body

import com.squareup.moshi.Json

data class DelayPredictionBody (
      val route : String,
      @Json(name = "train_number") val trainNumber : String,
      @Json(name = "depart_time") var departureTime   : String
)