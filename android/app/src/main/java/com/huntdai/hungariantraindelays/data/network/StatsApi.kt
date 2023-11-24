package com.huntdai.hungariantraindelays.data.network

import com.huntdai.hungariantraindelays.data.network.models.HighestDelayInTimePeriodBody
import com.huntdai.hungariantraindelays.data.network.models.response.MonthlyHighestDelayResponse
import com.huntdai.hungariantraindelays.data.network.models.response.MonthlyMeanDelayResponse
import com.huntdai.hungariantraindelays.data.network.models.response.MonthlyTotalDelayResponse
import com.huntdai.hungariantraindelays.data.network.models.response.RoutesResponse
import com.huntdai.hungariantraindelays.ui.stats.StatsUIState
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface StatsApi {
    companion object {
        const val ENDPOINT_URL = "http://192.168.0.33:8000/stats/"
    }

    @GET("routes")
    suspend fun getRoutes(): Response<RoutesResponse>

    @GET("monthly-mean")
    suspend fun getMonthlyMeanDelay(): Response<MonthlyMeanDelayResponse>

    @GET("monthly-sum")
    suspend fun getMonthlyTotalDelay(): Response<MonthlyTotalDelayResponse>

    @POST("highest-delay")
    suspend fun getHighestDelayInTimePeriod(@Body body: HighestDelayInTimePeriodBody): Response<MonthlyHighestDelayResponse>

    //suspend fun getMeanRouteDelay(): Response<RoutesResponse>
}