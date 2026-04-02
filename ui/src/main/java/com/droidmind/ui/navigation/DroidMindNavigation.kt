package com.droidmind.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun DroidMindNavigation(
    dashboardScreen: @Composable () -> Unit,
    settingsScreen: @Composable () -> Unit
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Dashboard.route) {
        composable(Screen.Dashboard.route) {
            dashboardScreen()
        }
        composable(Screen.Settings.route) {
            settingsScreen()
        }
    }
}
