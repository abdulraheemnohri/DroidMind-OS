package com.droidmind.core.ai.manager

import android.content.Context
import com.droidmind.core.ai.AIEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

/**
 * Model Manager for DroidMind OS.
 * Handles the "First Run Flow": checks for model existence, downloads if missing, and loads into AIEngine.
 */
class ModelManager(private val context: Context, private val aiEngine: AIEngine) {

    private val modelsDir = File(context.filesDir, "models").apply { if (!exists()) mkdirs() }

    private val modelRegistry = mapOf(
        "llm.gguf" to "https://huggingface.co/bartowski/SmolLM2-360M-Instruct-GGUF/resolve/main/SmolLM2-360M-Instruct-Q4_K_M.gguf",
        "usage_classifier.tflite" to "https://storage.googleapis.com/download.tensorflow.org/models/tflite/task_library/recommendation/android/recommendation.tflite",
        "anomaly_detector.tflite" to "https://storage.googleapis.com/download.tensorflow.org/models/tflite/anomaly_detection/model.tflite",
        "mobilenet_classifier.tflite" to "https://storage.googleapis.com/download.tensorflow.org/models/tflite/task_library/image_classification/android/mobilenet_v3_small_100_224.tflite",
        "ad_detector.tflite" to "https://storage.googleapis.com/download.tensorflow.org/models/tflite/task_library/object_detection/android/ssd_mobilenet_v2.tflite"
    )

    /**
     * Executes the first run flow.
     */
    suspend fun initializeModels(onProgress: (String, Float) -> Unit) = withContext(Dispatchers.IO) {
        modelRegistry.forEach { (name, url) ->
            val modelFile = File(modelsDir, name)
            if (!modelFile.exists()) {
                downloadModel(name, url, modelFile, onProgress)
            }
        }
        // Load interpreters (Simulated logic for blueprint)
        // aiEngine.loadFromInternalStorage(modelsDir)
    }

    private fun downloadModel(name: String, urlString: String, destination: File, onProgress: (String, Float) -> Unit) {
        try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()

            val fileLength = connection.contentLength
            url.openStream().use { input ->
                destination.outputStream().use { output ->
                    val data = ByteArray(8192)
                    var total = 0L
                    var count: Int
                    while (input.read(data).also { count = it } != -1) {
                        total += count
                        if (fileLength > 0) {
                            onProgress(name, total.toFloat() / fileLength)
                        }
                        output.write(data, 0, count)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun areModelsReady(): Boolean {
        return modelRegistry.keys.all { File(modelsDir, it).exists() }
    }
}
