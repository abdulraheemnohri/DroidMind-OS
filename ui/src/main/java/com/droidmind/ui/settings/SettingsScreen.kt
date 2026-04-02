package com.droidmind.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(24.dp))

        SettingsSection(title = "Privacy & Security", icon = Icons.Default.Security) {
            ToggleItem(
                label = "AdBlockerX",
                description = "System-wide DNS ad and tracker blocking",
                checked = uiState.isAdBlockerXEnabled
            ) { viewModel.toggleAdBlockerX(it) }

            ToggleItem(
                label = "PrivacyShield",
                description = "Detect suspicious app behavior and permission abuse",
                checked = uiState.isPrivacyShieldEnabled
            ) { /* viewModel toggle */ }

            ToggleItem(
                label = "NetworkNinja",
                description = "VPN firewall and network anomaly detection",
                checked = uiState.isNetworkNinjaEnabled
            ) { /* viewModel toggle */ }
        }

        Spacer(modifier = Modifier.height(24.dp))

        SettingsSection(title = "Optimization", icon = Icons.Default.Bolt) {
            ToggleItem(
                label = "PowerForge",
                description = "AI-powered battery optimization and idle app control",
                checked = uiState.isPowerForgeEnabled
            ) { viewModel.togglePowerForge(it) }

            ToggleItem(
                label = "FocusFlow",
                description = "Smart DND and notification batching",
                checked = uiState.isFocusFlowEnabled
            ) { /* viewModel toggle */ }
        }

        Spacer(modifier = Modifier.height(24.dp))

        SettingsSection(title = "AI Engine", icon = Icons.Default.AutoAwesome) {
            ListItem(
                headlineContent = { Text("AI Personality") },
                supportingContent = { Text("Current: ${uiState.aiPersonality}") },
                trailingContent = {
                    TextButton(onClick = { /* show personality picker */ }) {
                        Text("Change")
                    }
                }
            )
        }
    }
}

@Composable
fun SettingsSection(title: String, icon: ImageVector, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(12.dp))
            Text(title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                content()
            }
        }
    }
}

@Composable
fun ToggleItem(label: String, description: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    ListItem(
        headlineContent = { Text(label) },
        supportingContent = { Text(description) },
        trailingContent = {
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        },
        colors = ListItemDefaults.colors(containerColor = androidx.compose.ui.graphics.Color.Transparent)
    )
}
