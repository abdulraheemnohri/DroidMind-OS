package com.droidmind.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Settings", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(24.dp))

        ToggleItem("PowerForge", uiState.isPowerForgeEnabled) { viewModel.togglePowerForge(it) }
        ToggleItem("AdBlockerX", uiState.isAdBlockerXEnabled) { viewModel.toggleAdBlockerX(it) }

        Spacer(modifier = Modifier.height(16.dp))
        Text("AI Personality: ${uiState.aiPersonality}", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun ToggleItem(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
