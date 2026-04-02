package com.droidmind.ui.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class SettingsUiState(
    val isPowerForgeEnabled: Boolean = true,
    val isAdBlockerXEnabled: Boolean = true,
    val isNetworkNinjaEnabled: Boolean = true,
    val isPrivacyShieldEnabled: Boolean = true,
    val isFocusFlowEnabled: Boolean = true,
    val aiPersonality: String = "Balanced"
)

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState

    fun togglePowerForge(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(isPowerForgeEnabled = enabled)
    }

    fun toggleAdBlockerX(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(isAdBlockerXEnabled = enabled)
    }

    fun setAIPersonality(personality: String) {
        _uiState.value = _uiState.value.copy(aiPersonality = personality)
    }
}
