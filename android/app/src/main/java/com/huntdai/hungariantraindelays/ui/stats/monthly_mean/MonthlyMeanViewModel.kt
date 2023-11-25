package com.huntdai.hungariantraindelays.ui.stats.monthly_mean

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huntdai.hungariantraindelays.data.DataSourceError
import com.huntdai.hungariantraindelays.data.DataSourceResult
import com.huntdai.hungariantraindelays.data.network.StatsDataSource
import com.huntdai.hungariantraindelays.ui.stats.StatsUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MonthlyMeanViewModel@Inject constructor(
    private val statsDataSource: StatsDataSource
) : ViewModel() {

    private val _uiState = MutableStateFlow<MonthlyMeanUIState>(MonthlyMeanUIState.Initial)
    val uiState: StateFlow<MonthlyMeanUIState> = _uiState.asStateFlow()

    fun initUiState() = viewModelScope.launch {
        _uiState.update { MonthlyMeanUIState.Loading }
        when (val response = statsDataSource.getMonthlyMeanDelay()) {
            is DataSourceError -> {
                _uiState.update { MonthlyMeanUIState.Error }
            }
            is DataSourceResult -> {
                _uiState.update { MonthlyMeanUIState.Loaded(response.result) }
            }
        }
    }
}