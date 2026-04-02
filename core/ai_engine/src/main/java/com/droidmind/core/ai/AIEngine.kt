package com.droidmind.core.ai

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class AIEngine(private val context: Context) {
    private var interpreter: Interpreter? = null
    private var isSimulatedMode = false

    fun loadModel(modelPath: String) {
        try {
            val modelBuffer = loadModelFile(modelPath)
            if (modelBuffer.capacity() == 0) {
                throw IllegalArgumentException("Empty model file")
            }
            val options = Interpreter.Options()
            interpreter = Interpreter(modelBuffer, options)
            isSimulatedMode = false
        } catch (e: Exception) {
            e.printStackTrace()
            // Enable simulated mode if valid model is missing (e.g. in blueprint/CI)
            isSimulatedMode = true
        }
    }

    private fun loadModelFile(modelPath: String): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    fun runInference(input: Array<Any>, output: Map<Int, Any>) {
        if (isSimulatedMode) {
            simulateInference(output)
            return
        }
        interpreter?.runForMultipleInputsOutputs(input, output)
    }

    private fun simulateInference(output: Map<Int, Any>) {
        // Placeholder for simulated results when models are missing
        output.forEach { (_, value) ->
            if (value is FloatArray) {
                for (i in value.indices) value[i] = 0.5f
            }
        }
    }

    fun close() {
        interpreter?.close()
        interpreter = null
    }
}
