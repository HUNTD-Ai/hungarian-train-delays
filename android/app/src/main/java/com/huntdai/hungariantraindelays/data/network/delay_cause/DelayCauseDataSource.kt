package com.huntdai.hungariantraindelays.data.network.delay_cause

import android.util.Log
import com.huntdai.hungariantraindelays.data.DataSourceError
import com.huntdai.hungariantraindelays.data.DataSourceResponse
import com.huntdai.hungariantraindelays.data.DataSourceResult
import com.huntdai.hungariantraindelays.data.network.delay_cause.models.body.DelayCauseBody
import com.huntdai.hungariantraindelays.data.network.prediction.models.body.DelayPredictionBody
import com.huntdai.hungariantraindelays.ui.models.Route
import com.huntdai.hungariantraindelays.utils.combineRouteEnds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DelayCauseDataSource @Inject constructor(private val delayCauseApi: DelayCauseApi){

    suspend fun getDelayPrediction(route : Route, trainNumber : Int, departureTime : String): DataSourceResponse<String> =
        withContext(Dispatchers.IO) {
            try {
                val body = DelayCauseBody(
                    route = combineRouteEnds(route.startDestination, route.endDestination),
                    trainNumber = trainNumber.toString(),
                    departureTime = departureTime
                )
                Log.d("DEMO", "BODY" + body.toString())
                val response = delayCauseApi.predictDelayCause(body)
                Log.d("DEMO", "RESP" + response.toString())
                if (response.isSuccessful) {
                    val delay = response.body()?.cause
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