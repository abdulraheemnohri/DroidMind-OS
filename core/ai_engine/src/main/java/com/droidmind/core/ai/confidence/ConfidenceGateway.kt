package com.droidmind.core.ai.confidence

import kotlin.math.max

/**
 * AI Confidence Gateway for DroidMind OS.
 * Manages model decision-making by applying whitelists, blacklists, and confidence thresholds.
 */
class ConfidenceGateway {

    enum class ActionType {
        KILL_APP,
        BLOCK_DOMAIN,
        ENABLE_FOCUS,
        SUSPEND_SERVICES,
        NOTIFY_USER
    }

    private val userThreshold = 0.85f // Default 85%
    private val actionCooldowns = mutableMapOf<String, Long>()
    private val whitelist = mutableSetOf<String>()
    private val blacklist = mutableSetOf<String>()

    fun setThreshold(value: Float) {
        // value should be 0.0 - 1.0
    }

    fun isActionAuthorized(
        action: ActionType,
        target: String,
        confidence: Float
    ): Boolean {
        // 1. Confidence check: ModelConfidence > UserThreshold
        if (confidence < userThreshold) return false

        // 2. Blacklist check: ActionType NOT in Blacklist
        if (blacklist.contains(action.name)) return false

        // 3. Whitelist check: TargetApp NOT in Whitelist
        if (whitelist.contains(target)) return false

        // 4. Cooldown check: Has enough time passed since the last similar action?
        val lastActionTime = actionCooldowns[target] ?: 0L
        val currentTime = System.currentTimeMillis()
        val cooldownPeriod = 1000 * 60 * 60 // 1 hour

        if (currentTime - lastActionTime < cooldownPeriod) return false

        // Record action time
        actionCooldowns[target] = currentTime

        return true
    }

    fun addToWhitelist(target: String) {
        whitelist.add(target)
    }

    fun addToBlacklist(action: ActionType) {
        blacklist.add(action.name)
    }

    fun removeFromWhitelist(target: String) {
        whitelist.remove(target)
    }

    fun clearCooldowns() {
        actionCooldowns.clear()
    }
}
