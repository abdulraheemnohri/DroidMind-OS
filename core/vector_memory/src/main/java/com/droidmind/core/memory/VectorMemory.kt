package com.droidmind.core.memory

import java.io.File
import kotlin.math.sqrt

/**
 * AI Vector Memory System for DroidMind OS.
 * Remembers user behavioral patterns for predictive prefetching and RAM optimization.
 */
class VectorMemory {
    private val memoryStore = mutableMapOf<String, List<FloatArray>>()

    /**
     * Stores a high-dimensional vector representing a user behavior pattern.
     * Example: [Time, LocationID, WiFiState, BatteryLevel, AppID]
     */
    fun storePattern(key: String, pattern: FloatArray) {
        val existingPatterns = memoryStore.getOrDefault(key, emptyList()).toMutableList()
        existingPatterns.add(pattern)
        // Keep only last 100 patterns to avoid memory issues
        if (existingPatterns.size > 100) {
            existingPatterns.removeAt(0)
        }
        memoryStore[key] = existingPatterns
    }

    /**
     * Retrieves similar past patterns to predict future behavior.
     * Useful for NeuralCache preloading and PowerForge hibernation.
     */
    fun retrieveSimilarPatterns(key: String, currentPattern: FloatArray): List<FloatArray> {
        val storedPatterns = memoryStore.getOrDefault(key, emptyList())
        return storedPatterns.filter {
            calculateCosineSimilarity(it, currentPattern) > 0.85
        }
    }

    /**
     * Returns a prediction for a system optimization based on stored vector patterns.
     * Example: "PREFETCH_YOUTUBE", "OPTIMIZE_RAM_FOR_GAMING", "POWER_SAVE_HINT"
     */
    fun predictOptimizationHint(currentPattern: FloatArray): String {
        val similarPatterns = retrieveSimilarPatterns("global_patterns", currentPattern)

        if (similarPatterns.size > 5) {
            val mostFrequentAction = similarPatterns.map { it.last() }.groupBy { it }.maxByOrNull { it.value.size }?.key ?: -1.0f
            return when (mostFrequentAction.toInt()) {
                1 -> "HINT_PREFETCH_NETWORK"
                2 -> "HINT_OPTIMIZE_RAM"
                3 -> "HINT_CPU_THROTTLE"
                else -> "HINT_NO_OPTIMIZATION"
            }
        }
        return "HINT_INSUFFICIENT_DATA"
    }

    private fun calculateCosineSimilarity(v1: FloatArray, v2: FloatArray): Float {
        if (v1.size != v2.size) return 0.0f
        var dotProduct = 0.0f
        var normA = 0.0f
        var normB = 0.0f
        for (i in v1.indices) {
            dotProduct += v1[i] * v2[i]
            normA += v1[i] * v1[i]
            normB += v2[i] * v2[i]
        }
        val denominator = sqrt(normA.toDouble()) * sqrt(normB.toDouble())
        return if (denominator == 0.0) 0.0f else (dotProduct / denominator).toFloat()
    }
}
