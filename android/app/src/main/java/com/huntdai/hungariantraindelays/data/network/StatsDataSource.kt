package com.huntdai.hungariantraindelays.data.network

import android.util.Log
import com.huntdai.hungariantraindelays.data.DataSourceError
import com.huntdai.hungariantraindelays.data.DataSourceResponse
import com.huntdai.hungariantraindelays.data.DataSourceResult
import com.huntdai.hungariantraindelays.data.network.models.Delay
import com.huntdai.hungariantraindelays.data.network.models.HighestDelayInTimePeriodBody
import com.huntdai.hungariantraindelays.ui.stats.highest_in_time_period.models.TimePeriod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Calendar
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
                val response = statsApi.getMonthlyTotalDelay()
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

    suspend fun getHighestDelayInTimePeriod(timePeriod: TimePeriod): DataSourceResponse<List<Delay>> =
        withContext(Dispatchers.IO) {
            try {
                val today: Calendar = Calendar.getInstance()
                today.set(Calendar.HOUR_OF_DAY, 0)
                today.set(Calendar.MINUTE, 0)
                today.set(Calendar.SECOND, 0)
                today.set(Calendar.MILLISECOND, 0)
                val todaysUnix= today.time.time
                val previousUnix = when(timePeriod){
                        TimePeriod.WEEK -> {
                            val previousDay = today
                            previousDay.add(Calendar.DATE, -7)
                            previousDay.time.time
                        }
                        TimePeriod.MONTH -> {
                            val previousDay = today
                            previousDay.add(Calendar.DATE, -30)
                            previousDay.time.time
                        }

                        TimePeriod.SIX_MONTHS -> {
                            val previousDay = today
                            previousDay.add(Calendar.DATE, -180)
                            previousDay.time.time
                        }
                    }
                val body = HighestDelayInTimePeriodBody(
                    startTimestamp = previousUnix.toString(),
                    endTimestamp = todaysUnix.toString()
                )
                Log.d("DEMO", "BODY" + body.toString())
                val response = statsApi.getHighestDelayInTimePeriod(body)
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