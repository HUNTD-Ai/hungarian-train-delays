package com.huntdai.hungariantraindelays.ui.prediction.prediction_result

sealed class PredictionResultUIState {
    object Initial : PredictionResultUIState()
    data class Loaded(
        val delayTime: String,
        val delayCause: String,
        val liveDelayTime: String?,
        val liveDelayCause: String?
    ) : PredictionResultUIState()

    object Loading : PredictionResultUIState()
    object Error : PredictionResultUIState()
}