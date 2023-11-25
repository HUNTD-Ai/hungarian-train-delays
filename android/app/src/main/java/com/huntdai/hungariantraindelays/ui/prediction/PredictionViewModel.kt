package com.huntdai.hungariantraindelays.ui.prediction

import androidx.lifecycle.ViewModel
import com.huntdai.hungariantraindelays.data.network.delay_cause.DelayCauseApi
import com.huntdai.hungariantraindelays.data.network.prediction.DelayPredictionApi
import com.huntdai.hungariantraindelays.data.network.stats.StatsDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PredictionViewModel@Inject constructor(
    private val statsDataSource: StatsDataSource,
    private val delayCauseApi: DelayCauseApi,
    private val delayPredictionApi: DelayPredictionApi
) : ViewModel() {

//    private val routeDestinationMap = MutableStateFlow<RouteDestinationMap?>(null)
//    private val trainNumber = MutableStateFlow<Int?>(null)
//    private val isLoading = MutableStateFlow<Boolean>(false)
//    private val isError = MutableStateFlow<Boolean>(false)
//
//        val uiState = combine(
//        routeDestinationMap,
//        delays,
//            isLoading,
//            isError
//    ) { routeDestinationMap, delays, isLoading, isError->
//            if (isError){
//                MeanPerRouteUIState.Error(
//                    routeDestinationMap = routeDestinationMap
//                )
//            }
//            if(isLoading){
//                MeanPerRouteUIState.Loading(
//                    routeDestinationMap = routeDestinationMap
//                )
//            }
//            if(routeDestinationMap != null){
//                if(delays != null){
//                    MeanPerRouteUIState.DelaysLoaded(
//                        routeDestinationMap = routeDestinationMap,
//                        delays = delays
//                    )
//                }
//                else{
//                    MeanPerRouteUIState.RoutesLoaded(
//                        routeDestinationMap = routeDestinationMap,
//                    )
//                }
//
//            }
//            else{
//                MeanPerRouteUIState.Initial(
//                    routeDestinationMap = null
//                )
//            }
//    }.stateIn(
//        viewModelScope, SharingStarted.WhileSubscribed(), initialValue = MeanPerRouteUIState.Initial(
//                routeDestinationMap = null
//            )
//    )
//
//
//    fun initUiState() = viewModelScope.launch {
//        isLoading.update { true }
//        when (val response = statsDataSource.getRouteDestinationMap()) {
//            is DataSourceError -> {
//                isError.update { true }
//            }
//            is DataSourceResult -> {
//                routeDestinationMap.update { response.result }
//            }
//        }
//        isLoading.update { false }
//    }
//
//    fun errorHandled() = viewModelScope.launch {
//        isError.update { false }
//    }
//
//    fun loadDelays(startDestination: String, endDestination: String) = viewModelScope.launch {
//        isLoading.update { true }
//        when (val response = statsDataSource.getMeanRouteDelay(Route(startDestination = startDestination, endDestination = endDestination))) {
//            is DataSourceError -> {
//                isError.update { true }
//            }
//            is DataSourceResult -> {
//                delays.update { response.result }
//            }
//        }
//        isLoading.update { false }
//    }

}