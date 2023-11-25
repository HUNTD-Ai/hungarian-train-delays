package com.huntdai.hungariantraindelays.data.network

import android.util.Log
import com.huntdai.hungariantraindelays.data.DataSourceError
import com.huntdai.hungariantraindelays.data.DataSourceResponse
import com.huntdai.hungariantraindelays.data.DataSourceResult
import com.huntdai.hungariantraindelays.data.network.models.Delay
import com.huntdai.hungariantraindelays.ui.models.Route
import com.huntdai.hungariantraindelays.data.network.models.body.HighestDelayInTimePeriodBody
import com.huntdai.hungariantraindelays.data.network.models.body.MeanRouteDelayBody
import com.huntdai.hungariantraindelays.ui.models.RouteDestinationMap
import com.huntdai.hungariantraindelays.ui.stats.highest_in_time_period.models.TimePeriod
import com.huntdai.hungariantraindelays.utils.combineRouteEnds
import com.huntdai.hungariantraindelays.utils.getTodaysDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatsDataSource @Inject constructor(private val statsApi: StatsApi){

    private suspend fun getRoutes(): DataSourceResponse<List<String>> =
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

    suspend fun getRouteDestinationMap(): DataSourceResponse<RouteDestinationMap> =
        withContext(Dispatchers.IO) {
            when(val response = getRoutes()){
              is DataSourceError -> { DataSourceError}
                is DataSourceResult -> {
                    val routes = response.result
                    val startDestinations = mutableMapOf<String, List<String>>()

                    for (route in routes) {
                        val parts = route.split(" - ")
                        if (parts.size >= 2) {
                            val startDestination = parts[0]
                            val endDestination = parts[1]
                            val endDestinationsAlreadyAdded = startDestinations.getOrDefault(startDestination, mutableListOf()).toMutableList()
                            endDestinationsAlreadyAdded.add(endDestination)
                            startDestinations[startDestination] = endDestinationsAlreadyAdded
                        }
                    }
                    DataSourceResult(RouteDestinationMap(startDestinations = startDestinations))
                }
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
                val today = getTodaysDate()
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


    suspend fun getMeanRouteDelay(route : Route): DataSourceResponse<List<Delay>> =
        withContext(Dispatchers.IO) {
            try {
                val today = getTodaysDate()
                val todaysUnix= today.time.time

                val monthAgo = today
                monthAgo.add(Calendar.DATE, -30)
                val monthAgoUnix = monthAgo.time.time

                val body = MeanRouteDelayBody(
                    route = combineRouteEnds(route.startDestination, route.endDestination),
                    startTimestamp = monthAgoUnix.toString(),
                    endTimestamp = todaysUnix.toString()
                )
                Log.d("DEMO", "BODY" + body.toString())
                val response = statsApi.getMeanRouteDelay(body)
                Log.d("DEMO", "RESP" + response.toString())
                if (response.isSuccessful) {
                    val delays = response.body()?.delays?.delays
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