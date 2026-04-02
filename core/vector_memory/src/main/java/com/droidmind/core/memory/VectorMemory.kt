package com.droidmind.core.memory

/**
 * AI remembers patterns of user behavior.
 * Example: User opens YouTube every night at 8PM.
 * System prepares: prefetch network, optimize RAM.
 */
class VectorMemory {
    private val memoryStore = mutableMapOf<String, List<FloatArray>>()

    fun storePattern(key: String, pattern: FloatArray) {
        val existingPatterns = memoryStore.getOrDefault(key, emptyList()).toMutableList()
        existingPatterns.add(pattern)
        // Keep only last 100 patterns to avoid memory issues
        if (existingPatterns.size > 100) {
            existingPatterns.removeAt(0)
        }
        memoryStore[key] = existingPatterns
    }

    fun retrieveSimilarPatterns(key: String, currentPattern: FloatArray): List<FloatArray> {
        val storedPatterns = memoryStore.getOrDefault(key, emptyList())
        return storedPatterns.filter {
            calculateCosineSimilarity(it, currentPattern) > 0.85
        }
    }

    private fun calculateCosineSimilarity(v1: FloatArray, v2: FloatArray): Float {
        var dotProduct = 0.0f
        var normA = 0.0f
        var normB = 0.0f
        for (i in v1.indices) {
            dotProduct += v1[i] * v2[i]
            normA += v1[i] * v1[i]
            normB += v2[i] * v2[i]
        }
        return dotProduct / (Math.sqrt(normA.toDouble()) * Math.sqrt(normB.toDouble())).toFloat()
    }
}
