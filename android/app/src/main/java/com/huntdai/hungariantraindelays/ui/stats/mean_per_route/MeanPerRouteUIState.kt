package com.huntdai.hungariantraindelays.ui.stats.mean_per_route

import com.huntdai.hungariantraindelays.data.network.stats.models.Delay
import com.huntdai.hungariantraindelays.ui.models.RouteDestinationMap

sealed class MeanPerRouteUIState(routeDestinationMap: RouteDestinationMap?)  {
    data class Initial(val routeDestinationMap: RouteDestinationMap?) : MeanPerRouteUIState(routeDestinationMap = routeDestinationMap)
    data class RoutesLoaded(val routeDestinationMap: RouteDestinationMap?) : MeanPerRouteUIState(routeDestinationMap = routeDestinationMap)
    data class DelaysLoaded(val routeDestinationMap: RouteDestinationMap?, val delays: List<Delay>) : MeanPerRouteUIState(routeDestinationMap = routeDestinationMap)
    data class Loading(val routeDestinationMap: RouteDestinationMap?) : MeanPerRouteUIState(routeDestinationMap = routeDestinationMap)
    data class Error(val routeDestinationMap: RouteDestinationMap?) : MeanPerRouteUIState(routeDestinationMap = routeDestinationMap)
}