package com.droidmind.core.ai

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class AIEngine(private val context: Context) {
    private var interpreter: Interpreter? = null

    fun loadModel(modelPath: String) {
        try {
            val modelBuffer = loadModelFile(modelPath)
            val options = Interpreter.Options()
            interpreter = Interpreter(modelBuffer, options)
        } catch (e: Exception) {
            e.printStackTrace()
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
        interpreter?.runForMultipleInputsOutputs(input, output)
    }

    fun close() {
        interpreter?.close()
        interpreter = null
    }
}
