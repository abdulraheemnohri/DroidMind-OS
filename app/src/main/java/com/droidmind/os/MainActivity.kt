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
import android.content.Intent
import android.net.Uri
import com.droidmind.ui.theme.DroidMindTheme
import com.droidmind.os.update.UpdateManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import androidx.compose.material3.SnackbarResult

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
                val updateInfo = updateManager.checkForUpdate()
                if (updateInfo != null) {
                    val result = snackbarHostState.showSnackbar(
                        message = "Update available: ${updateInfo.latestVersion}",
                        actionLabel = "Download"
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(updateInfo.downloadUrl))
                        startActivity(intent)
                    }
                }
            }

            DroidMindTheme {
                DroidMindNavigation(
                    dashboardScreen = {
                        DashboardScreen(dashboardViewModel)
                        SnackbarHost(hostState = snackbarHostState)
                    },
                    settingsScreen = { SettingsScreen(settingsViewModel) }
                )
            }
        }
    }
}
