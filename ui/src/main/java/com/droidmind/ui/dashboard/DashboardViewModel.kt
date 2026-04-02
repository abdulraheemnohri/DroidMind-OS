package com.droidmind.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidmind.database.AdLogDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val adsBlocked: Int = 0,
    val batterySaved: String = "0%",
    val focusTime: String = "0h",
    val threatsStopped: Int = 0
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val adLogDao: AdLogDao
) : ViewModel() {
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState

    init {
        updateStats()
    }

    fun updateStats() {
        viewModelScope.launch {
            val totalBlocked = adLogDao.getTotalAdsBlocked()
            _uiState.value = _uiState.value.copy(
                adsBlocked = totalBlocked,
                batterySaved = "15%",
                focusTime = "2.5h",
                threatsStopped = 12
            )
        }
    }
}
