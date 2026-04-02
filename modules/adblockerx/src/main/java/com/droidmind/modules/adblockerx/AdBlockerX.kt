package com.droidmind.modules.adblockerx

import java.io.InputStream

/**
 * High-performance AdBlockerX using a Trie matching algorithm.
 */
class AdBlockerX {
    private val root = TrieNode()
    private var blockedCount = 0

    private class TrieNode {
        val children = mutableMapOf<Char, TrieNode>()
        var isEndOfDomain = false
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
        val reversed = domain.reversed()
        var curr = root
        for (char in reversed) {
            curr = curr.children[char] ?: return false
            if (curr.isEndOfDomain) return true
        }
        return curr.isEndOfDomain
    }

    fun getBlockedDomainsCount(): Int = blockedCount
}
