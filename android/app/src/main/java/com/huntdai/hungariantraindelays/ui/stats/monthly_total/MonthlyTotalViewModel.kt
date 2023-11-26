package com.huntdai.hungariantraindelays.ui.stats.monthly_total

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huntdai.hungariantraindelays.data.DataSourceError
import com.huntdai.hungariantraindelays.data.DataSourceResult
import com.huntdai.hungariantraindelays.data.network.stats.StatsDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MonthlyTotalViewModel@Inject constructor(
    private val statsDataSource: StatsDataSource
) : ViewModel() {

    private val _uiState = MutableStateFlow<MonthlyTotalUIState>(MonthlyTotalUIState.Initial)
    val uiState: StateFlow<MonthlyTotalUIState> = _uiState.asStateFlow()

    fun initUiState() = viewModelScope.launch {
        _uiState.update { MonthlyTotalUIState.Loading }
        when (val response = statsDataSource.getMonthlyTotalDelay()) {
            is DataSourceError -> {
                _uiState.update { MonthlyTotalUIState.Error }
            }
            is DataSourceResult -> {
                _uiState.update { MonthlyTotalUIState.Loaded(response.result) }
            }
        }
    }
}