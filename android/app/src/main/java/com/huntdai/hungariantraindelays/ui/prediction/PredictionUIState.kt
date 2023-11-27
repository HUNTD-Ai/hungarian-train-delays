package com.huntdai.hungariantraindelays.ui.prediction

import com.huntdai.hungariantraindelays.data.network.stats.models.Delay
import com.huntdai.hungariantraindelays.ui.models.RouteDestinationMap

sealed class PredictionUIState {
    object Initial : PredictionUIState()
    data class Loaded(val routeDestinationMap: RouteDestinationMap) : PredictionUIState()
    object Loading : PredictionUIState()
    object Error : PredictionUIState()
}
