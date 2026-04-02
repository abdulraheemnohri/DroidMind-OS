package com.droidmind.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        Text("Settings", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold)
        Spacer(modifier = Modifier.height(24.dp))

        // AI Personality & Autonomy Section
        SettingsSection(title = "AI Core Configuration", icon = Icons.Default.Psychology) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("AI Personality", style = MaterialTheme.typography.labelLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    AIPersonality.values().forEach { personality ->
                        FilterChip(
                            selected = uiState.personality == personality,
                            onClick = { viewModel.setPersonality(personality) },
                            label = { Text(personality.name.lowercase().capitalize(), fontSize = 10.sp) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text("Autonomy Level: ${(uiState.autonomyLevel * 100).toInt()}%", style = MaterialTheme.typography.labelLarge)
                Slider(
                    value = uiState.autonomyLevel,
                    onValueChange = { viewModel.setAutonomyLevel(it) },
                    valueRange = 0f..1f,
                    steps = 10
                )
                Text(
                    text = when {
                        uiState.autonomyLevel < 0.3f -> "Manual mode: AI only suggests actions."
                        uiState.autonomyLevel < 0.7f -> "Balanced: AI auto-executes high-confidence tasks."
                        else -> "Full Autonomy: AI manages system entirely."
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        SettingsSection(title = "Privacy & Security", icon = Icons.Default.Security) {
            ToggleItem(
                label = "AdShield",
                description = "System-wide DNS ad and tracker blocking",
                checked = uiState.isAdShieldEnabled
            ) { viewModel.toggleModule("AdShield", it) }

            ToggleItem(
                label = "PrivacyShield",
                description = "Detect suspicious app behavior and permission abuse",
                checked = uiState.isPrivacyShieldEnabled
            ) { viewModel.toggleModule("PrivacyShield", it) }

            ToggleItem(
                label = "NetworkNinja",
                description = "VPN firewall and network anomaly detection",
                checked = uiState.isNetworkNinjaEnabled
            ) { viewModel.toggleModule("NetworkNinja", it) }
        }

        Spacer(modifier = Modifier.height(24.dp))

        SettingsSection(title = "Optimization", icon = Icons.Default.Bolt) {
            ToggleItem(
                label = "PowerForge",
                description = "AI-powered battery optimization and idle app control",
                checked = uiState.isPowerForgeEnabled
            ) { viewModel.toggleModule("PowerForge", it) }

            ToggleItem(
                label = "FocusFlow",
                description = "Smart DND and notification batching",
                checked = uiState.isFocusFlowEnabled
            ) { viewModel.toggleModule("FocusFlow", it) }
        }
    }
}

@Composable
fun SettingsSection(title: String, icon: ImageVector, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(12.dp))
            Text(title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
fun ToggleItem(label: String, description: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    ListItem(
        headlineContent = { Text(label, fontWeight = FontWeight.SemiBold) },
        supportingContent = { Text(description) },
        trailingContent = {
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        },
        colors = ListItemDefaults.colors(containerColor = androidx.compose.ui.graphics.Color.Transparent)
    )
}

private fun String.capitalize() = replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
