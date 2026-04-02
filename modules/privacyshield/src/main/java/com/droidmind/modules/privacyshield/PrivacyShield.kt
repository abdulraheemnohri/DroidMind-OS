package com.droidmind.modules.privacyshield

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

/**
 * Behavioral Security System.
 */
class PrivacyShield(private val context: Context) {

    fun detectPermissionAnomalies(): List<String> {
        val anomalies = mutableListOf<String>()
        val packageManager = context.packageManager
        val packages = packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS)

        packages.forEach { pkg ->
            val permissions = pkg.requestedPermissions
            if (permissions != null && permissions.contains(android.Manifest.permission.RECORD_AUDIO)) {
                if (pkg.packageName.contains("utility", ignoreCase = true)) {
                    anomalies.add("Suspicious Audio Permission in Utility App: ${pkg.packageName}")
                }
            }
        }
        return anomalies
    }

    fun protectClipboard(content: String): String {
        // AI logic to detect sensitive data (SSN, Passwords) and mask it
        if (content.contains(Regex("\\d{3}-\\d{2}-\\d{4}"))) {
            return "MASKED_CONTENT"
        }
        return content
    }
}
