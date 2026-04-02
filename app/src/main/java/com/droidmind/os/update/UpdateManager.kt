package com.droidmind.os.update

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class UpdateManager(private val context: Context) {
    private val updateUrl = "https://abdulraheemnohri.github.io/DroidMind-OS/update.json"

    data class UpdateInfo(
        val latestVersion: String,
        val downloadUrl: String,
        val releaseNotes: String,
        val isCritical: Boolean
    )

    suspend fun checkForUpdate(): UpdateInfo? = withContext(Dispatchers.IO) {
        try {
            val url = URL(updateUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connect()

            if (connection.responseCode == 200) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val json = JSONObject(response)

                val currentVersion = context.packageManager.getPackageInfo(context.packageName, 0).versionName
                val latestVersion = json.getString("latestVersion")

                if (currentVersion != latestVersion) {
                    UpdateInfo(
                        latestVersion = latestVersion,
                        downloadUrl = json.getString("downloadUrl"),
                        releaseNotes = json.getString("releaseNotes"),
                        isCritical = json.getBoolean("isCritical")
                    )
                } else null
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
