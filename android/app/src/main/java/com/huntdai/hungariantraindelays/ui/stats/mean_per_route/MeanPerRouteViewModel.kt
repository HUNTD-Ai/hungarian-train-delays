package com.huntdai.hungariantraindelays.ui.stats.mean_per_route

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huntdai.hungariantraindelays.data.DataSourceError
import com.huntdai.hungariantraindelays.data.DataSourceResult
import com.huntdai.hungariantraindelays.data.network.StatsDataSource
import com.huntdai.hungariantraindelays.data.network.models.Delay
import com.huntdai.hungariantraindelays.ui.models.Route
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
class MeanPerRouteViewModel@Inject constructor(
    private val statsDataSource: StatsDataSource
) : ViewModel() {

    private val routeDestinationMap = MutableStateFlow<RouteDestinationMap?>(null)
    private val delays = MutableStateFlow<List<Delay>?>(null)
    private val isLoading = MutableStateFlow<Boolean>(false)
    private val isError = MutableStateFlow<Boolean>(false)

        val uiState = combine(
        routeDestinationMap,
        delays,
            isLoading,
            isError
    ) { routeDestinationMap, delays, isLoading, isError->
            if (isError){
                MeanPerRouteUIState.Error(
                    routeDestinationMap = routeDestinationMap
                )
            }
            if(isLoading){
                MeanPerRouteUIState.Loading(
                    routeDestinationMap = routeDestinationMap
                )
            }
            if(routeDestinationMap != null){
                if(delays != null){
                    MeanPerRouteUIState.DelaysLoaded(
                        routeDestinationMap = routeDestinationMap,
                        delays = delays
                    )
                }
                else{
                    MeanPerRouteUIState.RoutesLoaded(
                        routeDestinationMap = routeDestinationMap,
                    )
                }

            }
            else{
                MeanPerRouteUIState.Initial(
                    routeDestinationMap = null
                )
            }
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), initialValue = MeanPerRouteUIState.Initial(
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

    fun loadDelays(startDestination: String, endDestination: String) = viewModelScope.launch {
        isLoading.update { true }
        when (val response = statsDataSource.getMeanRouteDelay(Route(startDestination = startDestination, endDestination = endDestination))) {
            is DataSourceError -> {
                isError.update { true }
            }
            is DataSourceResult -> {
                delays.update { response.result }
            }
        }
        isLoading.update { false }
    }

}