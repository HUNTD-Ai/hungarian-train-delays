package com.huntdai.hungariantraindelays.data.network.prediction

import com.huntdai.hungariantraindelays.data.network.prediction.models.body.DelayPredictionBody
import com.huntdai.hungariantraindelays.data.network.prediction.models.response.DelayPredictionResponse
import com.huntdai.hungariantraindelays.data.network.stats.models.body.HighestDelayInTimePeriodBody
import com.huntdai.hungariantraindelays.data.network.stats.models.response.MonthlyHighestDelayResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface DelayPredictionApi {
    companion object {
        const val ENDPOINT_URL = "https://delay-pred.debreczeni.eu/"
    }

    @POST("predict")
    suspend fun predictDelay(@Body body: DelayPredictionBody): Response<DelayPredictionResponse>

}