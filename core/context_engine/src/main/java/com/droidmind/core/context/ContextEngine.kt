package com.droidmind.core.context

import android.content.Context
import android.location.Location
import android.os.BatteryManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.hardware.Sensor
import android.hardware.SensorManager
import java.util.Calendar

/**
 * AI Context Engine for DroidMind OS.
 * Combines signals from time, location, calendar, battery, motion, and network.
 */
class ContextEngine(private val context: Context) {

    private var lastMotionState: Int = 0 // 0: Still, 1: Moving, 2: Running

    fun getCurrentState(): Map<String, Any> {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        val batteryStatus = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val batteryLevel = batteryStatus.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        val isWiFi = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true

        return mapOf(
            "time" to hour,
            "battery_level" to batteryLevel,
            "is_weekend" to (calendar.get(Calendar.DAY_OF_WEEK) in listOf(Calendar.SATURDAY, Calendar.SUNDAY)),
            "is_wifi" to isWiFi,
            "motion_state" to lastMotionState,
            "is_charging" to batteryStatus.isCharging
        )
    }

    /**
     * Recommends a system action based on the combined context signals.
     */
    fun getActionRecommendation(): String {
        val state = getCurrentState()
        val hour = state["time"] as Int
        val battery = state["battery_level"] as Int
        val isWiFi = state["is_wifi"] as Boolean
        val motion = state["motion_state"] as Int

        return when {
            battery < 15 -> "OPTIMIZE_POWERFORGE_SAVE_MODE"
            hour in 9..17 && motion == 0 -> "ENABLE_FOCUS_FLOW_OFFICE_MODE"
            hour >= 22 && !isWiFi -> "SUSPEND_BACKGROUND_SYNC_NETWORK_NINJA"
            motion == 2 -> "DISABLE_NOTIFICATIONS_FOR_SAFETY"
            else -> "NO_ACTION"
        }
    }

    fun updateMotionState(state: Int) {
        lastMotionState = state
    }
}
