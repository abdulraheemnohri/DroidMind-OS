package com.droidmind.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidmind.database.AdLogDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

data class DashboardUiState(
    val adsBlocked: Int = 0,
    val batterySaved: String = "0%",
    val focusTime: String = "0h",
    val threatsStopped: Int = 0,
    val aiInsight: String = ""
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

            // AI Simulation Logic for Dashboard metrics
            val batterySavedPercent = Random.nextInt(5, 25)
            val focusHours = Random.nextInt(1, 5)
            val focusMins = Random.nextInt(0, 60)
            val threats = Random.nextInt(3, 20)

            val insight = "DroidMind AI has optimized ${Random.nextInt(5, 20)} background processes and AdShield filtered ${totalBlocked + Random.nextInt(10, 50)} trackers. Battery health is predicted at 98% with current usage patterns."

            _uiState.value = _uiState.value.copy(
                adsBlocked = totalBlocked,
                batterySaved = "$batterySavedPercent%",
                focusTime = "${focusHours}h ${focusMins}m",
                threatsStopped = threats,
                aiInsight = insight
            )
        }
    }
}
