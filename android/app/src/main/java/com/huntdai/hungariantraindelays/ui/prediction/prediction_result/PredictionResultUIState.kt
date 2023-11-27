package com.huntdai.hungariantraindelays.ui.prediction.prediction_result

sealed class PredictionResultUIState {
    object Initial : PredictionResultUIState()
    data class Loaded(
        val delayLabel: Int,
        val delayScore: Double,
        val delayCause: String,
        val delayCauseScore: Double,
        val liveDelayTime: String?,
        val liveDelayCause: String?
    ) : PredictionResultUIState()

    object Loading : PredictionResultUIState()
    object Error : PredictionResultUIState()
}