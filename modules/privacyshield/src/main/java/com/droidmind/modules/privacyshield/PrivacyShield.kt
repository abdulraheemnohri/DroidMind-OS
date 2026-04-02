package com.droidmind.modules.privacyshield

import android.content.Context
import android.content.pm.PackageManager
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Behavioral Security System for DroidMind OS v1.0 Ultimate Edition.
 * Features: Permission anomaly detection, clipboard sanitization, decoy mode, and log analysis.
 */
class PrivacyShield(private val context: Context) {

    private var isDecoyModeEnabled = false

    fun setDecoyMode(enabled: Boolean) {
        isDecoyModeEnabled = enabled
    }

    fun isDecoyModeActive(): Boolean = isDecoyModeEnabled

    fun detectPermissionAnomalies(): List<String> {
        val anomalies = mutableListOf<String>()
        val packageManager = context.packageManager
        val packages = packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS)

        packages.forEach { pkg ->
            val permissions = pkg.requestedPermissions
            if (permissions != null && (permissions.contains(android.Manifest.permission.RECORD_AUDIO) ||
                permissions.contains(android.Manifest.permission.ACCESS_FINE_LOCATION))) {
                if (pkg.packageName.contains("utility", ignoreCase = true) ||
                    pkg.packageName.contains("torch", ignoreCase = true)) {
                    anomalies.add("Suspicious Permission in Utility App: ${pkg.packageName}")
                }
            }
        }
        return anomalies
    }

    fun analyzeSystemLogs(): List<String> {
        val suspiciousLogs = mutableListOf<String>()
        try {
            // Utilizes READ_LOGS permission to detect background data exfiltration or credential leaks
            val process = Runtime.getRuntime().exec("logcat -d")
            val bufferedReader = BufferedReader(InputStreamReader(process.inputStream))
            bufferedReader.useLines { lines ->
                lines.forEach { line ->
                    if (line.contains("password", ignoreCase = true) || line.contains("secret", ignoreCase = true)) {
                        suspiciousLogs.add("Potential data leak detected in logs: $line")
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return suspiciousLogs
    }

    fun protectClipboard(content: String): String {
        // AI logic to detect sensitive data (SSN, Passwords, Credit Cards) and mask it
        val patterns = listOf(
            Regex("\\d{3}-\\d{2}-\\d{4}"), // SSN
            Regex("\\d{4}-\\d{4}-\\d{4}-\\d{4}"), // Credit Card
            Regex("(?i)password[:=]\\s*\\w+") // Password
        )

        var masked = content
        patterns.forEach { pattern ->
            masked = pattern.replace(masked, " [HIDDEN BY PRIVACYSHIELD] ")
        }
        return masked
    }

    fun sanitizeScreenshot(pixelData: ByteArray): ByteArray {
        // CV model stub for blurring sensitive content in screenshots
        return pixelData
    }
}
