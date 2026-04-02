package com.droidmind.modules.adshield

import java.io.InputStream

/**
 * High-performance AdShield using a Trie matching algorithm.
 * Part of DroidMind OS v1.0 Ultimate Edition.
 */
class AdShield {
    private val root = TrieNode()
    private var blockedCount = 0

    private class TrieNode {
        val children = mutableMapOf<Char, TrieNode>()
        var isEndOfDomain = false
    }

    enum class Aggressiveness {
        MINIMAL, STANDARD, MAXIMUM
    }

    private var currentAggressiveness = Aggressiveness.STANDARD

    fun setAggressiveness(level: Aggressiveness) {
        currentAggressiveness = level
    }

    fun loadHosts(inputStream: InputStream) {
        inputStream.bufferedReader().useLines { lines ->
            lines.forEach { line ->
                val trimmed = line.trim()
                if (trimmed.isNotEmpty() && !trimmed.startsWith("#")) {
                    val parts = trimmed.split(Regex("\\s+"))
                    val domain = if (parts.size >= 2) parts[1] else parts[0]
                    insert(domain.reversed()) // Match from TLD for efficiency
                    blockedCount++
                }
            }
        }
    }

    private fun insert(domain: String) {
        var curr = root
        for (char in domain) {
            curr = curr.children.getOrPut(char) { TrieNode() }
        }
        curr.isEndOfDomain = true
    }

    fun isDomainBlocked(domain: String): Boolean {
        // AI-based obfuscated ad detection
        if (currentAggressiveness >= Aggressiveness.STANDARD && detectObfuscatedAd(domain)) {
            return true
        }

        val reversed = domain.reversed()
        var curr = root
        for (char in reversed) {
            curr = curr.children[char] ?: return false
            if (curr.isEndOfDomain) {
                // In MINIMAL mode, we might want to allow some "non-intrusive" ads
                // for simplicity here, we just block everything in the list.
                return true
            }
        }

        // MAXIMUM mode blocks anything that looks suspicious or is a sub-domain of a known tracker
        if (currentAggressiveness == Aggressiveness.MAXIMUM) {
             if (domain.contains("analytics") || domain.contains("telemetry") || domain.contains("tracker")) {
                 return true
             }
        }

        return curr.isEndOfDomain
    }

    private fun detectObfuscatedAd(domain: String): Boolean {
        // Implementation for AI detection of obfuscated ad domains
        // In a real scenario, this would call a TFLite model or use entropy analysis
        if (domain.length > 20 && domain.count { it.isDigit() } > 5) {
            // Very long domains with many digits are often used for tracking or dynamic ad serving
            return true
        }
        return false
    }

    /**
     * Returns a JavaScript snippet to be injected into WebViews for cosmetic ad blocking.
     */
    fun getWebInjectionScript(): String {
        return """
            (function() {
                const selectors = [
                    '.ad-container', '.google-ads', 'ins.adsbygoogle',
                    '[id^="div-gpt-ad"]', '.facebook-ad', '.video-ad'
                ];
                function hideAds() {
                    selectors.forEach(selector => {
                        document.querySelectorAll(selector).forEach(el => el.style.display = 'none');
                    });
                }
                hideAds();
                new MutationObserver(hideAds).observe(document.body, { childList: true, subtree: true });
            })();
        """.trimIndent()
    }

    fun getBlockedDomainsCount(): Int = blockedCount
}
