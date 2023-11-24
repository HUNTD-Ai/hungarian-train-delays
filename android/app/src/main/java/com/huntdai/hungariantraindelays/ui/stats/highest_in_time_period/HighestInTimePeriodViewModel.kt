package com.huntdai.hungariantraindelays.ui.stats.highest_in_time_period

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huntdai.hungariantraindelays.data.DataSourceError
import com.huntdai.hungariantraindelays.data.DataSourceResult
import com.huntdai.hungariantraindelays.data.network.StatsDataSource
import com.huntdai.hungariantraindelays.ui.stats.highest_in_time_period.models.TimePeriod
import com.huntdai.hungariantraindelays.ui.stats.monthly_total.MonthlyTotalUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HighestInTimePeriodViewModel@Inject constructor(
    private val statsDataSource: StatsDataSource
) : ViewModel() {

    private val _uiState = MutableStateFlow<HighestInTimePeriodUIState>(HighestInTimePeriodUIState.Initial(TimePeriod.WEEK))
    val uiState: StateFlow<HighestInTimePeriodUIState> = _uiState.asStateFlow()

    fun initUiState() = viewModelScope.launch {
//        _uiState.update { HighestInTimePeriodUIState.Loading }
//        when (val response = statsDataSource.getMonthlyMeanDelay()) {
//            is DataSourceError -> {
//                _uiState.update { HighestInTimePeriodUIState.Error }
//            }
//            is DataSourceResult -> {
//                _uiState.update { HighestInTimePeriodUIState.Loaded(response.result) }
//            }
//        }
    }

    fun timePeriodChanged(newTimePeriod: TimePeriod) = viewModelScope.launch {
        statsDataSource.getHighestDelayInTimePeriod(newTimePeriod)
    }
}
