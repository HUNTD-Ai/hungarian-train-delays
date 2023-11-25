package com.huntdai.hungariantraindelays.data.network.prediction

import android.util.Log
import com.huntdai.hungariantraindelays.data.DataSourceError
import com.huntdai.hungariantraindelays.data.DataSourceResponse
import com.huntdai.hungariantraindelays.data.DataSourceResult
import com.huntdai.hungariantraindelays.data.network.prediction.models.body.DelayPredictionBody
import com.huntdai.hungariantraindelays.data.network.prediction.models.response.DelayPredictionResponse
import com.huntdai.hungariantraindelays.data.network.stats.models.Delay
import com.huntdai.hungariantraindelays.data.network.stats.models.body.MeanRouteDelayBody
import com.huntdai.hungariantraindelays.ui.models.Route
import com.huntdai.hungariantraindelays.utils.combineRouteEnds
import com.huntdai.hungariantraindelays.utils.getTodaysDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DelayPredictionDataSource @Inject constructor(private val delayPredictionApi: DelayPredictionApi){

    suspend fun getDelayPrediction(route : Route, trainNumber : Int, departureTime : String): DataSourceResponse<String> =
        withContext(Dispatchers.IO) {
            try {
                val body = DelayPredictionBody(
                    route = combineRouteEnds(route.startDestination, route.endDestination),
                    trainNumber = trainNumber.toString(),
                    departureTime = departureTime
                )
                Log.d("DEMO", "BODY" + body.toString())
                val response = delayPredictionApi.predictDelay(body)
                Log.d("DEMO", "RESP" + response.toString())
                if (response.isSuccessful) {
                    val delay = response.body()?.delay
                    if (delay != null) {
                        return@withContext DataSourceResult(delay)
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