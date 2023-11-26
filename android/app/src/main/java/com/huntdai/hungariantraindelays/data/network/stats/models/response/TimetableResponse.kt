package com.huntdai.hungariantraindelays.data.network.stats.models.response

import com.squareup.moshi.Json

data class TimetableResponse(
    @Json(name = "plans") val plans: List<Plans>
)

data class Details(

    @Json(name = "from_station") var fromStation: String? = null,
    @Json(name = "to_station") var toStation: String? = null,
    @Json(name = "dep_planned_time") var depPlannedTime: String? = null,
    @Json(name = "dep_actual_time") var depActualTime: String? = null,
    @Json(name = "dep_info") var depInfo: String? = null,
    @Json(name = "arr_planned_time") var arrPlannedTime: String? = null,
    @Json(name = "arr_actual_time") var arrActualTime: String? = null,
    @Json(name = "arr_info") var arrInfo: String? = null,
    @Json(name = "train_number") var trainNumber: String? = null

)

data class Plans(

    @Json(name = "route") var route: String? = null,
    @Json(name = "arrival_time") var arrivalTime: String? = null,
    @Json(name = "departure_time") var departureTime: String? = null,
    @Json(name = "changes") var changes: Int? = null,
    @Json(name = "duration") var duration: String? = null,
    @Json(name = "length_km") var lengthKm: String? = null,
    @Json(name = "highest_class") var highestClass: String? = null,
    @Json(name = "details") var details: List<Details> = arrayListOf()

)


