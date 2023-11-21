package com.huntdai.hungariantraindelays.data.network

import com.huntdai.hungariantraindelays.data.network.models.MonthlyMeanDelayResponse
import com.huntdai.hungariantraindelays.data.network.models.MonthlySumDelayResponse
import com.huntdai.hungariantraindelays.data.network.models.RoutesResponse
import retrofit2.Response
import retrofit2.http.GET

interface StatsApi {
    companion object {
        const val ENDPOINT_URL = "http://192.168.0.33:8000/stats/"
    }

    @GET("routes")
    suspend fun getRoutes(): Response<RoutesResponse>

    @GET("monthly-mean")
    suspend fun getMonthlyMeanDelay(): Response<MonthlyMeanDelayResponse>

    @GET("monthly-sum")
    suspend fun getMonthlySumDelay(): Response<MonthlySumDelayResponse>

    //suspend fun getMonthlyHighestDelay(): Response<RoutesResponse>

    //suspend fun getMeanRouteDelay(): Response<RoutesResponse>
}