package com.droidmind.services.automation

import com.droidmind.core.ai.AIEngine

/**
 * AI Automation Engine for DroidMind OS.
 * Converts natural language commands into system automation rules using local LLM reasoning.
 */
class AutomationEngine(private val aiEngine: AIEngine) {

    data class Rule(
        val trigger: String,
        val condition: String?,
        val action: String,
        val confidence: Float
    )

    private val activeRules = mutableListOf<Rule>()

    /**
     * Converts a natural language command into an automation rule.
     * Example: "When I reach office turn on focus mode"
     */
    fun processCommand(command: String): Rule {
        // Local LLM (SmolLM2) is called to parse the natural language into structured triggers/actions.
        val llmResponse = aiEngine.executeLocalLLM("Parse this automation: $command. Format as Trigger: [T], Action: [A]")

        // Simulating parsing of LLM's structured output
        return when {
            command.contains("office", ignoreCase = true) && command.contains("focus", ignoreCase = true) -> {
                Rule(trigger = "LOCATION_OFFICE", condition = null, action = "ENABLE_FOCUS_MODE", confidence = 0.98f)
            }
            command.contains("night", ignoreCase = true) && command.contains("dnd", ignoreCase = true) -> {
                Rule(trigger = "TIME_22:00", condition = null, action = "ENABLE_DND", confidence = 0.95f)
            }
            command.contains("battery", ignoreCase = true) && command.contains("save", ignoreCase = true) -> {
                Rule(trigger = "BATTERY_LOW", condition = "LEVEL_20", action = "ENABLE_POWER_FORGE", confidence = 0.99f)
            }
            else -> Rule(trigger = "UNKNOWN", condition = null, action = "LOG_COMMAND", confidence = 0.5f)
        }
    }

    fun addRule(rule: Rule) {
        if (rule.trigger != "UNKNOWN" && rule.confidence > 0.8f) {
            activeRules.add(rule)
        }
    }

    fun executeTriggers(trigger: String) {
        activeRules.filter { it.trigger == trigger }.forEach { rule ->
            // Execute action through relevant module/service using AI context (Stub)
            println("AI Automation: Executing ${rule.action} triggered by ${rule.trigger}")
        }
    }

    fun getActiveRules(): List<Rule> = activeRules
}
