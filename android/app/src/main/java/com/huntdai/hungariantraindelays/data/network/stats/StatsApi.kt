package com.huntdai.hungariantraindelays.data.network.stats

import com.huntdai.hungariantraindelays.data.network.stats.models.body.HighestDelayInTimePeriodBody
import com.huntdai.hungariantraindelays.data.network.stats.models.body.LiveDataBody
import com.huntdai.hungariantraindelays.data.network.stats.models.body.MeanRouteDelayBody
import com.huntdai.hungariantraindelays.data.network.stats.models.body.TimetableBody
import com.huntdai.hungariantraindelays.data.network.stats.models.response.LiveDataResponse
import com.huntdai.hungariantraindelays.data.network.stats.models.response.MeanRouteDelayResponse
import com.huntdai.hungariantraindelays.data.network.stats.models.response.MonthlyHighestDelayResponse
import com.huntdai.hungariantraindelays.data.network.stats.models.response.MonthlyMeanDelayResponse
import com.huntdai.hungariantraindelays.data.network.stats.models.response.MonthlyTotalDelayResponse
import com.huntdai.hungariantraindelays.data.network.stats.models.response.RoutesResponse
import com.huntdai.hungariantraindelays.data.network.stats.models.response.TimetableResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface StatsApi {
    companion object {
        const val ENDPOINT_URL = "https://core.debreczeni.eu/stats/"
    }

    @GET("routes")
    suspend fun getRoutes(): Response<RoutesResponse>

    @GET("monthly-mean")
    suspend fun getMonthlyMeanDelay(): Response<MonthlyMeanDelayResponse>

    @GET("monthly-sum")
    suspend fun getMonthlyTotalDelay(): Response<MonthlyTotalDelayResponse>

    @POST("highest-delay")
    suspend fun getHighestDelayInTimePeriod(@Body body: HighestDelayInTimePeriodBody): Response<MonthlyHighestDelayResponse>

    @POST("mean-route-delay")
    suspend fun getMeanRouteDelay(@Body body: MeanRouteDelayBody): Response<MeanRouteDelayResponse>

    @POST("timetable")
    suspend fun getTimetable(@Body body: TimetableBody): Response<TimetableResponse>

    @POST("live")
    suspend fun getLiveData(@Body body: LiveDataBody): Response<LiveDataResponse>

}