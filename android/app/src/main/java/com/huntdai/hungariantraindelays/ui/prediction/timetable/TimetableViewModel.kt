package com.huntdai.hungariantraindelays.ui.prediction.timetable

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
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class TimetableViewModel@Inject constructor(
    private val statsDataSource: StatsDataSource
) : ViewModel() {

    private val _uiState = MutableStateFlow<TimetableUIState>(TimetableUIState.Initial)
    val uiState: StateFlow<TimetableUIState> = _uiState.asStateFlow()

    fun initUiState(route : String, departureDate : String) = viewModelScope.launch {
        _uiState.update { TimetableUIState.Loading }
        when (val response = statsDataSource.getTimetable(route, departureDate)) {
            is DataSourceError -> {
                _uiState.update { TimetableUIState.Error }
            }
            is DataSourceResult -> {
                _uiState.update { TimetableUIState.Loaded(response.result) }
            }
        }
    }
}