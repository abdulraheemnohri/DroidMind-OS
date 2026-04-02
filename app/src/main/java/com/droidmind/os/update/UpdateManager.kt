package com.droidmind.os.update

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

class UpdateManager(private val context: Context) {
    // In a real app, this would be fetched from BuildConfig or a RemoteConfig service
    private var updateUrl = "https://abdulraheemnohri.github.io/DroidMind-OS/update.json"

    fun setUpdateUrl(url: String) {
        updateUrl = url
    }

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

    suspend fun downloadAndInstall(updateInfo: UpdateInfo) = withContext(Dispatchers.IO) {
        try {
            val url = URL(updateInfo.downloadUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()

            val file = File(context.externalCacheDir, "update.apk")
            connection.inputStream.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            installApk(file)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun installApk(file: File) {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.android.package-archive")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}
