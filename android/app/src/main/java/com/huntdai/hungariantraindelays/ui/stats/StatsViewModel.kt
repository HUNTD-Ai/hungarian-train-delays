package com.huntdai.hungariantraindelays.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huntdai.hungariantraindelays.data.DataSourceError
import com.huntdai.hungariantraindelays.data.DataSourceResult
import com.huntdai.hungariantraindelays.data.network.StatsDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatsViewModel@Inject constructor(
    private val statsDataSource: StatsDataSource
) : ViewModel() {

    private val _uiState = MutableStateFlow<StatsUIState>(StatsUIState.Initial)
    val uiState: StateFlow<StatsUIState> = _uiState.asStateFlow()

    fun initUiState() = viewModelScope.launch {
//        when (val response = statsDataSource.getRoutes()) {
//            is DataSourceError -> {
//                _uiState.update { StatsUIState.Demo(listOf("error")) }
//            }
//            is DataSourceResult -> {
//                _uiState.update { StatsUIState.Demo(response.result) }
//            }
//        }
    }
}