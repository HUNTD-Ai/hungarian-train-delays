package com.huntdai.hungariantraindelays.ui.prediction

import com.huntdai.hungariantraindelays.data.network.stats.models.Delay
import com.huntdai.hungariantraindelays.ui.models.RouteDestinationMap

sealed class PredictionUIState {
    object Initial : PredictionUIState()
    data class Loaded(val routeDestinationMap: RouteDestinationMap) : PredictionUIState()
    object Loading : PredictionUIState()
    object Error : PredictionUIState()
}

//sealed class PredictionUIState(routeDestinationMap: RouteDestinationMap?) {
//    data class Initial(val routeDestinationMap: RouteDestinationMap?) :
//        PredictionUIState(routeDestinationMap = routeDestinationMap)
//
//    data class RoutesLoaded(val routeDestinationMap: RouteDestinationMap?) :
//        PredictionUIState(routeDestinationMap = routeDestinationMap)
//
//    data class TrainNumberSelected(
//        val routeDestinationMap: RouteDestinationMap?,
//        val trainNumber: Int
//    ) : PredictionUIState(routeDestinationMap = routeDestinationMap)
//
//    data class Loading(val routeDestinationMap: RouteDestinationMap?) :
//        PredictionUIState(routeDestinationMap = routeDestinationMap)
//
//    data class Error(val routeDestinationMap: RouteDestinationMap?) :
//        PredictionUIState(routeDestinationMap = routeDestinationMap)
//}