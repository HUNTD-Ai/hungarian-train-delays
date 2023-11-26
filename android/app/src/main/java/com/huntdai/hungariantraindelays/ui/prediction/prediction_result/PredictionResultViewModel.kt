package com.huntdai.hungariantraindelays.ui.prediction.prediction_result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huntdai.hungariantraindelays.data.DataSourceError
import com.huntdai.hungariantraindelays.data.DataSourceResponse
import com.huntdai.hungariantraindelays.data.DataSourceResult
import com.huntdai.hungariantraindelays.data.network.delay_cause.DelayCauseDataSource
import com.huntdai.hungariantraindelays.data.network.prediction.DelayPredictionDataSource
import com.huntdai.hungariantraindelays.data.network.stats.StatsDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PredictionResultViewModel @Inject constructor(
    private val statsDataSource: StatsDataSource,
    private val delayPredictionDataSource: DelayPredictionDataSource,
    private val delayCauseDataSource: DelayCauseDataSource
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<PredictionResultUIState>(PredictionResultUIState.Initial)
    val uiState: StateFlow<PredictionResultUIState> = _uiState.asStateFlow()

    fun initUiState(route: String, departureTime: String, trainNumber: String) =
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _uiState.update { PredictionResultUIState.Loading }
                val delayTimeResponse = delayPredictionDataSource.getDelayPrediction(
                    route = route,
                    departureTime = departureTime,
                    trainNumber = trainNumber.toInt()
                )
                val delayCauseResponse = delayCauseDataSource.getDelayCause(
                    route = route,
                    departureTime = departureTime,
                    trainNumber = trainNumber.toInt()
                )
                val liveStatsResponse =
                    statsDataSource.getLiveData(route = route, trainNumber = trainNumber.toInt())
                if (delayCauseResponse is DataSourceResult && delayTimeResponse is DataSourceResult) {
                    if (liveStatsResponse !is DataSourceResult) {
                        _uiState.update {
                            PredictionResultUIState.Loaded(
                                delayTime = delayTimeResponse.result,
                                delayCause = delayCauseResponse.result,
                                null, null
                            )
                        }
                    } else {
                        _uiState.update {
                            PredictionResultUIState.Loaded(
                                delayTime = (delayTimeResponse as DataSourceResult).result,
                                delayCause = (delayCauseResponse as DataSourceResult).result,
                                liveDelayTime = liveStatsResponse.result.delay.toString(),
                                liveDelayCause = liveStatsResponse.result.delayCause.toString()
                            )
                        }
                    }

                } else {
                    _uiState.update { PredictionResultUIState.Error }
                }

            }
        }
}