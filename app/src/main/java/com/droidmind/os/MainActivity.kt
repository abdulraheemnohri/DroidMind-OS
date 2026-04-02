package com.droidmind.os

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.droidmind.ui.dashboard.DashboardScreen
import com.droidmind.ui.dashboard.DashboardViewModel
import com.droidmind.ui.navigation.DroidMindNavigation
import com.droidmind.ui.settings.SettingsScreen
import com.droidmind.ui.settings.SettingsViewModel
import com.droidmind.ui.theme.DroidMindTheme
import com.droidmind.os.update.UpdateManager
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.material3.SnackbarResult
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val updateManager = UpdateManager(this)

        setContent {
            val snackbarHostState = remember { SnackbarHostState() }

            LaunchedEffect(Unit) {
                try {
                    val updateInfo = updateManager.checkForUpdate()
                    if (updateInfo != null) {
                        val result = snackbarHostState.showSnackbar(
                            message = "Update available: ${updateInfo.latestVersion}",
                            actionLabel = "Download"
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            updateManager.downloadAndInstall(updateInfo)
                        }
                    }
                } catch (e: Exception) {
                    // Ignore background update errors during init
                }
            }

            DroidMindTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    DroidMindNavigation(
                        dashboardScreen = {
                            DashboardScreen(dashboardViewModel)
                        },
                        settingsScreen = { SettingsScreen(settingsViewModel) }
                    )
                    SnackbarHost(
                        hostState = snackbarHostState,
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            }
        }
    }
}
