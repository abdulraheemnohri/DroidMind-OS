package com.droidmind.os

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.droidmind.ui.dashboard.DashboardScreen
import com.droidmind.ui.dashboard.DashboardViewModel
import com.droidmind.ui.navigation.DroidMindNavigation
import com.droidmind.ui.settings.SettingsScreen
import com.droidmind.ui.settings.SettingsViewModel
import com.droidmind.ui.theme.DroidMindTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DroidMindTheme {
                DroidMindNavigation(
                    dashboardScreen = { DashboardScreen(dashboardViewModel) },
                    settingsScreen = { SettingsScreen(settingsViewModel) }
                )
            }
        }
    }
}
