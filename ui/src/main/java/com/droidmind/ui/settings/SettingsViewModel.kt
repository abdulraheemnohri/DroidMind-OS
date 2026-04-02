package com.droidmind.ui.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

enum class AIPersonality {
    CONSERVATIVE, BALANCED, AGGRESSIVE, GUARDIAN
}

data class SettingsUiState(
    val personality: AIPersonality = AIPersonality.BALANCED,
    val autonomyLevel: Float = 0.5f,
    val isPowerForgeEnabled: Boolean = true,
    val isAdShieldEnabled: Boolean = true,
    val isNetworkNinjaEnabled: Boolean = true,
    val isPrivacyShieldEnabled: Boolean = true,
    val isFocusFlowEnabled: Boolean = true
)

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState

    fun setPersonality(personality: AIPersonality) {
        _uiState.value = _uiState.value.copy(personality = personality)
    }

    fun setAutonomyLevel(level: Float) {
        _uiState.value = _uiState.value.copy(autonomyLevel = level)
    }

    fun toggleModule(moduleName: String, enabled: Boolean) {
        _uiState.value = when (moduleName) {
            "PowerForge" -> _uiState.value.copy(isPowerForgeEnabled = enabled)
            "AdShield" -> _uiState.value.copy(isAdShieldEnabled = enabled)
            "NetworkNinja" -> _uiState.value.copy(isNetworkNinjaEnabled = enabled)
            "PrivacyShield" -> _uiState.value.copy(isPrivacyShieldEnabled = enabled)
            "FocusFlow" -> _uiState.value.copy(isFocusFlowEnabled = enabled)
            else -> _uiState.value
        }
    }
}
