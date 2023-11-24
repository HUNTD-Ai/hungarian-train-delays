package com.huntdai.hungariantraindelays.data.network

import android.location.Location
import android.util.Log
import com.huntdai.hungariantraindelays.data.DataSourceError
import com.huntdai.hungariantraindelays.data.DataSourceResponse
import com.huntdai.hungariantraindelays.data.DataSourceResult
import com.huntdai.hungariantraindelays.data.network.models.Delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatsDataSource @Inject constructor(private val statsApi: StatsApi){

    suspend fun getRoutes(): DataSourceResponse<List<String>> =
        withContext(Dispatchers.IO) {
            try {
                val response = statsApi.getRoutes()
                Log.d("DEMO", "RESP" + response.toString())
                if (response.isSuccessful) {
                    val routes = response.body()?.routes
                    if (routes != null) {
                        return@withContext DataSourceResult(routes)
                    }
                    DataSourceError
                } else {
                    DataSourceError
                }
            } catch (error: IOException) {
                Log.d("DEMO", "IO EXC" + error.toString())
                DataSourceError
            }
        }

    suspend fun getMonthlyMeanDelay(): DataSourceResponse<List<Delay>> =
        withContext(Dispatchers.IO) {
            try {
                val response = statsApi.getMonthlyMeanDelay()
                Log.d("DEMO", "RESP" + response.toString())
                if (response.isSuccessful) {
                    val delays = response.body()?.delays
                    if (delays != null) {
                        return@withContext DataSourceResult(delays)
                    }
                    DataSourceError
                } else {
                    DataSourceError
                }
            } catch (error: IOException) {
                Log.d("DEMO", "IO EXC" + error.toString())
                DataSourceError
            }
        }

    suspend fun getMonthlyTotalDelay(): DataSourceResponse<List<Delay>> =
        withContext(Dispatchers.IO) {
            try {
                val response = statsApi.getMonthlySumDelay()
                Log.d("DEMO", "RESP" + response.toString())
                if (response.isSuccessful) {
                    val delays = response.body()?.delays
                    if (delays != null) {
                        return@withContext DataSourceResult(delays)
                    }
                    DataSourceError
                } else {
                    DataSourceError
                }
            } catch (error: IOException) {
                Log.d("DEMO", "IO EXC" + error.toString())
                DataSourceError
            }
        }
}