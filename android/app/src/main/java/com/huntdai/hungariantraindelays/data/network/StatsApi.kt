package com.huntdai.hungariantraindelays.data.network

import com.huntdai.hungariantraindelays.data.network.model.RoutesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface StatsApi {
    companion object {
        const val ENDPOINT_URL = "http://192.168.0.33:8000/stats/"
    }

    @GET("routes")
    suspend fun getRoutes(): Response<RoutesResponse>
}