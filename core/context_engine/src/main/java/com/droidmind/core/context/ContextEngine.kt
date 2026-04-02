package com.droidmind.core.context

import android.content.Context
import android.location.Location
import android.os.BatteryManager
import java.util.Calendar

class ContextEngine(private val context: Context) {

    fun getCurrentState(): Map<String, Any> {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val batteryStatus = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val batteryLevel = batteryStatus.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

        return mapOf(
            "time" to hour,
            "battery_level" to batteryLevel,
            "is_weekend" to (calendar.get(Calendar.DAY_OF_WEEK) in listOf(Calendar.SATURDAY, Calendar.SUNDAY))
        )
    }

    fun getActionRecommendation(): String {
        val state = getCurrentState()
        val hour = state["time"] as Int
        val battery = state["battery_level"] as Int

        return when {
            battery < 15 -> "CLOSED_APPS_TO_SAVE_BATTERY"
            hour in 9..17 -> "ENABLE_FOCUS_MODE"
            hour >= 22 -> "ENABLE_DND_MODE"
            else -> "NO_ACTION"
        }
    }
}
