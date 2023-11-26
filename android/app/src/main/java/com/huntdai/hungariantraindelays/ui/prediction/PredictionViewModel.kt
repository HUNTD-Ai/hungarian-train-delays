package com.huntdai.hungariantraindelays.ui.prediction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huntdai.hungariantraindelays.data.DataSourceError
import com.huntdai.hungariantraindelays.data.DataSourceResult
import com.huntdai.hungariantraindelays.data.network.delay_cause.DelayCauseApi
import com.huntdai.hungariantraindelays.data.network.prediction.DelayPredictionApi
import com.huntdai.hungariantraindelays.data.network.stats.StatsDataSource
import com.huntdai.hungariantraindelays.ui.models.RouteDestinationMap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PredictionViewModel@Inject constructor(
    private val statsDataSource: StatsDataSource,
    private val delayCauseApi: DelayCauseApi,
    private val delayPredictionApi: DelayPredictionApi
) : ViewModel() {

    private val routeDestinationMap = MutableStateFlow<RouteDestinationMap?>(null)
    private val trainNumber = MutableStateFlow<Int?>(null)
    private val isLoading = MutableStateFlow<Boolean>(false)
    private val isError = MutableStateFlow<Boolean>(false)

        val uiState = combine(
        routeDestinationMap,
            trainNumber,
            isLoading,
            isError
    ) { routeDestinationMap, trainNumber, isLoading, isError->
            if (isError){
                PredictionUIState.Error(
                    routeDestinationMap = routeDestinationMap
                )
            }
            if(isLoading){
                PredictionUIState.Loading(
                    routeDestinationMap = routeDestinationMap
                )
            }
            if(routeDestinationMap != null){
                if(trainNumber != null){
                    PredictionUIState.TrainNumberSelected(
                        routeDestinationMap = routeDestinationMap,
                        trainNumber = trainNumber
                    )
                }
                else{
                    PredictionUIState.RoutesLoaded(
                        routeDestinationMap = routeDestinationMap,
                    )
                }

            }
            else{
                PredictionUIState.Initial(
                    routeDestinationMap = null
                )
            }
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), initialValue = PredictionUIState.Initial(
                routeDestinationMap = null
            )
    )


    fun initUiState() = viewModelScope.launch {
        isLoading.update { true }
        when (val response = statsDataSource.getRouteDestinationMap()) {
            is DataSourceError -> {
                isError.update { true }
            }
            is DataSourceResult -> {
                routeDestinationMap.update { response.result }
            }
        }
        isLoading.update { false }
    }

    fun errorHandled() = viewModelScope.launch {
        isError.update { false }
    }
}