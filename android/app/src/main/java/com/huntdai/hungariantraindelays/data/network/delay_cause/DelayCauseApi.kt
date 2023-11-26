package com.huntdai.hungariantraindelays.data.network.delay_cause

import com.huntdai.hungariantraindelays.data.network.delay_cause.models.body.DelayCauseBody
import com.huntdai.hungariantraindelays.data.network.delay_cause.models.response.DelayCauseResponse
import com.huntdai.hungariantraindelays.data.network.stats.models.body.HighestDelayInTimePeriodBody
import com.huntdai.hungariantraindelays.data.network.stats.models.response.MonthlyHighestDelayResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface DelayCauseApi {
    companion object {
        const val ENDPOINT_URL = "https://cause-pred.debreczeni.eu/"
    }

    @POST("predict")
    suspend fun predictDelayCause(@Body body: DelayCauseBody): Response<DelayCauseResponse>

}