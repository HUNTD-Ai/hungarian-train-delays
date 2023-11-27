package com.huntdai.hungariantraindelays.data.network.delay_cause

import android.util.Log
import com.huntdai.hungariantraindelays.data.DataSourceError
import com.huntdai.hungariantraindelays.data.DataSourceResponse
import com.huntdai.hungariantraindelays.data.DataSourceResult
import com.huntdai.hungariantraindelays.data.network.delay_cause.models.body.DelayCauseBody
import com.huntdai.hungariantraindelays.data.network.delay_cause.models.response.DelayCauseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DelayCauseDataSource @Inject constructor(private val delayCauseApi: DelayCauseApi){

    suspend fun getDelayCause(route : String, trainNumber : Int, departureTime : String): DataSourceResponse<DelayCauseResponse> =
        withContext(Dispatchers.IO) {
            try {
                val body = DelayCauseBody(
                    route = route,
                    trainNumber = trainNumber.toString(),
                    departureTime = departureTime
                )
                Log.d("DEMO", "BODY" + body.toString())
                val response = delayCauseApi.predictDelayCause(body)
                Log.d("DEMO", "RESP" + response.toString())
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        return@withContext DataSourceResult(result)
                    }
                    DataSourceError
                } else {
                    DataSourceError
                }
            } catch (error: Exception) {
                Log.d("DEMO", "IO EXC" + error.toString())
                DataSourceError
            }
        }
}